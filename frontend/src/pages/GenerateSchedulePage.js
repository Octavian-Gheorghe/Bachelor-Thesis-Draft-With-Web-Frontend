import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import API_BASE_URL from '../config';

const GenerateSchedulePage = () => {
  const { authToken, loggedUsername } = useAuth();
  const [activityIdeas, setActivityIdeas] = useState([]);
  const [selectedIds, setSelectedIds] = useState([]);
  const [scheduleName, setScheduleName] = useState('');
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [generating, setGenerating] = useState(false);

  useEffect(() => {
  if (!authToken || !loggedUsername) return;

  const fetchIdeas = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_BASE_URL}/activity-ideas/for/${loggedUsername}`, {
        headers: { Authorization: `Bearer ${authToken}` }
      });

      if (response.ok) {
        const data = await response.json();
        setActivityIdeas(data);
      } else {
        setError('Failed to load activity ideas.');
      }
    } catch {
      setError('Something went wrong. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  fetchIdeas();
}, [authToken, loggedUsername]);

  const toggleSelection = (id) => {
    setSelectedIds((prev) =>
      prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id]
    );
  };

  const handleSubmit = async () => {
    if (!scheduleName.trim() || selectedIds.length === 0) {
      setError('Please enter a schedule name and select at least one activity.');
      return;
    }

    const invalids = [];

    for (const id of selectedIds) {
      const res = await fetch(`${API_BASE_URL}/activity-ideas/${id}`, {
        headers: { Authorization: `Bearer ${authToken}` }
      });

      if (res.ok) {
        const act = await res.json();
        const hasIntervals = act.temporalIntervals?.length > 0;
        const hasLocations = act.locations?.length > 0;
        if (!hasIntervals || !hasLocations) {
          invalids.push(`${act.name} (ID: ${id})`);
        }
      } else {
        setError(`Failed to fetch activity with ID ${id}.`);
        return;
      }
    }

    if (invalids.length > 0) {
      setError(`These activities are missing required data (at least one interval and one location):\n${invalids.join(', ')}`);
      return;
    }

    setError(null);
    setGenerating(true);

    try {
      const response = await fetch(`${API_BASE_URL}/scheduler/${loggedUsername}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${authToken}`
        },
        body: JSON.stringify({ name: scheduleName, activityIdeaIds: selectedIds })
      });

      if (response.ok) {
        const data = await response.json();
        navigate('/generated-schedule', { state: { result: data } });
      } else {
        setError('Schedule generation failed.');
      }
    } catch {
      setError('An unexpected error occurred.');
    } finally {
      setGenerating(false);
    }
  };

  return (

    <div
      style={{
        background: 'linear-gradient(to bottom right, #483AA0, #7965C1)',
        minHeight: '100vh',
        color: '#E3D095',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        padding: '2rem'
      }}
    >
          

      <h1>Generate a New Schedule</h1>

      <label style={{ fontWeight: 'bold' }}>Schedule Name:</label>
      <input
        value={scheduleName}
        onChange={(e) => setScheduleName(e.target.value)}
        style={{
          margin: '10px',
          padding: '10px',
          borderRadius: '8px',
          border: '2px solid #E3D095',
          backgroundColor: '#0E2148',
          color: '#E3D095'
        }}
      />

      <h3>Select Activities:</h3>
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {activityIdeas.map((activity) => (
          <li key={activity.id} style={{ margin: '5px 0' }}>
            <input
              type="checkbox"
              checked={selectedIds.includes(activity.id)}
              onChange={() => toggleSelection(activity.id)}
            />{' '}
            {activity.name}
          </li>
        ))}
      </ul>

      {loading && <div style={modalStyle}>Loading your activities...</div>}

          {!loading && error && (
            <div style={errorStyle}>{error}</div>
          )}

          {!loading && !error && activityIdeas.length === 0 && (
            <p style={{ fontStyle: 'italic' }}>No activities available to choose from.</p>
          )}

      <button
        onClick={handleSubmit}
        style={{
          borderRadius: '8px',
          border: '2px solid #E3D095',
          backgroundColor: '#0E2148',
          color: '#E3D095',
          fontWeight: 'bold',
          padding: '8px 16px',
          cursor: 'pointer',
          transition: 'all 0.3s ease'
        }}
      >
        Generate
      </button>
      {generating && <div style={modalStyle}>Please wait while your schedule is being generated...</div>}

    </div>
  );
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



export default GenerateSchedulePage;