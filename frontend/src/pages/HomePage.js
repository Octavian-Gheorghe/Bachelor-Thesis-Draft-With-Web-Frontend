import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';

const HomePage = () => {

  return (
    <div style={pageStyle}>
      <h1>Welcome!</h1>
      <nav style={navStyle}>
        <Link to="/login" className="nav-button" style={buttonStyle}>Login</Link>
        <Link to="/register" className="nav-button" style={buttonStyle}>Register</Link>
        <Link to="/activities" className="nav-button" style={buttonStyle}>Your Activities</Link>
        <Link to="/schedules" className="nav-button" style={buttonStyle}>Your Schedules</Link>
        <Link to="/generate-schedule" className="nav-button" style={buttonStyle}>Generate a Schedule</Link>
      </nav>
    </div>
  );
};

const pageStyle = {
  background: 'linear-gradient(to bottom right, #483AA0, #7965C1)',
  height: '100vh',
  color: '#E3D095',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center'
};

const navStyle = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  gap: '10px'
};

const buttonStyle = {
  backgroundColor: '#0E2148',
  color: '#E3D095',
  border: '2px solid #E3D095',
  borderRadius: '8px',
  padding: '10px 20px',
  textDecoration: 'none',
  textAlign: 'center',
  cursor: 'pointer',
  transition: 'all 0.3s ease'
};

const sheet = document.styleSheets[0];
    sheet.insertRule(`
      .nav-button:hover {
        background-color: #E3D095 !important;
        color: #0E2148 !important;
      }
    `, sheet.cssRules.length);

export default HomePage;