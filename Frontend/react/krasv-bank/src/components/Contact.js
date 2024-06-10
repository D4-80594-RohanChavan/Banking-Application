import React, { useState } from 'react';
import './Contact.css';

const Contact = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    subject: '',
    message: '',
  });

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
    console.log('Contact form data submitted:', formData);
  };

  return (
    <div className="contact-container">
      <h2>Contact Us</h2>
      <p>
        At Krasv Bank, we value your feedback and inquiries. Whether you have a question about our services, need assistance, or want to provide feedback, our team is here to help you. Please fill out the form below to get in touch with us.
      </p>
      <div className="contact-info">
        <h3>Our Contact Information</h3>
        <p>
          <strong>Address:</strong> 1234 Bank Street, Financial City, Country<br />
          <strong>Phone:</strong> +1 234 567 890<br />
          <strong>Email:</strong> support@krasvbank.com
        </p>
        <h3>Business Hours</h3>
        <p>
          <strong>Monday - Friday:</strong> 9:00 AM - 5:00 PM<br />
          <strong>Saturday:</strong> 10:00 AM - 2:00 PM<br />
          <strong>Sunday:</strong> Closed
        </p>
      </div>
      <h3>Send Us a Message</h3>
      <form onSubmit={handleSubmit}>
        <label htmlFor="name">Name</label>
        <input
          type="text"
          id="name"
          name="name"
          value={formData.name}
          onChange={handleChange}
          required
        />
        <label htmlFor="email">Email</label>
        <input
          type="email"
          id="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <label htmlFor="subject">Subject</label>
        <input
          type="text"
          id="subject"
          name="subject"
          value={formData.subject}
          onChange={handleChange}
          required
        />
        <label htmlFor="message">Message</label>
        <textarea
          id="message"
          name="message"
          value={formData.message}
          onChange={handleChange}
          required
        ></textarea>
        <button type="submit">Send Message</button>
      </form>
    </div>
  );
}

export default Contact;
