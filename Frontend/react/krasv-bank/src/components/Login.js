import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

const Login = () => {
  const [isAdmin, setIsAdmin] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [adminId, setAdminId] = useState('');
  const [adminPassword, setAdminPassword] = useState('');
  const navigate = useNavigate();

  const handleCustomerLogin = (event) => {
    event.preventDefault();
    // Handle customer login logic here
    navigate('/dashboard');
  };

  const handleAdminLogin = (event) => {
    event.preventDefault();
    // Handle admin login logic here
    navigate('/adminDashboard');
  };

  return (
    <div className="login-container" >
      <h1>Login</h1>
      <div className="tabs">
        <button 
          className={`tab-button ${!isAdmin ? 'active' : ''}`} 
          onClick={() => setIsAdmin(false)}
        >
          Customer Login
        </button>
        <button 
          className={`tab-button ${isAdmin ? 'active' : ''}`} 
          onClick={() => setIsAdmin(true)}
        >
          Admin Login
        </button>
      </div>

      {!isAdmin ? (
        <form onSubmit={handleCustomerLogin}>
          <div className="form-group">
            <label htmlFor="email">Email:</label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password:</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button type="submit">Login</button>
          <div className="create-account">
            <p>Don't have an account? <a href="/registration">Create one</a></p>
          </div>
        </form>
      ) : (
        <form onSubmit={handleAdminLogin}>
          <div className="form-group">
            <label htmlFor="adminId">Admin ID:</label>
            <input
              type="text"
              id="adminId"
              value={adminId}
              onChange={(e) => setAdminId(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="adminPassword">Password:</label>
            <input
              type="password"
              id="adminPassword"
              value={adminPassword}
              onChange={(e) => setAdminPassword(e.target.value)}
              required
            />
          </div>
          <button type="submit">Login</button>
        </form>
      )}

      <footer className="footer">
        <p>&copy; 2024 Krasv Bank. All rights reserved.</p>
        <div className="footer-links">
          <a href="/privacy-policy">Privacy Policy</a>
          <a href="/terms-of-service">Terms of Service</a>
        </div>
      </footer>
    </div>
  );
};

export default Login;