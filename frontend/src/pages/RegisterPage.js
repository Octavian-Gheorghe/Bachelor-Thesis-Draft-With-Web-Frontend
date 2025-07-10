import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import API_BASE_URL from '../config';
import { Link } from 'react-router-dom';

const RegisterPage = () => {
  const { login, getLoggedInUsername } = useAuth();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const isPasswordValid = (password) => {
    const hasUppercase = /[A-Z]/.test(password);
    const hasLowercase = /[a-z]/.test(password);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);
    return hasUppercase && hasLowercase && hasSpecialChar;
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setErrorMessage('');

    if (password !== confirmPassword) {
      setErrorMessage('Passwords do not match');
      return;
    }

    if (!isPasswordValid(password)) {
      setErrorMessage('Password must include at least 1 uppercase letter, 1 lowercase letter, and 1 special character.');
      return;
    }

    setIsLoading(true);
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 7000);

    try {
      const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
        signal: controller.signal
      });

      clearTimeout(timeoutId);
      setIsLoading(false);

      const data = await response.json();

      if (response.ok) {
        login(data.accesToken);
        getLoggedInUsername(username);
        navigate('/');
      } else if (response.status === 401) {
        setErrorMessage('Unauthorized. Please try a different username or password.');
      } else {
        setErrorMessage('Registration failed. Please try again.');
      }
    } catch (err) {
      clearTimeout(timeoutId);
      setIsLoading(false);
      if (err.name === 'AbortError') {
        setErrorMessage('Request timed out. Please check your internet connection and try again.');
      } else {
        setErrorMessage('Network error. Please try again later.');
      }
    }
  };

  return (
  <div style={pageStyle}>
    {isLoading && <div style={modalStyle}>Registering...</div>}
    <form onSubmit={handleRegister} style={formStyle}>
      <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder="Username" style={inputStyle} />
      <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" style={inputStyle} />
      <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} placeholder="Confirm Password" style={inputStyle} />
      {errorMessage && <p style={errorStyle}>{errorMessage}</p>}
      <button type="submit" style={buttonStyle} disabled={isLoading}>Register</button>

      <p style={{ marginTop: '10px', color: '#E3D095' }}>
        Already have an account? <Link to="/login" style={{ color: '#E3D095', textDecoration: 'underline' }}>Login</Link>
      </p>
    </form>
  </div>
);
};


const pageStyle = {
  background: 'linear-gradient(to bottom right, #483AA0, #7965C1)',
  height: '100vh',
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  position: 'relative'
};

const formStyle = {
  backgroundColor: '#0E2148',
  padding: '30px',
  borderRadius: '10px',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  color: '#E3D095',
  border: '2px solid #E3D095',
  zIndex: 1
};

const inputStyle = {
  marginBottom: '10px',
  padding: '10px',
  borderRadius: '5px',
  border: '1px solid #E3D095',
  width: '250px'
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
  alignSelf: 'center'
};

const errorStyle = {
  color: '#FFBABA',
  backgroundColor: '#4E0E0E',
  padding: '10px',
  borderRadius: '5px',
  marginBottom: '10px',
  textAlign: 'center',
  maxWidth: '300px'
};

const modalStyle = {
  position: 'absolute',
  top: '40%',
  backgroundColor: '#0E2148',
  padding: '20px 40px',
  borderRadius: '10px',
  color: '#E3D095',
  fontSize: '18px',
  border: '2px solid #E3D095',
  boxShadow: '0 0 15px rgba(0, 0, 0, 0.5)',
  zIndex: 2
};

export default RegisterPage;
