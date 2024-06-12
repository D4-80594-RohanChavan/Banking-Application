
import React from 'react';
import './Header.css'; // Import CSS for styling

const Header = () => {
  return (
    <header className="header">
      <div className="header-content">
        <img src="/images/logo.png" alt="Krasv Bank Logo" className="header-logo" />
        <h1 className="header-title"> </h1>
      </div>
      <nav>
        <ul>
          <li><a href="/"><i className="fas fa-home"></i> Home</a></li>
          <li><a href="/about"><i className="fas fa-info-circle"></i> About</a></li>
          <li><a href="/contact"><i className="fas fa-envelope"></i> Contact</a></li>
          <li><a href="/login"><i className="fas fa-sign-in-alt"></i> Login</a></li>
          <li><a href="/register"><i className="fas fa-user-plus"></i> Register</a></li>
        </ul>
      </nav>
    </header>
  );
}

export default Header;