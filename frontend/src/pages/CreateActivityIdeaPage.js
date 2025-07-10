import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import API_BASE_URL from '../config';

const CreateActivityIdeaPage = () => {
  const { authToken, loggedUsername } = useAuth();
  const navigate = useNavigate();

  const [name, setName] = useState('');
  const [durimin, setDurimin] = useState('');
  const [durimax, setDurimax] = useState('');
  const [isInterruptible, setIsInterruptible] = useState(false);
  const [smin, setSmin] = useState('');
  const [smax, setSmax] = useState('');
  const [dmin, setDmin] = useState('');
  const [dmax, setDmax] = useState('');
  const [dminNone, setDminNone] = useState(false);
  const [dmaxNone, setDmaxNone] = useState(false);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const validateInput = () => {
    if (!name.trim()) return 'Name is required.';
    if (isNaN(durimin) || isNaN(durimax)) return 'Durations must be numbers.';
    if (parseInt(durimin) > parseInt(durimax)) return 'Minimum duration must be ≤ maximum duration.';

    if (isInterruptible) {
      const sMin = parseInt(smin);
      const sMax = parseInt(smax);
      const dMin = parseInt(durimin);
      const dMax = parseInt(durimax);

      if (isNaN(sMin) || isNaN(sMax)) return 'Part durations must be numbers.';
      if (sMin > sMax) return 'Minimum part duration must be ≤ maximum part duration.';
      if (sMin > dMin || sMin > dMax || sMax > dMax) return 'Part durations must be between total min and max.';
    }

    return null;
  };

  const handleSave = async () => {
    const validationError = validateInput();
    if (validationError) {
      setError(validationError);
      return;
    }

    const dto = {
      name,
      durimin: parseInt(durimin),
      durimax: parseInt(durimax),
      smin: isInterruptible ? parseInt(smin) : null,
      smax: isInterruptible ? parseInt(smax) : null,
      dmin: isInterruptible ? (dminNone ? 0 : parseInt(dmin)) : null,
      dmax: isInterruptible ? (dmaxNone ? 999999 : parseInt(dmax)) : null,
      user_id: loggedUsername
    };

    setIsLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/activity-ideas`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${authToken}`
        },
        body: JSON.stringify(dto)
      });

      if (response.ok) {
        navigate('/activities');
      } else {
        setError('Failed to create activity. Please try again later.');
      }
    } catch (error) {
      console.error('Error creating activity:', error);
      setError('Unexpected error occurred. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const inputStyle = {
    backgroundColor: '#0E2148',
    border: '1px solid #E3D095',
    color: '#E3D095',
    padding: '8px',
    borderRadius: '6px',
    marginBottom: '15px',
    width: '250px',
    textAlign: 'center'
  };

  const buttonStyle = {
    borderRadius: '8px',
    border: '2px solid #E3D095',
    backgroundColor: '#0E2148',
    color: '#E3D095',
    fontWeight: 'bold',
    padding: '10px 20px',
    cursor: 'pointer',
    transition: 'all 0.3s ease'
  };

  const modalStyle = {
    position: 'fixed',
    top: '40%',
    backgroundColor: '#0E2148',
    padding: '20px 40px',
    borderRadius: '10px',
    color: '#E3D095',
    fontSize: '18px',
    border: '2px solid #E3D095',
    boxShadow: '0 0 15px rgba(0, 0, 0, 0.5)',
    zIndex: 999,
    textAlign: 'center'
  };

  const styleSheet = document.styleSheets[0];
  if (styleSheet && styleSheet.insertRule) {
    styleSheet.insertRule(`
      .hoverable-button:hover {
        background-color: #E3D095 !important;
        color: #0E2148 !important;
      }
    `, styleSheet.cssRules.length);
  }

  return (
    <div style={{
      background: 'linear-gradient(to bottom right, #0E2148, #483AA0)',
      minHeight: '100vh',
      padding: '40px',
      color: '#E3D095',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      position: 'relative'
    }}>
      {isLoading && <div style={modalStyle}>Creating activity...</div>}

      <button
        onClick={() => navigate('/activities')}
        className="hoverable-button"
        style={buttonStyle}
      >
        Back
      </button>

      <h2 style={{ marginBottom: '20px' }}>Create New Activity</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}

      <label>Name:</label>
      <input type="text" value={name} onChange={e => setName(e.target.value)} style={inputStyle} />

      <label>Minimum Duration:</label>
      <input type="number" value={durimin} onChange={e => setDurimin(e.target.value)} style={inputStyle} />

      <label>Maximum Duration:</label>
      <input type="number" value={durimax} onChange={e => setDurimax(e.target.value)} style={inputStyle} />

      <label style={{ marginTop: '10px' }}>
        <input type="checkbox" checked={isInterruptible} onChange={() => setIsInterruptible(!isInterruptible)} />
        {' '}Is Interruptible
      </label>

      {isInterruptible && (
        <>
          <label>Minimum Part Duration:</label>
          <input type="number" value={smin} onChange={e => setSmin(e.target.value)} style={inputStyle} />

          <label>Maximum Part Duration:</label>
          <input type="number" value={smax} onChange={e => setSmax(e.target.value)} style={inputStyle} />

          <label style={{ marginTop: '10px' }}>
            <input type="checkbox" checked={dminNone} onChange={() => {
              const newValue = !dminNone;
              setDminNone(newValue);
              if (newValue) setDmin('0');
            }} />
            {' '}No Minimum Part Distance
          </label>
          <input type="number" value={dmin} onChange={e => setDmin(e.target.value)} disabled={dminNone} style={inputStyle} />

          <label style={{ marginTop: '10px' }}>
            <input type="checkbox" checked={dmaxNone} onChange={() => {
              const newValue = !dmaxNone;
              setDmaxNone(newValue);
              if (newValue) setDmax('999999');
            }} />
            {' '}No Maximum Part Distance
          </label>
          <input type="number" value={dmax} onChange={e => setDmax(e.target.value)} disabled={dmaxNone} style={inputStyle} />
        </>
      )}

      <button
        onClick={handleSave}
        className="hoverable-button"
        style={{ ...buttonStyle, marginTop: '20px' }}
        disabled={isLoading}
      >
        Save
      </button>
    </div>
  );
};

export default CreateActivityIdeaPage;
