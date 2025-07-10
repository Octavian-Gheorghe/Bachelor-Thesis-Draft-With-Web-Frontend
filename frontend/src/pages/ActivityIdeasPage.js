import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import API_BASE_URL from '../config';
import { useNavigate } from 'react-router-dom';

const ActivityIdeasPage = () => {
  const { authToken, loggedUsername } = useAuth();
  const [activities, setActivities] = useState([]);
  const [username, setUsername] = useState(null);
  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');
  const navigate = useNavigate();

  const goToCreatePage = () => navigate('/create-activity');
  const goToConstraintsPage = () => navigate('/constraints');

  useEffect(() => {
    if (!authToken) return;

    const fetchActivities = async () => {
      const username = loggedUsername;
      if (!username) return;
      setUsername(username);
      setLoading(true);
      setErrorMsg('');

      try {
        const response = await fetch(`${API_BASE_URL}/activity-ideas/for/${username}`, {
          headers: {
            Authorization: `Bearer ${authToken}`
          }
        });

        if (response.ok) {
          const data = await response.json();
          setActivities(data);
        } else {
          setErrorMsg('Something went wrong. Please try again later.');
        }
      } catch (error) {
        setErrorMsg('Something went wrong. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchActivities();
  }, [authToken, loggedUsername]);

  return (
    <div
      style={{
        background: 'linear-gradient(to bottom right, #483AA0, #7965C1)',
        minHeight: '100vh',
        padding: '20px',
        color: '#E3D095'
      }}
    >
      <div style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
        <button
          onClick={goToCreatePage}
          style={buttonStyle}
          className="hoverable-button"
        >
          +
        </button>

        <button
          onClick={goToConstraintsPage}
          style={buttonStyle}
          className="hoverable-button"
        >
          Constraints and Preferences
        </button>
      </div>

     <h1>All of your created activities</h1>

      {loading && <div style={modalStyle}>Loading your activities...</div>}

      {errorMsg && !loading && <p style={errorStyle}>{errorMsg}</p>}

      {!loading && activities.length === 0 && !errorMsg && (
        <p style={{ fontStyle: 'italic' }}>No activities provided yet!</p>
      )}

      {activities.map((activity) => {
        const isInterruptible =
          activity.smin !== null ||
          activity.smax !== null ||
          activity.dmin !== null ||
          activity.dmax !== null;

        return (
          <div
            key={activity.id}
            onClick={() => navigate(`/activities/${activity.id}`)}
            className="activity-card"
            style={activityCardStyle}
          >
            <h2 style={{ color: '#E3D095', fontWeight: 'bold' }}>
              {activity.name}
            </h2>
            <p>
              <strong>Minimum duration:</strong>{' '}
              <span style={{ fontWeight: 'normal' }}>{activity.durimin} minutes</span>
            </p>
            <p>
              <strong>Maximum duration:</strong>{' '}
              <span style={{ fontWeight: 'normal' }}>{activity.durimax} minutes</span>
            </p>
            <p>
              <strong>Interruptible:</strong>{' '}
              <span style={{ fontWeight: 'normal' }}>{isInterruptible ? '✔️' : '❌'}</span>
            </p>

            {isInterruptible && (
              <>
                <p>
                  <strong>Minimum part duration:</strong>{' '}
                  <span style={{ fontWeight: 'normal' }}>{activity.smin ?? 'N/A'} minutes</span>
                </p>
                <p>
                  <strong>Maximum part duration:</strong>{' '}
                  <span style={{ fontWeight: 'normal' }}>{activity.smax ?? 'N/A'} minutes</span>
                </p>
                <p>
                  <strong>Minimum part distance:</strong>{' '}
                  <span style={{ fontWeight: 'normal' }}>
                    {activity.dmin === 0 ? 'none' : `${activity.dmin} minutes`}
                  </span>
                </p>
                <p>
                  <strong>Maximum part distance:</strong>{' '}
                  <span style={{ fontWeight: 'normal' }}>
                    {activity.dmax > 9999 ? 'none' : `${activity.dmax} minutes`}
                  </span>
                </p>
              </>
            )}

            <h4>Locations:</h4>
            <ul>
              {activity.locations.map((loc) => (
                <li key={loc.id}>
                  <strong>{loc.name}</strong> (x: {loc.x}, y: {loc.y})
                </li>
              ))}
            </ul>

            <h4>Temporal Intervals:</h4>
            <ul>
              {activity.temporalIntervals.map((interval) => {
                const [startDate, startTime] = interval.startInterval.split('T');
                const [endDate, endTime] = interval.endInterval.split('T');

                return (
                  <li key={interval.id}>
                    {startDate} → {endDate}
                    <br />
                    <strong>{startTime}</strong> → <strong>{endTime}</strong>
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

const buttonStyle = {
  borderRadius: '8px',
  border: '2px solid #E3D095',
  backgroundColor: '#0E2148',
  color: '#E3D095',
  fontWeight: 'bold',
  padding: '8px 16px',
  cursor: 'pointer',
  transition: 'all 0.3s ease'
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
  maxWidth: '400px'
};

const styleSheet = document.styleSheets[0];
styleSheet.insertRule(`
  .hoverable-button:hover {
    background-color: #E3D095 !important;
    color: #0E2148 !important;
  }
`, styleSheet.cssRules.length);

styleSheet.insertRule(`
  .activity-card:hover {
    transform: scale(1.01); 
  }
`, styleSheet.cssRules.length);

const activityCardStyle = {
  border: '2px solid #E3D095',
  borderRadius: '10px',
  padding: '15px',
  margin: '15px 0',
  cursor: 'pointer',
  transition: 'transform 0.2s ease-in-out'
};

export default ActivityIdeasPage;
