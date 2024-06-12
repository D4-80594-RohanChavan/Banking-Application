import React from 'react';
import { useNavigate } from 'react-router-dom';
import './AdminDashboard.css';

const AdminDashboard = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    // Handle logout logic here
    navigate('/');
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <button onClick={handleLogout} className="logout-button">Logout</button>
      </header>
      <div className="dashboard-content">
        <h1>Customer Management</h1>
        <p>Manage customer accounts and activities here.</p>
      </div>
    </div>
  );
};

export default AdminDashboard;