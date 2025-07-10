import React, { createContext, useContext, useState } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [authToken, setAuthToken] = useState(null);
  const [loggedUsername, setLoggedUsername] = useState(null);

  const login = (token) => setAuthToken(token);
  const logout = () => setAuthToken(null);
  const getLoggedInUsername = (username) => setLoggedUsername(username);

  return (
    <AuthContext.Provider value={{ authToken, loggedUsername, login, logout, getLoggedInUsername }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);