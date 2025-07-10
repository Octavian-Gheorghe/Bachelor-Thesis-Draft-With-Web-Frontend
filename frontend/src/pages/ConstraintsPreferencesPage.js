import React, { useEffect, useState } from 'react';
import API_BASE_URL from '../config';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';


const ConstraintsPreferencesPage = () => {
    const navigate = useNavigate();
    const { authToken, loggedUsername } = useAuth();
    const [activities, setActivities] = useState([]);
    const [constraints, setConstraints] = useState([]);
    const [orderingConstraints, setOrderingConstraints] = useState([]);
    const [activitiesDistanceConstraints, setActivitiesDistanceConstraints] = useState([]);
    const [activitiesDistancePrefs, setActivitiesDistancePrefs] = useState([]);
    const [activityPartDistancePrefs, setActivityPartDistancePrefs] = useState([]);
    const [orderingPrefs, setOrderingPrefs] = useState([]);
    const [activityDurationPrefs, setActivityDurationPrefs] = useState([]);
    const [activityScheduleTimePrefs, setActivityScheduleTimePrefs] = useState([]);
    const [implicationPrefs, setImplicationPrefs] = useState([]);

    const [editId, setEditId] = useState(null);
    const [editPrefId, setEditPrefId] = useState(null);
    const [editAPrefId, setEditAPrefId] = useState(null);
    const [editOrderingId, setEditOrderingId] = useState(null);
    const [editOrderingPrefId, setEditOrderingPrefId] = useState(null);
    const [editDurPrefId, setEditDurPrefId] = useState(null);
    const [editSchedulePrefId, setEditSchedulePrefId] = useState(null);
    const [editImplicationPrefId, setEditImplicationPrefId] = useState(null);

    const [activitiesDistanceForm, setActivitiesDistanceForm] = useState({
    id: null,
    activityIdea1_id: '',
    activityIdea2_id: '',
    distance: '',
    type: 'MINIMUM'
    });

    const [formData, setFormData] = useState({
        activityIdea_id: '',
        distance: '',
        type: 'MINIMUM'
    });

    const [activitiesDistancePrefForm, setActivitiesDistancePrefForm] = useState({
    id: null,
    activityIdea1_id: '',
    activityIdea2_id: '',
    distance: '',
    type: 'MINIMUM'
  });


    const [orderingForm, setOrderingForm] = useState({
        activityIdeaBefore_id: '',
        activityIdeaAfter_id: ''
    });

    const [activityPartDistancePrefForm, setActivityPartDistancePrefForm] = useState({
    id: null,
    activityIdea_id: '',
    distance: '',
    type: 'MINIMUM'
  });


    const [orderingPrefForm, setOrderingPrefForm] = useState({
    activityIdeaBefore_id: '',
    activityIdeaAfter_id: ''
  });


  const [activityDurationPrefForm, setActivityDurationPrefForm] = useState({
    id: null,
    activityIdea_id: '',
    minimumDuration: '',
    maximumDuration: ''
  });

   const [activityScheduleTimePrefForm, setActivityScheduleTimePrefForm] = useState({
    id: null,
    activityIdea_id: '',
    timeOfAnalysis: '',
    type: 'EARLIER'
  });

  const [implicationPrefForm, setImplicationPrefForm] = useState({
    id: null,
    activityIdeaInitial_id: '',
    activityIdeaImplied_id: ''
  });



    const fetchActivities = async () => {
    if (!authToken || !loggedUsername) return;

    try {
      const res = await fetch(`${API_BASE_URL}/activity-ideas/for/${loggedUsername}`, {
        headers: {
          'Authorization': `Bearer ${authToken}`
        }
      });
      if (!res.ok) throw new Error('Failed to fetch activities');
      const data = await res.json();
      console.log('Fetched activity data:', data);

      setActivities(data);

      const allConstraints = data.flatMap(act =>
        act.activityPartDistanceConstraints.map(constraint => ({
          ...constraint,
          activityId: act.id,
          activityName: act.name
        }))
      );
      setConstraints(allConstraints);

      const allOrderingConstraints = [];
      data.forEach(before => {
        before.orderingConstraintsBefore.forEach(c => {
          const after = data.find(a => a.orderingConstraintsAfter.some(aft => aft.id === c.id));
          if (after) {
            allOrderingConstraints.push({
              id: c.id,
              beforeId: before.id,
              beforeName: before.name,
              afterId: after.id,
              afterName: after.name
            });
          }
        });
      });
      setOrderingConstraints(allOrderingConstraints);

      const allDistConstraints = data.flatMap(act =>
        act.activitiesDistanceConstraintsAsFirst.map(dc => {
          const second = data.find(a => a.activitiesDistanceConstraintsAsSecond.some(d => d.id === dc.id));
          return second ? {
            id: dc.id,
            activity1Id: act.id,
            activity1Name: act.name,
            activity2Id: second.id,
            activity2Name: second.name,
            distance: dc.distance,
            type: dc.type
          } : null;
        }).filter(Boolean)
      );
      setActivitiesDistanceConstraints(allDistConstraints);

      const allDistPrefs = data.flatMap(act =>
        act.activitiesDistancePreferencesAsFirst.map(dp => {
          const second = data.find(a => a.activitiesDistancePreferencesAsSecond.some(d => d.id === dp.id));
          return second ? {
            id: dp.id,
            activity1Id: act.id,
            activity1Name: act.name,
            activity2Id: second.id,
            activity2Name: second.name,
            distance: dp.distance,
            type: dp.type
          } : null;
        }).filter(Boolean)
      );
      setActivitiesDistancePrefs(allDistPrefs);

      const allPartPrefs = data.flatMap(act =>
        act.activityPartDistancePreferences.map(p => ({
          ...p,
          activityId: act.id,
          activityName: act.name
        }))
      );
      setActivityPartDistancePrefs(allPartPrefs);

      const allOrderingPrefs = [];
      data.forEach(before => {
        before.orderingPreferencesBefore.forEach(p => {
          const after = data.find(a => a.orderingPreferencesAfter.some(aft => aft.id === p.id));
          if (after) {
            allOrderingPrefs.push({
              id: p.id,
              beforeId: before.id,
              beforeName: before.name,
              afterId: after.id,
              afterName: after.name
            });
          }
        });
      });
      setOrderingPrefs(allOrderingPrefs);

      const allDurationPrefs = data.flatMap(act =>
        act.activityDurationPreferences.map(p => ({
          ...p,
          activityId: act.id,
          activityName: act.name
        }))
      );
      setActivityDurationPrefs(allDurationPrefs);

      const allScheduleTimePrefs = data.flatMap(act =>
        act.activityScheduleTimePreferences.map(p => ({
          ...p,
          activityId: act.id,
          activityName: act.name
        }))
      );
      setActivityScheduleTimePrefs(allScheduleTimePrefs);

      const allImplicationPrefs = data.flatMap(act =>
        act.implicationPreferencesAsInitial.map(p => ({
          ...p,
          initialId: act.id,
          initialName: act.name,
          implied: data.find(a => a.id === p.activityIdeaImplied_id)
        }))
      );
      setImplicationPrefs(allImplicationPrefs);

    } catch (err) {
      console.error('Error fetching data:', err);
    }
  };

  useEffect(() => {
    fetchActivities();
  }, [authToken, loggedUsername]);

  const handleSubmit = async () => {
    const { activityIdea_id, distance, type } = formData;
    if (!activityIdea_id || !distance || isNaN(distance)) return alert('Fill out all fields properly');

    const method = editId ? 'PUT' : 'POST';
    const url = editId ? `${API_BASE_URL}/activity-part-distance-constraints/${editId}` : `${API_BASE_URL}/activity-part-distance-constraints`;

    try {
      const res = await fetch(url, {
        method,
        headers: {
          'Authorization': `Bearer ${authToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          activityIdea_id: parseInt(activityIdea_id),
          distance: parseInt(distance),
          type
        })
      });

      if (!res.ok) throw new Error('Failed to save constraint');

      await fetchActivities();
      setFormData({ activityIdea_id: '', distance: '', type: 'MINIMUM' });
      setEditId(null);
    } catch (err) {
      console.error('Error saving constraint:', err);
    }
  };

  const handleDelete = async (id) => {
    try {
      await fetch(`${API_BASE_URL}/activity-part-distance-constraints/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${authToken}` }
      });
      await fetchActivities();
    } catch (err) {
      console.error('Error deleting constraint:', err);
    }
  };

  const handleEdit = (constraint) => {
    setFormData({
      activityIdea_id: constraint.activityId,
      distance: constraint.distance,
      type: constraint.type
    });
    setEditId(constraint.id);
  };

  const handleOrderingSubmit = async () => {
    const { activityIdeaBefore_id, activityIdeaAfter_id } = orderingForm;
    if (!activityIdeaBefore_id || !activityIdeaAfter_id) return alert('Select both activities');

    const method = editOrderingId ? 'PUT' : 'POST';
    const url = editOrderingId ? `${API_BASE_URL}/ordering-constraints/${editOrderingId}` : `${API_BASE_URL}/ordering-constraints`;

    try {
      const res = await fetch(url, {
        method,
        headers: {
          'Authorization': `Bearer ${authToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          id: editOrderingId,
          activityIdeaBefore_id: parseInt(activityIdeaBefore_id),
          activityIdeaAfter_id: parseInt(activityIdeaAfter_id)
        })
      });

      if (!res.ok) throw new Error('Failed to save ordering constraint');
      await fetchActivities();
      setOrderingForm({ activityIdeaBefore_id: '', activityIdeaAfter_id: '' });
      setEditOrderingId(null);
    } catch (err) {
      console.error('Error saving ordering constraint:', err);
    }
  };

  const handleDeleteOrdering = async (id) => {
    try {
      await fetch(`${API_BASE_URL}/ordering-constraints/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${authToken}` }
      });
      await fetchActivities();
    } catch (err) {
      console.error('Error deleting ordering constraint:', err);
    }
  };

  const handleEditOrdering = (oc) => {
    setOrderingForm({
      activityIdeaBefore_id: oc.beforeId.toString(),
      activityIdeaAfter_id: oc.afterId.toString()
    });
    setEditOrderingId(oc.id);
  };

    const handleImplicationPrefSubmit = async () => {
    const { activityIdeaInitial_id, activityIdeaImplied_id, id } = implicationPrefForm;
    if (!activityIdeaInitial_id || !activityIdeaImplied_id) return alert('All fields required');

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE_URL}/implication-preferences/${id}` : `${API_BASE_URL}/implication-preferences`;

    try {
      const res = await fetch(url, {
        method,
        headers: {
          'Authorization': `Bearer ${authToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          activityIdeaInitial_id: parseInt(activityIdeaInitial_id),
          activityIdeaImplied_id: parseInt(activityIdeaImplied_id)
        })
      });
      if (!res.ok) throw new Error('Failed to save');
      await fetchActivities();
      setImplicationPrefForm({ id: null, activityIdeaInitial_id: '', activityIdeaImplied_id: '' });
      setEditImplicationPrefId(null);
    } catch (err) {
      console.error('Error saving implication preference:', err);
    }
  };

  const handleImplicationPrefDelete = async (id) => {
    try {
      await fetch(`${API_BASE_URL}/implication-preferences/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${authToken}` }
      });
      await fetchActivities();
    } catch (err) {
      console.error('Error deleting implication preference:', err);
    }
  };

  const handleImplicationPrefEdit = (p) => {
    setImplicationPrefForm({
      id: p.id,
      activityIdeaInitial_id: p.initialId,
      activityIdeaImplied_id: p.activityIdeaImplied_id
    });
    setEditImplicationPrefId(p.id);
  };


  const handleActivitiesDistanceSubmit = async () => {
    const { activityIdea1_id, activityIdea2_id, distance, type, id } = activitiesDistanceForm;
    if (!activityIdea1_id || !activityIdea2_id || !distance || isNaN(distance)) return alert('All fields are required');

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE_URL}/activities-distance-constraints/${id}` : `${API_BASE_URL}/activities-distance-constraints`;

    try {
      const res = await fetch(url, {
        method,
        headers: {
          'Authorization': `Bearer ${authToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          activityIdea1_id: parseInt(activityIdea1_id),
          activityIdea2_id: parseInt(activityIdea2_id),
          distance: parseInt(distance),
          type
        })
      });

      if (!res.ok) throw new Error('Failed to save activities distance constraint');
      await fetchActivities();
      setActivitiesDistanceForm({ id: null, activityIdea1_id: '', activityIdea2_id: '', distance: '', type: 'MINIMUM' });
    } catch (err) {
      console.error('Error saving:', err);
    }
  };

  const handleActivitiesDistanceDelete = async (id) => {
    try {
      await fetch(`${API_BASE_URL}/activities-distance-constraints/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${authToken}` }
      });
      await fetchActivities();
    } catch (err) {
      console.error('Error deleting:', err);
    }
  };

  const handleActivitiesDistanceEdit = (c) => {
    setActivitiesDistanceForm({
      id: c.id,
      activityIdea1_id: c.activity1Id,
      activityIdea2_id: c.activity2Id,
      distance: c.distance,
      type: c.type
    });
  };

    const handleActivitiesDistancePrefSubmit = async () => {
    const { activityIdea1_id, activityIdea2_id, distance, type, id } = activitiesDistancePrefForm;
    if (!activityIdea1_id || !activityIdea2_id || !distance || isNaN(distance)) return alert('All fields are required');

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE_URL}/activities-distance-preferences/${id}` : `${API_BASE_URL}/activities-distance-preferences`;

    try {
      const res = await fetch(url, {
        method,
        headers: {
          'Authorization': `Bearer ${authToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          activityIdea1_id: parseInt(activityIdea1_id),
          activityIdea2_id: parseInt(activityIdea2_id),
          distance: parseInt(distance),
          type
        })
      });

      if (!res.ok) throw new Error('Failed to save preference');
      await fetchActivities();
      setActivitiesDistancePrefForm({ id: null, activityIdea1_id: '', activityIdea2_id: '', distance: '', type: 'MINIMUM' });
      setEditPrefId(null);
    } catch (err) {
      console.error('Error saving preference:', err);
    }
  };

  const handleActivitiesDistancePrefDelete = async (id) => {
    try {
      await fetch(`${API_BASE_URL}/activities-distance-preferences/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${authToken}` }
      });
      await fetchActivities();
    } catch (err) {
      console.error('Error deleting:', err);
    }
  };

  const handleActivitiesDistancePrefEdit = (p) => {
    setActivitiesDistancePrefForm({
      id: p.id,
      activityIdea1_id: p.activity1Id,
      activityIdea2_id: p.activity2Id,
      distance: p.distance,
      type: p.type
    });
    setEditPrefId(p.id);
  };

  const handleOrderingPrefSubmit = async () => {
    const { activityIdeaBefore_id, activityIdeaAfter_id } = orderingPrefForm;
    if (!activityIdeaBefore_id || !activityIdeaAfter_id) return alert('Select both activities');

    const method = editOrderingPrefId ? 'PUT' : 'POST';
    const url = editOrderingPrefId ? `${API_BASE_URL}/ordering-preferences/${editOrderingPrefId}` : `${API_BASE_URL}/ordering-preferences`;

    try {
      const res = await fetch(url, {
        method,
        headers: {
          'Authorization': `Bearer ${authToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          activityIdeaBefore_id: parseInt(activityIdeaBefore_id),
          activityIdeaAfter_id: parseInt(activityIdeaAfter_id)
        })
      });

      if (!res.ok) throw new Error('Failed to save ordering preference');
      await fetchActivities();
      setOrderingPrefForm({ activityIdeaBefore_id: '', activityIdeaAfter_id: '' });
      setEditOrderingPrefId(null);
    } catch (err) {
      console.error('Error saving ordering preference:', err);
    }
  };

  const handleOrderingPrefDelete = async (id) => {
    try {
      await fetch(`${API_BASE_URL}/ordering-preferences/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${authToken}` }
      });
      await fetchActivities();
    } catch (err) {
      console.error('Error deleting ordering preference:', err);
    }
  };

  const handleOrderingPrefEdit = (p) => {
    setOrderingPrefForm({
      activityIdeaBefore_id: p.beforeId.toString(),
      activityIdeaAfter_id: p.afterId.toString()
    });
    setEditOrderingPrefId(p.id);
  };

   const handleActivityPartDistancePrefSubmit = async () => {
    const { activityIdea_id, distance, type, id } = activityPartDistancePrefForm;
    if (!activityIdea_id || !distance || isNaN(distance)) return alert('All fields are required');

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE_URL}/activity-part-distance-preferences/${id}` : `${API_BASE_URL}/activity-part-distance-preferences`;

    try {
      const res = await fetch(url, {
        method,
        headers: {
          'Authorization': `Bearer ${authToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          activityIdea_id: parseInt(activityIdea_id),
          distance: parseInt(distance),
          type
        })
      });

      if (!res.ok) throw new Error('Failed to save preference');
      await fetchActivities();
      setActivityPartDistancePrefForm({ id: null, activityIdea_id: '', distance: '', type: 'MINIMUM' });
      setEditAPrefId(null);
    } catch (err) {
      console.error('Error saving preference:', err);
    }
  };

  const handleActivityPartDistancePrefDelete = async (id) => {
    try {
      await fetch(`${API_BASE_URL}/activity-part-distance-preferences/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${authToken}` }
      });
      await fetchActivities();
    } catch (err) {
      console.error('Error deleting:', err);
    }
  };

  const handleActivityPartDistancePrefEdit = (p) => {
    setActivityPartDistancePrefForm({
      id: p.id,
      activityIdea_id: p.activityId,
      distance: p.distance,
      type: p.type
    });
    setEditAPrefId(p.id);
  };


  const handleActivityDurationPrefSubmit = async () => {
    const { activityIdea_id, minimumDuration, maximumDuration, id } = activityDurationPrefForm;
    if (!activityIdea_id || isNaN(minimumDuration) || isNaN(maximumDuration)) return alert('All fields are required');

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE_URL}/activity-duration-preferences/${id}` : `${API_BASE_URL}/activity-duration-preferences`;

    try {
      const res = await fetch(url, {
        method,
        headers: {
          'Authorization': `Bearer ${authToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          activityIdea_id: parseInt(activityIdea_id),
          minimumDuration: parseInt(minimumDuration),
          maximumDuration: parseInt(maximumDuration)
        })
      });

      if (!res.ok) throw new Error('Failed to save duration preference');
      await fetchActivities();
      setActivityDurationPrefForm({ id: null, activityIdea_id: '', minimumDuration: '', maximumDuration: '' });
      setEditDurPrefId(null);
    } catch (err) {
      console.error('Error saving duration preference:', err);
    }
  };

  const handleActivityDurationPrefDelete = async (id) => {
    try {
      await fetch(`${API_BASE_URL}/activity-duration-preferences/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${authToken}` }
      });
      await fetchActivities();
    } catch (err) {
      console.error('Error deleting:', err);
    }
  };

  const handleActivityDurationPrefEdit = (p) => {
    setActivityDurationPrefForm({
      id: p.id,
      activityIdea_id: p.activityId,
      minimumDuration: p.minimumDuration,
      maximumDuration: p.maximumDuration
    });
    setEditDurPrefId(p.id);
  };

  const handleActivityScheduleTimePrefSubmit = async () => {
    const { activityIdea_id, timeOfAnalysis, type, id } = activityScheduleTimePrefForm;
    if (!activityIdea_id || !timeOfAnalysis) return alert('All fields are required');

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE_URL}/activity-schedule-time-preferences/${id}` : `${API_BASE_URL}/activity-schedule-time-preferences`;

    try {
      const res = await fetch(url, {
        method,
        headers: {
          'Authorization': `Bearer ${authToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          activityIdea_id: parseInt(activityIdea_id),
          timeOfAnalysis,
          type
        })
      });

      if (!res.ok) throw new Error('Failed to save preference');
      await fetchActivities();
      setActivityScheduleTimePrefForm({ id: null, activityIdea_id: '', timeOfAnalysis: '', type: 'EARLIER' });
      setEditSchedulePrefId(null);
    } catch (err) {
      console.error('Error saving preference:', err);
    }
  };

  const handleActivityScheduleTimePrefDelete = async (id) => {
    try {
      await fetch(`${API_BASE_URL}/activity-schedule-time-preferences/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${authToken}` }
      });
      await fetchActivities();
    } catch (err) {
      console.error('Error deleting:', err);
    }
  };

  const handleActivityScheduleTimePrefEdit = (p) => {
    setActivityScheduleTimePrefForm({
      id: p.id,
      activityIdea_id: p.activityId,
      timeOfAnalysis: p.timeOfAnalysis,
      type: p.type
    });
    setEditSchedulePrefId(p.id);
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

const inputStyle = {
  border: '1px solid #e3d095',
  backgroundColor: '#0E2148',
  color: '#e3d095',
  padding: '0.4rem',
  margin: '0.2rem 0',
  borderRadius: '6px'
};

const formStyle = {
  border: '1px solid #e3d095',
  backgroundColor: '#0E2148',
  padding: '1rem',
  borderRadius: '10px',
  maxWidth: '500px',
  margin: '1rem auto',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  color: '#e3d095'
};

const sectionStyle = {
  marginBottom: '2rem'
};
  

  return (
    <div style={{
    background: 'linear-gradient(to right, #0E2148, #483AA0)',
    padding: '2rem',
    color: '#e3d095'
  }}>
    <button style={buttonStyle} onClick={(e) => { e.preventDefault(); navigate('/activities')}}>Back</button>
    <h2 style={{ textAlign: 'center' }}>Constraints and Preferences</h2>

    <section style={sectionStyle}>
      <h3>Activity Part Distance Constraints</h3>
      <ul>
        {constraints.map(c => (
          <li key={c.id}>
            Activity ID: {c.activityId} - Name: {c.activityName} - Type: {c.type} - Distance: {c.distance} minutes
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleEdit(c)}}>Edit</button>
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleDelete(c.id)}}>Delete</button>
          </li>
        ))}
      </ul>

      <form style={formStyle}>
        <h3>{editId ? 'Edit Constraint' : 'Add Constraint'}</h3>
        <label>Activity:</label>
        <select style={inputStyle} value={formData.activityIdea_id} onChange={e => setFormData({ ...formData, activityIdea_id: e.target.value })}>
          <option value=''>Select activity</option>
          {activities.map(act => (
            <option key={act.id} value={act.id}>{act.name} (ID: {act.id})</option>
          ))}
        </select>
        <label>Distance Type:</label>
        <select style={inputStyle} value={formData.type} onChange={e => setFormData({ ...formData, type: e.target.value })}>
          <option value='MINIMUM'>MINIMUM</option>
          <option value='MAXIMUM'>MAXIMUM</option>
        </select>
        <label>Distance (in minutes):</label>
        <input style={inputStyle} type='number' value={formData.distance} onChange={e => setFormData({ ...formData, distance: e.target.value })} />
        <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleSubmit()}}>{editId ? 'Save Changes' : 'Add Constraint'}</button>
      </form>
    </section>

    <section style={sectionStyle}>
      <h3>Ordering Constraints</h3>
      <ul>
        {orderingConstraints.map(oc => (
          <li key={oc.id}>
            {oc.beforeId} - {oc.beforeName} → {oc.afterId} - {oc.afterName}
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleEditOrdering(oc)}}>Edit</button>
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleDeleteOrdering(oc.id)}}>Delete</button>
          </li>
        ))}
      </ul>

      <form style={formStyle}>
        <h3>{editOrderingId ? 'Edit Constraint' : 'Add Constraint'}</h3>
        <label>Before Activity:</label>
        <select style={inputStyle} value={orderingForm.activityIdeaBefore_id} onChange={e => setOrderingForm({ ...orderingForm, activityIdeaBefore_id: e.target.value })}>
          <option value=''>Select activity</option>
          {activities.map(act => (
            <option key={act.id} value={act.id}>{act.name} (ID: {act.id})</option>
          ))}
        </select>
        <label>After Activity:</label>
        <select style={inputStyle} value={orderingForm.activityIdeaAfter_id} onChange={e => setOrderingForm({ ...orderingForm, activityIdeaAfter_id: e.target.value })}>
          <option value=''>Select activity</option>
          {activities.map(act => (
            <option key={act.id} value={act.id}>{act.name} (ID: {act.id})</option>
          ))}
        </select>
        
        <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleOrderingSubmit()}}>{editOrderingId ? 'Save Changes' : 'Add Ordering Constraint'}</button>
      </form>
    </section>

      <section style={sectionStyle}>
      <h3>Activities Distance Constraints</h3>
      <ul>
        {activitiesDistanceConstraints.map(c => (
          <li key={c.id}>
            {c.activity1Id} - {c.activity1Name} → {c.activity2Id} - {c.activity2Name} | Type: {c.type} | Distance: {c.distance} mins
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivitiesDistanceEdit(c)}}>Edit</button>
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivitiesDistanceDelete(c.id)}}>Delete</button>
          </li>
        ))}
      </ul>

      <form style={formStyle}>
        <h3>{activitiesDistanceForm.id ? 'Edit' : 'Add'} Activities Distance Constraint</h3>
        <label>Activity 1:</label>
        <select style={inputStyle} value={activitiesDistanceForm.activityIdea1_id} onChange={e => setActivitiesDistanceForm({ ...activitiesDistanceForm, activityIdea1_id: e.target.value })}>
          <option value=''>Select activity 1</option>
          {activities.map(a => <option key={a.id} value={a.id}>{a.name}</option>)}
        </select>
        <label>Activity 2:</label>
        <select style={inputStyle} value={activitiesDistanceForm.activityIdea2_id} onChange={e => setActivitiesDistanceForm({ ...activitiesDistanceForm, activityIdea2_id: e.target.value })}>
          <option value=''>Select activity 2</option>
          {activities.map(a => <option key={a.id} value={a.id}>{a.name}</option>)}
        </select>
        <label>Distance Type:</label>
        <select style={inputStyle} value={activitiesDistanceForm.type} onChange={e => setActivitiesDistanceForm({ ...activitiesDistanceForm, type: e.target.value })}>
          <option value='MINIMUM'>MINIMUM</option>
          <option value='MAXIMUM'>MAXIMUM</option>
        </select>
        <label>Distance (in minutes):</label>
        <input style={inputStyle} type='number' value={activitiesDistanceForm.distance} onChange={e => setActivitiesDistanceForm({ ...activitiesDistanceForm, distance: e.target.value })} />
        
        <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivitiesDistanceSubmit()}}>{activitiesDistanceForm.id ? 'Update' : 'Add'} Constraint</button>
      </form>
    </section>

      <section style={sectionStyle}>
      <h3>Activities Distance Preferences</h3>
      <ul>
        {activitiesDistancePrefs.map(p => (
          <li key={p.id}>
            {p.activity1Id} - {p.activity1Name} → {p.activity2Id} - {p.activity2Name} | Type: {p.type} | Distance: {p.distance} mins
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivitiesDistancePrefEdit(p)}}>Edit</button>
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivitiesDistancePrefDelete(p.id)}}>Delete</button>
          </li>
        ))}
      </ul>

      <form style={formStyle}>
        <h3>{editPrefId ? 'Edit' : 'Add'} Activities Distance Preference</h3>
        <label>Activity 1:</label>
        <select style={inputStyle} value={activitiesDistancePrefForm.activityIdea1_id} onChange={e => setActivitiesDistancePrefForm({ ...activitiesDistancePrefForm, activityIdea1_id: e.target.value })}>
          <option value=''>Select activity 1</option>
          {activities.map(a => <option key={a.id} value={a.id}>{a.name}</option>)}
        </select>
        <label>Activity 2:</label>
        <select style={inputStyle} value={activitiesDistancePrefForm.activityIdea2_id} onChange={e => setActivitiesDistancePrefForm({ ...activitiesDistancePrefForm, activityIdea2_id: e.target.value })}>
          <option value=''>Select activity 2</option>
          {activities.map(a => <option key={a.id} value={a.id}>{a.name}</option>)}
        </select>
        <label>Distance Type:</label>
        <select style={inputStyle} value={activitiesDistancePrefForm.type} onChange={e => setActivitiesDistancePrefForm({ ...activitiesDistancePrefForm, type: e.target.value })}>
          <option value='MINIMUM'>MINIMUM</option>
          <option value='MAXIMUM'>MAXIMUM</option>
        </select>
        <label>Distance (in minutes):</label>
        <input style={inputStyle} type='number' value={activitiesDistancePrefForm.distance} onChange={e => setActivitiesDistancePrefForm({ ...activitiesDistancePrefForm, distance: e.target.value })} />
        
        <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivitiesDistancePrefSubmit()}}>{editPrefId ? 'Update' : 'Add'} Preference</button>
      </form>
    </section>

      
      <section style={sectionStyle}>
      <h3>Activity Part Distance Preferences</h3>
      <ul>
        {activityPartDistancePrefs.map(p => (
          <li key={p.id}>
            Activity ID: {p.activityId} - Name: {p.activityName} - Type: {p.type} - Distance: {p.distance} minutes
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivityPartDistancePrefEdit(p)}}>Edit</button>
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivityPartDistancePrefDelete(p.id)}}>Delete</button>
          </li>
        ))}
      </ul>

      <form style={formStyle}>
        <h3>{editAPrefId ? 'Edit' : 'Add'} Activity Part Distance Preference</h3>
        <label>Activity:</label>
        <select style={inputStyle} value={activityPartDistancePrefForm.activityIdea_id} onChange={e => setActivityPartDistancePrefForm({ ...activityPartDistancePrefForm, activityIdea_id: e.target.value })}>
          <option value=''>Select activity</option>
          {activities.map(act => (
            <option key={act.id} value={act.id}>{act.name} (ID: {act.id})</option>
          ))}
        </select>
        <label>Distance Type:</label>
        <select style={inputStyle} value={activityPartDistancePrefForm.type} onChange={e => setActivityPartDistancePrefForm({ ...activityPartDistancePrefForm, type: e.target.value })}>
          <option value='MINIMUM'>MINIMUM</option>
          <option value='MAXIMUM'>MAXIMUM</option>
        </select>
        <label>Distance (in minutes):</label>
        <input style={inputStyle} type='number' value={activityPartDistancePrefForm.distance} onChange={e => setActivityPartDistancePrefForm({ ...activityPartDistancePrefForm, distance: e.target.value })} />
        
        <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivityPartDistancePrefSubmit()}}>{editAPrefId ? 'Save Changes' : 'Add Preference'}</button>
      </form>
    </section>

<section style={sectionStyle}>
      <h3>Ordering Preferences</h3>
      <ul>
        {orderingPrefs.map(p => (
          <li key={p.id}>
            {p.beforeId} - {p.beforeName} → {p.afterId} - {p.afterName}
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleOrderingPrefEdit(p)}}>Edit</button>
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleOrderingPrefDelete(p.id)}}>Delete</button>
          </li>
        ))}
      </ul>

      <form style={formStyle}>
        <h3>{editOrderingPrefId ? 'Edit' : 'Add'} Ordering Preference</h3>
        <label>Before Activity:</label>
        <select style={inputStyle} value={orderingPrefForm.activityIdeaBefore_id} onChange={e => setOrderingPrefForm({ ...orderingPrefForm, activityIdeaBefore_id: e.target.value })}>
          <option value=''>Select activity</option>
          {activities.map(act => (
            <option key={act.id} value={act.id}>{act.name} (ID: {act.id})</option>
          ))}
        </select>
        <label>After Activity:</label>
        <select style={inputStyle} value={orderingPrefForm.activityIdeaAfter_id} onChange={e => setOrderingPrefForm({ ...orderingPrefForm, activityIdeaAfter_id: e.target.value })}>
          <option value=''>Select activity</option>
          {activities.map(act => (
            <option key={act.id} value={act.id}>{act.name} (ID: {act.id})</option>
          ))}
        </select>
        
        <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleOrderingPrefSubmit()}}>{editOrderingPrefId ? 'Save Changes' : 'Add Preference'}</button>
      </form>
    </section>

      <section style={sectionStyle}>
      <h3>Activity Duration Preferences</h3>
      <ul>
        {activityDurationPrefs.map(p => (
          <li key={p.id}>
            Activity ID: {p.activityId} - Name: {p.activityName} - Min: {p.minimumDuration} - Max: {p.maximumDuration} minutes
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivityDurationPrefEdit(p)}}>Edit</button>
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivityDurationPrefDelete(p.id)}}>Delete</button>
          </li>
        ))}
      </ul>

      <form style={formStyle}>
        <h3>{editDurPrefId ? 'Edit' : 'Add'} Activity Duration Preference</h3>
        <label>Activity:</label>
        <select style={inputStyle} value={activityDurationPrefForm.activityIdea_id} onChange={e => setActivityDurationPrefForm({ ...activityDurationPrefForm, activityIdea_id: e.target.value })}>
          <option value=''>Select activity</option>
          {activities.map(act => (
            <option key={act.id} value={act.id}>{act.name} (ID: {act.id})</option>
          ))}
        </select>
        <label>Minimum Duration (minutes):</label>
        <input style={inputStyle} type='number' value={activityDurationPrefForm.minimumDuration} onChange={e => setActivityDurationPrefForm({ ...activityDurationPrefForm, minimumDuration: e.target.value })} />
        <label>Maximum Duration (minutes):</label>
        <input style={inputStyle} type='number' value={activityDurationPrefForm.maximumDuration} onChange={e => setActivityDurationPrefForm({ ...activityDurationPrefForm, maximumDuration: e.target.value })} />
        
        <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivityDurationPrefSubmit()}}>{editDurPrefId ? 'Save Changes' : 'Add Preference'}</button>
      </form>
    </section>

    <section style={sectionStyle}>
      <h3>Activity Schedule Time Preferences</h3>
      <ul>
        {activityScheduleTimePrefs.map(p => (
          <li key={p.id}>
            Activity ID: {p.activityId} - Name: {p.activityName} - Type: {p.type} - Time: {p.timeOfAnalysis}
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivityScheduleTimePrefEdit(p)}}>Edit</button>
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivityScheduleTimePrefDelete(p.id)}}>Delete</button>
          </li>
        ))}
      </ul>

      <form style={formStyle}>
        <h3>{editSchedulePrefId ? 'Edit' : 'Add'} Activity Schedule Time Preference</h3>
        <label>Activity:</label>
        <select style={inputStyle} value={activityScheduleTimePrefForm.activityIdea_id} onChange={e => setActivityScheduleTimePrefForm({ ...activityScheduleTimePrefForm, activityIdea_id: e.target.value })}>
          <option value=''>Select activity</option>
          {activities.map(act => (
            <option key={act.id} value={act.id}>{act.name} (ID: {act.id})</option>
          ))}
        </select>
        <label>Time Preference Type:</label>
        <select style={inputStyle} value={activityScheduleTimePrefForm.type} onChange={e => setActivityScheduleTimePrefForm({ ...activityScheduleTimePrefForm, type: e.target.value })}>
          <option value='EARLIER'>EARLIER</option>
          <option value='LATER'>LATER</option>
        </select>
        <label>Time of Analysis (ISO format):</label>
        <input style={inputStyle} type='datetime-local' value={activityScheduleTimePrefForm.timeOfAnalysis} onChange={e => setActivityScheduleTimePrefForm({ ...activityScheduleTimePrefForm, timeOfAnalysis: e.target.value })} />
        
        <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleActivityScheduleTimePrefSubmit()}}>{editSchedulePrefId ? 'Save Changes' : 'Add Preference'}</button>
      </form>
    </section>

      <section style={sectionStyle}>
      <h3>Implication Preferences</h3>
      <ul>
        {implicationPrefs.map(p => (
          <li key={p.id}>
            Initial: {p.initialId} - {p.initialName} → Implied: {p.activityIdeaImplied_id} - {p.implied?.name || 'Unknown'}
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleImplicationPrefEdit(p)}}>Edit</button>
            <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleImplicationPrefDelete(p.id)}}>Delete</button>
          </li>
        ))}
      </ul>

      <form style={formStyle}>
        <h4>{editImplicationPrefId ? 'Edit' : 'Add'} Implication Preference</h4>
        <label>Initial Activity:</label>
        <select style={inputStyle} value={implicationPrefForm.activityIdeaInitial_id} onChange={e => setImplicationPrefForm({ ...implicationPrefForm, activityIdeaInitial_id: e.target.value })}>
          <option value=''>Select initial activity</option>
          {activities.map(a => (
            <option key={a.id} value={a.id}>{a.name} (ID: {a.id})</option>
          ))}
        </select>

        <label>Implied Activity:</label>
        <select style={inputStyle} value={implicationPrefForm.activityIdeaImplied_id} onChange={e => setImplicationPrefForm({ ...implicationPrefForm, activityIdeaImplied_id: e.target.value })}>
          <option value=''>Select implied activity</option>
          {activities.map(a => (
            <option key={a.id} value={a.id}>{a.name} (ID: {a.id})</option>
          ))}
        </select>


        <button style={buttonStyle} onClick={(e) => { e.preventDefault(); handleImplicationPrefSubmit()}}>{editImplicationPrefId ? 'Update' : 'Add'} Preference</button>
      </form>
    </section>

    </div>
  );
};



export default ConstraintsPreferencesPage;
