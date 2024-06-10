import React from 'react';
import { Link } from 'react-router-dom';
import './Dashboard.css';

const Dashboard = () => {
  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <h1>Welcome to Krasv Bank</h1>
        <Link to="/" className="logout-button">Logout</Link>
      </header>
      <div className="dashboard-content">
        <div className="dashboard-sidebar">
          <ul>
            <li><Link to="/amount-transfer">Amount Transfer</Link></li>
            <li><Link to="/credit-history">Credit History</Link></li>
            <li><Link to="/debit-history">Debit History</Link></li>
            <li><Link to="/contact">contact</Link></li>
          </ul>
        </div>
        <div className="dashboard-main">
          <img src="/images/account.png" alt="Dashboard" />
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
