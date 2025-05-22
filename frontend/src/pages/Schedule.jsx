import React, { act, useState } from 'react';
import axios from 'axios';

function Schedule() {
  const [activities, setActivities] = useState([]);
  const [newActivity, setNewActivity] = useState({
    name: '',
    duration: '',
    minPartDuration: '',
    maxPartDuration: '',
    intervals: [{ startTime: '', endTime: '' }],
    locations: [{ locationName: '' }],
  });
  const [result, setResult] = useState([]);

  const handleChange = (e) => {
    setNewActivity({ ...newActivity, [e.target.name]: e.target.value });
  };

  const handleIntervalChange = (index, field, value) => {
    const newIntervals = [...newActivity.intervals];
    newIntervals[index][field] = value;
    setNewActivity({ ...newActivity, intervals: newIntervals });
  };

  const handleLocationChange = (value) => {
    setNewActivity({ ...newActivity, locations: [{ locationName: value }] });
  };

  const addIntervalField = () => {
    setNewActivity({
      ...newActivity,
      intervals: [...newActivity.intervals, { startTime: '', endTime: '' }],
    });
  };

  const handleAddActivity = () => {
    const activity = {
      id: activities.length + 1,
      name: newActivity.name,
      duration: parseInt(newActivity.duration),
      minPartDuration: parseInt(newActivity.minPartDuration),
      maxPartDuration: parseInt(newActivity.maxPartDuration),
      minGapBetweenParts: 0,
      maxGapBetweenParts: 0,
      intervals: newActivity.intervals,
      locations: newActivity.locations,
    };
    setActivities([...activities, activity]);
    setNewActivity({
      name: '',
      duration: '',
      minPartDuration: '',
      maxPartDuration: '',
      intervals: [{ startTime: '', endTime: '' }],
      locations: [{ locationName: '' }],
    });
  };

  const handleFinish = async () => {
    try {
        const token = localStorage.getItem('token');
        console.log(token);
        console.log(activities);
        const response = await axios.post(
        'http://localhost:8080/schedule',
        activities,
        {
            headers: {
            Authorization: token
            }
        }
        );
        console.log("Axios response:", response);

        console.log("Done");
        console.log(response.data);
        setResult(response.data);
    } catch (error) {
      alert('Failed to schedule activities');
    }
  };

  return (
    <div>
      <h2>Add Activity</h2>
      <input name="name" placeholder="Name" value={newActivity.name} onChange={handleChange} />
      <input name="duration" placeholder="Duration" value={newActivity.duration} onChange={handleChange} />
      <input name="minPartDuration" placeholder="Min Part Duration" value={newActivity.minPartDuration} onChange={handleChange} />
      <input name="maxPartDuration" placeholder="Max Part Duration" value={newActivity.maxPartDuration} onChange={handleChange} />

      <h4>Intervals</h4>
      {newActivity.intervals.map((interval, index) => (
        <div key={index}>
          <input
            placeholder="Start Time"
            value={interval.startTime}
            onChange={(e) => handleIntervalChange(index, 'startTime', e.target.value)}
          />
          <input
            placeholder="End Time"
            value={interval.endTime}
            onChange={(e) => handleIntervalChange(index, 'endTime', e.target.value)}
          />
        </div>
      ))}
      <button onClick={addIntervalField}>+ Add Interval</button>

      <h4>Location</h4>
      <input
        placeholder="Location Name"
        value={newActivity.locations[0].locationName}
        onChange={(e) => handleLocationChange(e.target.value)}
      />

      <button onClick={handleAddActivity}>Add Activity to List</button>

      <h3>Activities:</h3>
      <ul>
        {activities.map((act, i) => (
          <li key={i}>{act.name} ({act.duration} min)</li>
        ))}
      </ul>

      <button onClick={handleFinish}>Finish and Schedule</button>

      <h3>Schedule Result:</h3>
{result.length > 0 ? (
  <table border="1" cellPadding="8" style={{ borderCollapse: 'collapse', marginTop: '10px' }}>
    <thead>
      <tr>
        <th>Activity</th>
        <th>Start Time</th>
        <th>End Time</th>
        <th>Duration</th>
        <th>Location</th>
      </tr>
    </thead>
    <tbody>
      {result.map((r, index) => (
        <tr key={index}>
          <td>{r.activityName}</td>
          <td>{r.startTime}</td>
          <td>{r.endTime}</td>
          <td>{r.duration} min</td>
          <td>{r.location}</td>
        </tr>
      ))}
    </tbody>
  </table>
) : (
  <p>No schedule results yet.</p>
)}
    </div>
  );
}

export default Schedule;