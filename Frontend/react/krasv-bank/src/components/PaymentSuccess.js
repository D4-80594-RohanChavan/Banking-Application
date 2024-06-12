import React from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import './PaymentSuccess.css';

const PaymentSuccess = () => {
  const successImageUrl = '/images/contact4.jpg'; // Update with the correct path to your success image

  return (
    <div className="payment-success-container" style={{ backgroundImage: `url(${successImageUrl})` }}>
      <div className="success-content">
        <h1>
          Payment Successful! <FontAwesomeIcon icon={faCheckCircle} className="success-icon" />
        </h1>
        <p>Your payment has been processed successfully.</p>
        <Link to="/dashboard" className="back-button">Back to Home</Link>
      </div>
    </div>
  );
};

export default PaymentSuccess;
