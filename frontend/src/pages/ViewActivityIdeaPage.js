import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import API_BASE_URL from '../config';
import { useNavigate } from 'react-router-dom';


const ViewActivityIdeaPage = () => {
    const { authToken, loggedUsername } = useAuth();
    const { id } = useParams();
    const navigate = useNavigate();

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

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
    const [locations, setLocations] = useState([]);
    const [newLocationName, setNewLocationName] = useState('');
    const [newLocationX, setNewLocationX] = useState('');
    const [newLocationY, setNewLocationY] = useState('');
    const [editingLocationId, setEditingLocationId] = useState(null);
    const [editLocationName, setEditLocationName] = useState('');
    const [editLocationX, setEditLocationX] = useState('');
    const [editLocationY, setEditLocationY] = useState('');
    const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
    const [temporalIntervals, setTemporalIntervals] = useState([]);
    const [newStartInterval, setNewStartInterval] = useState('');
    const [newEndInterval, setNewEndInterval] = useState('');
    const [editingIntervalId, setEditingIntervalId] = useState(null);
    const [editStartInterval, setEditStartInterval] = useState('');
    const [editEndInterval, setEditEndInterval] = useState('');
    const [globalLoading, setGlobalLoading] = useState(false);


    const handleDelete = async () => {
    setGlobalLoading(true);
    try {
      
        const res = await fetch(`${API_BASE_URL}/activity-ideas/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${authToken}`
        }
        });

        if (res.ok) {
        navigate('/activities');
        } else {
        alert('Failed to delete activity');
        }
    } catch (error) {
        console.error('Error deleting activity:', error);
        alert('Unexpected error occurred');
    }
    finally {
      setGlobalLoading(false);
    }
    };


    const handleAddLocation = async () => {
    setGlobalLoading(true);
    if (!newLocationName || isNaN(newLocationX) || isNaN(newLocationY)) {
        alert('Please enter valid name, X, and Y values');
        return;
    }

    const parsedX = parseFloat(newLocationX);
    const parsedY = parseFloat(newLocationY);

    const duplicate = locations.find(
        loc =>
        loc.name.trim().toLowerCase() === newLocationName.trim().toLowerCase() &&
        loc.x === parsedX &&
        loc.y === parsedY
    );

    if (duplicate) {
    alert('This location is already mapped to the activity.');
    return;
  }

    try {
        const createRes = await fetch(`${API_BASE_URL}/locations`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${authToken}`
        },
        body: JSON.stringify({
            name: newLocationName,
            x: parseFloat(newLocationX),
            y: parseFloat(newLocationY)
        })
        });

        if (!createRes.ok) {
        alert('Failed to create location');
        return;
        }

        const createdLocation = await createRes.json();

        const mapRes = await fetch(`${API_BASE_URL}/locations/${createdLocation.id}/map-to-activity/${id}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${authToken}`
        }
        });

        if (!mapRes.ok) {
        alert('Failed to map location to activity');
        return;
        }

        setLocations(prev => [...prev, createdLocation]);

        setNewLocationName('');
        setNewLocationX('');
        setNewLocationY('');
    } catch (error) {
        console.error('Error adding location:', error);
        alert('Unexpected error while adding location');
    }
        finally {
      setGlobalLoading(false);
    }
    };

    const hasOverlap = (start, end, skipId = null) => {
    return temporalIntervals.some(interval => {
        if (interval.id === skipId)
            return false;
        const iStart = new Date(interval.startInterval);
        const iEnd = new Date(interval.endInterval);
        return (start < iEnd && end > iStart);
    });
    };

    const handleAddTemporalInterval = async () => {
    setGlobalLoading(true);
    if (!newStartInterval || !newEndInterval) {
        alert("Please provide both start and end times.");
        return;
    }

    const start = new Date(newStartInterval);
    const end = new Date(newEndInterval);

    if (start >= end) {
        alert("Start must be before end.");
        return;
    }

    if (hasOverlap(start, end, -1)) {
        alert("This interval overlaps with an existing one.");
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/temporal-intervals`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${authToken}`
        },
        body: JSON.stringify({
            startInterval: newStartInterval,
            endInterval: newEndInterval,
            activityIdea_id: parseInt(id)
        })
        });

        if (!response.ok) {
        alert('Failed to add temporal interval');
        return;
        }

        const created = await response.json();
        setTemporalIntervals(prev => [...prev, created]);
        setNewStartInterval('');
        setNewEndInterval('');
    } catch (err) {
        console.error(err);
        alert("Unexpected error");
    }
    finally {
      setGlobalLoading(false);
    }
    };

    const handleUpdateTemporalInterval = async (intervalId) => {
    setGlobalLoading(true);
    const start = new Date(editStartInterval);
    const end = new Date(editEndInterval);

    if (start >= end) {
        alert("Start must be before end.");
        return;
    }

    if (hasOverlap(start, end, intervalId)) {
        alert("This interval overlaps with an existing one.");
        return;
    }

    try {
        const res = await fetch(`${API_BASE_URL}/temporal-intervals/${intervalId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${authToken}`
        },
        body: JSON.stringify({
            startInterval: editStartInterval,
            endInterval: editEndInterval,
            activityIdea_id: parseInt(id)
        })
        });

        if (!res.ok) {
        alert('Failed to update interval');
        return;
        }

        const updated = await res.json();
        setTemporalIntervals(prev =>
        prev.map(i => (i.id === intervalId ? updated : i))
        );

        setEditingIntervalId(null);
    } catch (error) {
        console.error(error);
        alert("Unexpected error");
    }
    finally {
      setGlobalLoading(false);
    }
    };


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
    setGlobalLoading(true);
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

    try {
        const response = await fetch(`${API_BASE_URL}/activity-ideas/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${authToken}`
        },
        body: JSON.stringify(dto)
        });

        if (response.ok) {
        navigate('/activities');
        } else {
        alert('Failed to update activity');
        }
    } catch (error) {
        console.error('Error updating activity:', error);
        alert('Unexpected error occurred');
    }
    };

    const handleUpdateLocation = async (locId) => {
    setGlobalLoading(true);

    if (!editLocationName || isNaN(editLocationX) || isNaN(editLocationY)) {
        alert('Invalid update values');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/locations/${locId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${authToken}`
        },
        body: JSON.stringify({
            name: editLocationName,
            x: parseFloat(editLocationX),
            y: parseFloat(editLocationY)
        })
        });

        if (!response.ok) {
        alert('Failed to update location');
        return;
        }

        const updatedLoc = await response.json();

        setLocations(prev =>
        prev.map(loc => (loc.id === locId ? updatedLoc : loc))
        );

        setEditingLocationId(null);
    } catch (error) {
        console.error('Error updating location:', error);
        alert('Unexpected error occurred');
    }
    finally {
      setGlobalLoading(false);
    }
    };

    const handleDeleteLocation = async (locId) => {
    setGlobalLoading(true);

    try {
        const res = await fetch(`${API_BASE_URL}/locations/${locId}/unmap-to-activity/${id}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${authToken}` }
        });

        if (!res.ok) {
        alert('Failed to unmap location from activity');
        return;
        }

        setLocations(prev => prev.filter(loc => loc.id !== locId));
    } catch (err) {
        console.error(err);
        alert('Unexpected error unmapping location');
    }
    };

    const handleDeleteTemporalInterval = async (intervalId) => {
    setGlobalLoading(true);

    try {
        const res = await fetch(`${API_BASE_URL}/temporal-intervals/${intervalId}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${authToken}` }
        });

        if (!res.ok) {
        alert('Failed to delete interval');
        return;
        }

        setTemporalIntervals(prev => prev.filter(i => i.id !== intervalId));
    } catch (err) {
        console.error(err);
        alert('Unexpected error deleting interval');
    }
    finally {
      setGlobalLoading(false);
    }
};

    useEffect(() => {
        const fetchActivity = async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/activity-ideas/${id}`, {
            headers: {
                Authorization: `Bearer ${authToken}`
            }
            });

            if (!response.ok) throw new Error('Failed to fetch activity');

            const data = await response.json();

            setName(data.name);
            setDurimin(data.durimin);
            setDurimax(data.durimax);

            const interruptible = data.smin !== null || data.smax !== null || data.dmin !== null || data.dmax !== null;
            setIsInterruptible(interruptible);

            if (interruptible) {
            setSmin(data.smin ?? '');
            setSmax(data.smax ?? '');
            setDmin(data.dmin ?? '');
            setDmax(data.dmax ?? '');

            if (data.dmin === 0) setDminNone(true);
            if (data.dmax === 999999) setDmaxNone(true);
            }

            setLocations(data.locations || []);
            setTemporalIntervals(data.temporalIntervals || []);

            setLoading(false);
        } catch (err) {
            setError(err.message);
            setLoading(false);
        }
        };

        fetchActivity();
    }, [id, authToken]);


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
      backgroundColor: '#0E2148',
      color: '#E3D095',
      border: '2px solid #E3D095',
      borderRadius: '8px',
      padding: '10px 20px',
      cursor: 'pointer',
      transition: 'all 0.3s ease',
      width: '100px',
      alignSelf: 'center',
      margin: '10px'
    };

  const styleSheet = document.styleSheets[0];
  styleSheet.insertRule(`
    .hoverable-button:hover {
      background-color: #E3D095 !important;
      color: #0E2148 !important;
    }
  `, styleSheet.cssRules.length);

    if (loading) return  <div style={{
    background: 'linear-gradient(to bottom right, #0E2148, #483AA0)',
    minHeight: '100vh',
    padding: '40px',
    color: '#E3D095',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center'
  }}><p>Loading...</p></div> ;
    return (
  <div style={{
    background: 'linear-gradient(to bottom right, #0E2148, #483AA0)',
    minHeight: '100vh',
    padding: '40px',
    color: '#E3D095',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center'
  }}>
    <h2>Edit Activity</h2>
    {globalLoading && (
      <div className="modal">
        <p>Loading...</p>
      </div>
    )}
    {error && <p style={{ color: 'red' }}>{error}</p>}

    <label>Name:</label>
    <input type="text" value={name} onChange={e => setName(e.target.value)} style={inputStyle} />

    <label>Minimum Duration:</label>
    <input type="number" value={durimin} onChange={e => setDurimin(e.target.value)} style={inputStyle} />

    <label>Maximum Duration:</label>
    <input type="number" value={durimax} onChange={e => setDurimax(e.target.value)} style={inputStyle} />

    <label>
      <input type="checkbox" checked={isInterruptible} onChange={() => setIsInterruptible(!isInterruptible)} />
      Is Interruptible
    </label>

    {isInterruptible && (
      <>
        <label>Minimum Part Duration:</label>
        <input type="number" value={smin} onChange={e => setSmin(e.target.value)} style={inputStyle} />

        <label>Maximum Part Duration:</label>
        <input type="number" value={smax} onChange={e => setSmax(e.target.value)} style={inputStyle} />

        <label>
          <input
            type="checkbox"
            checked={dminNone}
            onChange={() => {
              const newVal = !dminNone;
              setDminNone(newVal);
              if (newVal) setDmin('0');
            }}
          />
          No Minimum Part Distance
        </label>
        <input type="number" value={dmin} onChange={e => setDmin(e.target.value)} disabled={dminNone} style={inputStyle} />

        <label>
          <input
            type="checkbox"
            checked={dmaxNone}
            onChange={() => {
              const newVal = !dmaxNone;
              setDmaxNone(newVal);
              if (newVal) setDmax('999999');
            }}
          />
          No Maximum Part Distance
        </label>
        <input type="number" value={dmax} onChange={e => setDmax(e.target.value)} disabled={dmaxNone} style={inputStyle} />
      </>
    )}

    <h3>Locations:</h3>
    <table style={{ border: '1px solid #E3D095', color: '#E3D095', marginBottom: '20px' }}>
      <thead>
        <tr>
          <th>Name</th><th>X</th><th>Y</th><th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {locations.length > 0 ? locations.map(loc => (
          <tr key={loc.id}>
            {editingLocationId === loc.id ? (
              <>
                <td><input value={editLocationName} onChange={e => setEditLocationName(e.target.value)} style={inputStyle} /></td>
                <td><input type="number" value={editLocationX} onChange={e => setEditLocationX(e.target.value)} style={inputStyle} /></td>
                <td><input type="number" value={editLocationY} onChange={e => setEditLocationY(e.target.value)} style={inputStyle} /></td>
                <td>
                  <button onClick={() => handleUpdateLocation(loc.id)} style={buttonStyle}>Save</button>
                  <button onClick={() => setEditingLocationId(null)} style={buttonStyle}>Cancel</button>
                </td>
              </>
            ) : (
              <>
                <td>{loc.name}</td>
                <td>{loc.x}</td>
                <td>{loc.y}</td>
                <td>
                  <button onClick={() => {
                    setEditingLocationId(loc.id);
                    setEditLocationName(loc.name);
                    setEditLocationX(loc.x);
                    setEditLocationY(loc.y);
                  }} style={buttonStyle}>Edit</button>
                  <button onClick={() => handleDeleteLocation(loc.id)} style={{ ...buttonStyle, color: 'red' }}>Delete</button>
                </td>
              </>
            )}
          </tr>
        )) : <tr><td colSpan="4">No locations available</td></tr>}
      </tbody>
    </table>

    <h4>Add New Location</h4>
    <input type="text" value={newLocationName} onChange={e => setNewLocationName(e.target.value)} placeholder="Name" style={inputStyle} />
    <input type="number" value={newLocationX} onChange={e => setNewLocationX(e.target.value)} placeholder="X" style={inputStyle} />
    <input type="number" value={newLocationY} onChange={e => setNewLocationY(e.target.value)} placeholder="Y" style={inputStyle} />
    <button onClick={handleAddLocation} style={buttonStyle}>Add Location</button>

    <h3>Temporal Intervals:</h3>
    <table style={{ border: '1px solid #E3D095', color: '#E3D095', marginBottom: '20px' }}>
      <thead>
        <tr>
          <th>Start</th><th>End</th><th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {temporalIntervals.length > 0 ? temporalIntervals.map(interval => (
          <tr key={interval.id}>
            {editingIntervalId === interval.id ? (
              <>
                <td><input type="datetime-local" value={editStartInterval} onChange={e => setEditStartInterval(e.target.value)} style={inputStyle} /></td>
                <td><input type="datetime-local" value={editEndInterval} onChange={e => setEditEndInterval(e.target.value)} style={inputStyle} /></td>
                <td>
                  <button onClick={() => handleUpdateTemporalInterval(interval.id)} style={buttonStyle}>Save</button>
                  <button onClick={() => setEditingIntervalId(null)} style={buttonStyle}>Cancel</button>
                </td>
              </>
            ) : (
              <>
                <td>{new Date(interval.startInterval).toLocaleString()}</td>
                <td>{new Date(interval.endInterval).toLocaleString()}</td>
                <td>
                  <button onClick={() => {
                    setEditingIntervalId(interval.id);
                    setEditStartInterval(interval.startInterval.slice(0, 16));
                    setEditEndInterval(interval.endInterval.slice(0, 16));
                  }} style={buttonStyle}>Edit</button>
                  <button onClick={() => handleDeleteTemporalInterval(interval.id)} style={{ ...buttonStyle, color: 'red' }}>Delete</button>
                </td>
              </>
            )}
          </tr>
        )) : <tr><td colSpan="3">No intervals available</td></tr>}
      </tbody>
    </table>

    <h4>Add New Temporal Interval</h4>
    <input type="datetime-local" value={newStartInterval} onChange={e => setNewStartInterval(e.target.value)} style={inputStyle} />
    <input type="datetime-local" value={newEndInterval} onChange={e => setNewEndInterval(e.target.value)} style={inputStyle} />
    <button onClick={handleAddTemporalInterval} style={buttonStyle}>Add Temporal Interval</button>

    <button onClick={() => setShowDeleteConfirm(true)} style={{ ...buttonStyle, color: 'red' }}>Delete</button>
    <button onClick={handleSave} style={buttonStyle}>Save</button>

    {showDeleteConfirm && (
      <div style={{ border: '1px solid red', padding: '1em', marginTop: '1em' }}>
        <p>Are you sure you want to delete this activity idea?</p>
        <button onClick={handleDelete} style={buttonStyle}>Yes, delete it</button>
        <button onClick={() => setShowDeleteConfirm(false)} style={buttonStyle}>Cancel</button>
      </div>
    )}
  </div>
);
};


export default ViewActivityIdeaPage;
