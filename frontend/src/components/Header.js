import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const Header = () => {
  const [clicked, setClicked] = useState(false);

  const handleClick = () => {
    setClicked(true);
    setTimeout(() => setClicked(false), 200); // Reset after animation
  };

  return (
    <header style={headerStyle}>
      <Link
        to="/"
        style={{ ...logoStyle, ...(clicked ? clickedStyle : {}) }}
        onClick={handleClick}
      >
        <span style={{ fontWeight: 'bold' }}>Flexi</span>
        <span style={{ color: '#E3D095' }}>Time</span>
      </Link>
    </header>
  );
};

const headerStyle = {
  backgroundColor: '#0E2148',
  padding: '15px 30px',
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  color: '#E3D095',
  position: 'fixed',
  top: 0,
  left: 0,
  right: 0,
  zIndex: 1000,
  height: '60px'
};

const logoStyle = {
  color: '#E3D095',
  textDecoration: 'none',
  fontSize: '24px',
  transition: 'transform 0.2s ease'
};

const clickedStyle = {
  transform: 'scale(1.1)'
};

export default Header;
