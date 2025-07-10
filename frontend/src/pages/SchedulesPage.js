import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import API_BASE_URL from '../config';
import { useNavigate } from 'react-router-dom';

const SchedulesPage = () => {
  const { authToken, loggedUsername } = useAuth();
  const [schedules, setSchedules] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalText, setModalText] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    if (!authToken || !loggedUsername) return;

    const fetchSchedules = async () => {
      setLoading(true);
      setModalText('Loading your generated schedules...');
      setErrorMsg('');

      try {
        const response = await fetch(`${API_BASE_URL}/schedules/for/${loggedUsername}`, {
          headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
          const data = await response.json();
          setSchedules(data);
        } else {
          setErrorMsg('Something went wrong. Please try again later.');
        }
      } catch (error) {
        setErrorMsg('Something went wrong. Please try again later.');
      } finally {
        setLoading(false);
        setModalText('');
      }
    };

    fetchSchedules();
  }, [authToken, loggedUsername]);

  const handleCardClick = async (scheduleId) => {
    if (!authToken) return;

    setModalText('Loading selected schedule...');
    setLoading(true);

    try {
      const response = await fetch(`${API_BASE_URL}/schedules/${scheduleId}`, {
        headers: { Authorization: `Bearer ${authToken}` }
      });

      if (!response.ok) throw new Error('Failed to fetch schedule');

      const data = await response.json();

      navigate('/generated-schedule', {
        state: { result: data.scheduledActivityParts }
      });
    } catch (error) {
      alert('Failed to fetch schedule. Please try again.');
    } finally {
      setLoading(false);
      setModalText('');
    }
  };

  useEffect(() => {
    const styleSheet = document.styleSheets[0];
    if (styleSheet && styleSheet.insertRule) {
      styleSheet.insertRule(`
        .hoverable-schedule:hover {
          transform: scale(1.01);
        }
      `, styleSheet.cssRules.length);
      styleSheet.insertRule(`
        .hoverable-schedule {
          transition: transform 0.2s ease-in-out;
        }
      `, styleSheet.cssRules.length);
    }
  }, []);

  return (
    <div style={{
      minHeight: '100vh',
      padding: '20px',
      background: 'linear-gradient(to bottom right, #483AA0, #7965C1)',
      color: '#E3D095'
    }}>
      <h1 style={{ textAlign: 'center', fontWeight: 'bold' }}>All of your schedules</h1>

      {loading && modalText && <div style={modalStyle}>{modalText}</div>}
      {errorMsg && !loading && <div style={errorStyle}>{errorMsg}</div>}
      {!loading && schedules.length === 0 && !errorMsg && (
        <p style={{ fontStyle: 'italic', textAlign: 'center' }}>No generated schedules yet!</p>
      )}

      {schedules.map(schedule => {
        const startDate = schedule.startInterval.split('T')[0];
        const endDate = schedule.endInterval.split('T')[0];
        const startTime = schedule.startInterval.split('T')[1];
        const endTime = schedule.endInterval.split('T')[1];

        return (
          <div
            key={schedule.id}
            className="hoverable-schedule"
            style={scheduleCardStyle}
            onClick={() => handleCardClick(schedule.id)}
          >
            <h2 style={{ color: '#E3D095', fontWeight: 'bold' }}>{schedule.name}</h2>
            <p><strong>Start Interval:</strong> {startDate} → {endDate}</p>
            <p><strong>Time:</strong> <strong>{startTime}</strong> → <strong>{endTime}</strong></p>
            <h4 style={{ color: '#E3D095' }}>Scheduled Activity Parts:</h4>
            <ul>
              {schedule.scheduledActivityParts.map(part => {
                const partDate = part.startTime.split('T')[0];
                const partTime = part.startTime.split('T')[1];

                return (
                  <li key={part.id} style={{ marginBottom: '10px' }}>
                    <strong>{part.name}</strong><br />
                    <strong>Start Date:</strong> {partDate}<br />
                    <strong>Start Time:</strong> <strong>{partTime}</strong><br />
                    <strong>Duration:</strong> {part.duration} minutes<br />
                    <strong>Location:</strong> <strong>{part.locationName}</strong>
                  </li>
                );
              })}
            </ul>
          </div>
        );
      })}
    </div>
  );
};

const scheduleCardStyle = {
  border: '2px solid #E3D095',
  borderRadius: '12px',
  padding: '16px',
  margin: '20px auto',
  maxWidth: '600px',
  backgroundColor: '#0E2148',
  cursor: 'pointer'
};

const modalStyle = {
  backgroundColor: '#0E2148',
  padding: '20px 40px',
  borderRadius: '10px',
  color: '#E3D095',
  fontSize: '18px',
  border: '2px solid #E3D095',
  boxShadow: '0 0 15px rgba(0, 0, 0, 0.5)',
  maxWidth: '300px',
  margin: '20px auto',
  textAlign: 'center'
};

const errorStyle = {
  color: '#FFBABA',
  backgroundColor: '#4E0E0E',
  padding: '10px',
  borderRadius: '5px',
  marginBottom: '20px',
  textAlign: 'center',
  maxWidth: '400px',
  margin: '20px auto'
};

export default SchedulesPage;
