import React from 'react';
import './About.css';

const About = () => {
  return (
    <div className="about-container">
      <h1>There's More to Our Story</h1>

      <div className="about-section">
        <img src="/images/about1.jpeg" alt="Krasv Bank" className="about-image" />
        <div className="about-content">
          <h2>About Krasv Bank</h2>
          <p>
            Founded in 2000, Krasv Bank has been a pillar of trust and reliability in the banking industry for over two decades. Our mission is to provide comprehensive financial solutions that empower individuals and businesses to achieve their financial goals.
          </p>
        </div>
      </div>

      <div className="about-section">
        <img src="/images/about2.jpg" alt="Our Vision" className="about-image" />
        <div className="about-content">
          <h2>Our Vision</h2>
          <p>
            At Krasv Bank, our vision is to be the most trusted and innovative financial institution, creating lasting value for our customers, employees, and stakeholders. We aim to foster financial inclusion and contribute to the economic growth of the communities we serve.
          </p>
        </div>
      </div>

      <div className="about-section">
        <img src="/images/about3.jpg" alt="Our Services" className="about-image" />
        <div className="about-content">
          <h2>Our Services</h2>
          <ul>
            <li><strong>Personal Banking:</strong> Tailored solutions for your savings, loans, and daily banking needs.</li>
            <li><strong>Business Banking:</strong> Comprehensive services to support your business growth and financial management.</li>
            <li><strong>Investment Services:</strong> Expert advice and investment options to grow your wealth.</li>
            <li><strong>Digital Banking:</strong> Convenient and secure online banking services at your fingertips.</li>
          </ul>
        </div>
      </div>

      <div className="about-section">
        <img src="/images/about4.jpg" alt="Our Commitment" className="about-image" />
        <div className="about-content">
          <h2>Our Commitment</h2>
          <p>
            We are committed to maintaining the highest standards of integrity, transparency, and customer service. Our dedicated team of professionals works tirelessly to ensure that our customers receive personalized and efficient banking experiences.
          </p>
        </div>
      </div>

      <div className="about-section">
        <img src="/images/amount.jpg" alt="Corporate Responsibility" className="about-image" />
        <div className="about-content">
          <h2>Corporate Responsibility</h2>
          <p>
            Krasv Bank believes in giving back to the community. Through various CSR initiatives, we support education, healthcare, and environmental sustainability projects, aiming to make a positive impact on society.
          </p>
        </div>
      </div>

      <div className="about-section">
        <img src="/images/about5.jpg" alt="Join Us" className="about-image" />
        <div className="about-content">
          <h2>Join Us</h2>
          <p>
            Become a part of the Krasv Bank family and experience banking like never before. Whether you are looking to manage your personal finances or grow your business, we have the right solutions for you. Visit our branches or explore our digital banking platform to get started.
          </p>
        </div>
      </div>
    </div>
  );
}

export default About;
