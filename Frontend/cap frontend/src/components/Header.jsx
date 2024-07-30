import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Header.css';

function Header() {
  const location = useLocation();
  const hideHeaderPaths = ['dashboard']; // Add more paths if needed

  // Check if the current path matches any of the paths where the header should be hidden
  const shouldHideHeader = hideHeaderPaths.includes(location.pathname);

  if (shouldHideHeader) {
    return null; // Don't render the header
  }

  return (
    <header className="header">
      <div className="container">
        <div className="row align-items-center">
          <div className="col-3">
            <img src="/src/assets/logo.jpg" alt="Krasv" width="220px" />
          </div>
          <nav className="col-9">
            <ul className="nav justify-content-end">
              <li className="nav-item">
                <Link to="/" className="nav-link">Home</Link>
              </li>
              <li className="nav-item">
                <Link to="/about" className="nav-link">About</Link>
              </li>
              <li className="nav-item">
                <Link to="/contact" className="nav-link">Contact</Link>
              </li>
              <li className="nav-item">
                <Link to="/terms" className="nav-link">Terms of Conditions</Link>
              </li>
              <li className="nav-item">
                <Link to="/privacy" className="nav-link">Privacy Policy</Link>
              </li>
              {/* <li className="nav-item">
                <Link to="/sign-in">
                  <button className="btn btn-primary">Sign In</button>
                </Link>
              </li> */}
              <li className="nav-item">
                <Link to="/sign-up">
                  <button className="btn btn-primary">Register Here</button>
                </Link>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </header>
  );
}

export default Header;
