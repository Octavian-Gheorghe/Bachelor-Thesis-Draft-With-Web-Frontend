import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import HomePage from './pages/HomePage';
import Header from './components/Header';
import { AuthProvider } from './context/AuthContext';
import ActivityIdeasPage from './pages/ActivityIdeasPage';
import SchedulesPage from './pages/SchedulesPage';
import GenerateSchedulePage from './pages/GenerateSchedulePage';
import GeneratedScheduleResultPage from './pages/GeneratedScheduleResultPage';
import CreateActivityIdeaPage from './pages/CreateActivityIdeaPage';
import ViewActivityIdeaPage from './pages/ViewActivityIdeaPage';
import ConstraintsPreferencesPage from './pages/ConstraintsPreferencesPage';
import ProtectedRoute from './components/ProtectedRoute';
import './global.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Header />
        <div className="page-content">
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            <Route path="/" element={<ProtectedRoute><HomePage /></ProtectedRoute>} />
            <Route path="/activities" element={<ProtectedRoute><ActivityIdeasPage /></ProtectedRoute>} />
            <Route path="/schedules" element={<ProtectedRoute><SchedulesPage /></ProtectedRoute>} />
            <Route path="/generate-schedule" element={<ProtectedRoute><GenerateSchedulePage /></ProtectedRoute>} />
            <Route path="/generated-schedule" element={<ProtectedRoute><GeneratedScheduleResultPage /></ProtectedRoute>} />
            <Route path="/create-activity" element={<ProtectedRoute><CreateActivityIdeaPage /></ProtectedRoute>} />
            <Route path="/activities/:id" element={<ProtectedRoute><ViewActivityIdeaPage /></ProtectedRoute>} />
            <Route path="/constraints" element={<ProtectedRoute><ConstraintsPreferencesPage /></ProtectedRoute>} />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
