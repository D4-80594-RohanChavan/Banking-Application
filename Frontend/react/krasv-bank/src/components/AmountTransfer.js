import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './AmountTransfer.css';

const AmountTransfer = () => {
  const [formData, setFormData] = useState({
    accountNumber: '',
    amount: '',
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log('Amount transfer data submitted:', formData);
    // Navigate to payment success page
    navigate('/payment-success');
  };

  const backgroundImageUrl = process.env.PUBLIC_URL + '/images/amount.jpg';

  return (
    <div className="amount-transfer-container" style={{ backgroundImage: `url(${backgroundImageUrl})` }}>
      <div className="form-container">
        <h2>Amount Transfer</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="accountNumber">Account Number</label>
            <input
              type="text"
              id="accountNumber"
              name="accountNumber"
              value={formData.accountNumber}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="amount">Amount</label>
            <input
              type="number"
              id="amount"
              name="amount"
              value={formData.amount}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-buttons">
            <button type="submit">Confirm Payment</button>
          </div>
          <div className="cancel-link-container">
            <Link to="/dashboard" className="cancel-link">Cancel Payment</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AmountTransfer;
