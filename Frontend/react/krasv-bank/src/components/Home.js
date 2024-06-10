import React from 'react';
import './Home.css';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  const navigate = useNavigate();

  return (
    <div className="home-container">
      <div className="header-image">
      <img src="/images/logo.png" alt="Header" />
      </div>

      <div className="info-section">
      <img src="/images/savings.jpg" alt="Header" />
        <div className="info-box">
          <h2>Benefits of Creating an Account</h2>
          <ul>
            <li>24/7 Customer Support</li>
            <li>Easy Account Setup</li>
            <li>High Security</li>
            <li>Mobile Banking</li>
            <li>Debit Card Delivery</li>

          </ul>
          <button onClick={() => navigate('/login')}>Get Started</button>
        </div>
      </div>

      <div className="additional-image">
      <img src="/images/logo3.jpg" alt="Header" />
      </div>

      <div className="three-sections">
        <div className="section-box">
          <h2>A Bank That Cares</h2>
          <p>      We listen to your needs,</p>
          <p>understand your ambitions and support your plans so you can succeed and be happy.</p>
          <button onClick={() => navigate('/about')}>Learn More</button>
        </div>
        <div className="section-box">
          <h2>Why Join Us?</h2>
          <p>Discover the benefits of becoming a Krasv Bank customer.</p>
          <button onClick={() => navigate('/login')}>Join Us</button>
        </div>
        <div className="section-box">
          <h2>Get in Touch</h2>
          <p>Contact us for any inquiries or support.</p>
          <button onClick={() => navigate('/contact')}>Contact</button>
        </div>
      </div>

      <footer className="footer">
        <p>&copy; 2024 Krasv Bank. All rights reserved.</p>
        <div className="footer-links">
          <a href="/privacy-policy">Privacy Policy</a>
          <a href="/terms-of-service">Terms of Service</a>
          <a href="/contact">Contact Us</a>
        </div>
      </footer>
    </div>
  );
};

export default Home;
