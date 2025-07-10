import React from 'react';
import { useLocation } from 'react-router-dom';

const GeneratedScheduleResultPage = () => {
  const { state } = useLocation();
  const result = state?.result || [];

  return (
    <div
      style={{
        background: 'linear-gradient(to bottom right, #483AA0, #7965C1)',
        minHeight: '100vh',
        color: '#E3D095',
        padding: '2rem'
      }}
    >
      <h1 style={{ textAlign: 'center' }}>Generated Schedule</h1>
      {result.length === 0 ? (
        <p>No schedule data available.</p>
      ) : (
        <ul style={{ listStyle: 'none', padding: 0 }}>
          {result.map((item, index) => {
            const date = item.startTime.split('T')[0];
            const time = item.startTime.split('T')[1];
            return (
              <li
                key={index}
                className="hoverable-result"
                style={{
                  backgroundColor: '#0E2148',
                  border: '2px solid #E3D095',
                  borderRadius: '10px',
                  padding: '1rem',
                  marginBottom: '1rem',
                  transition: 'transform 0.2s ease-in-out'
                }}
              >
                <strong>{item.name}</strong><br />
                Date: {date}<br />
                <strong>Time:</strong> {time}<br />
                <strong>Duration:</strong> {item.duration} minutes<br />
                <strong>Location:</strong> {item.locationName}
              </li>
            );
          })}
        </ul>
      )}
    </div>
  );
};
const styleSheet = document.styleSheets[0];
if (styleSheet && styleSheet.insertRule) {
  styleSheet.insertRule(`
    .hoverable-result:hover {
      transform: scale(1.02);
    }
  `, styleSheet.cssRules.length);
}

export default GeneratedScheduleResultPage;