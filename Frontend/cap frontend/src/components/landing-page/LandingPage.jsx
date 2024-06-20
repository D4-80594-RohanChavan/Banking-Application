import React from 'react';
import { CiBank } from 'react-icons/ci';
import { FiEye, FiUsers } from 'react-icons/fi';
import { FaHandshake, FaRegCopyright } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import Logo from '../../assets/logo.jpg';
import Mobile from '../../assets/mobile.png';
import './LandingPage.css';

function LandingPage() {
    return (
        <>
            
            <div className="container">
                <div className="row align-items-center">
                    <div className="col-md-6 col-12">
                        <h1 className="heading">NEXT GENERATION DIGITAL BANKING</h1>
                        <p className="paragraph">Take your financial life online. Your Krasv bank account will be a one-stop-shop for spending, saving, budgeting, investing, and much more.</p>
                        <div className="row g-3">
                            <div className="col-md-6 col-12">
                                <Link to="/sign-in" className="btn btn-sm btn-primary rounded-pill w-100">Sign In</Link>
                            </div>
                            <div className="col-md-6 col-12">
                                <Link to="/sign-up" className="btn btn-sm btn-primary rounded-pill w-100">Sign up</Link>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-6 col-12 bg-home">
                        <img src={Mobile} alt="Krasv" className="img-fluid" />
                    </div>
                </div>
            </div>
            <div className="container-fluid bg-body-secondary py-5">
                <div className="container">
                    <div className="row g-3">
                        <div className="col-12">
                            <h1 className="display-5 mb-4 heading">There's More to Our Story</h1>
                            {/*<p className="mb-5 paragraph">Founded in 2000, Krasv Bank has been a pillar of trust and reliability in the banking industry for over two decades. Our mission is to provide comprehensive financial solutions that empower individuals and businesses to achieve their financial goals.</p>*/}
                        </div>
                        <div className="col-lg-3 col-md-6 col-12">
                            <div className="card shadow text-center h-100">
                                <div className="card-body">
                                    <CiBank fontSize={50} className="bg-primary text-white rounded-5 p-1 mb-3" />
                                    <h4 className="heading">Banking Online</h4>
                                    <p className="paragraph">Our modern web and mobile applications allow you to keep track of your finances wherever you are in the world. Our dedicated team of professionals works tirelessly to ensure that our customers receive personalized and efficient banking experiences.</p>
                                </div>
                            </div>
                        </div>
                        <div className="col-lg-3 col-md-6 col-12">
                            <div className="card shadow text-center h-100">
                                <div className="card-body">
                                    <FiEye fontSize={50} className="bg-primary text-white rounded-5 p-1 mb-3" />
                                    <h4 className="heading">Our Vision</h4>
                                    <p className="paragraph">At Krasv Bank, our vision is to be the most trusted and innovative financial institution, creating lasting value for our customers, employees, and stakeholders. We aim to foster financial inclusion and contribute to the economic growth of the communities we serve.</p>
                                </div>
                            </div>
                        </div>
                        <div className="col-lg-3 col-md-6 col-12">
                            <div className="card shadow text-center h-100">
                                <div className="card-body">
                                    <FiUsers fontSize={50} className="bg-primary text-white rounded-5 p-1 mb-3" />
                                    <h4 className="heading">Our Services</h4>
                                    <p className="paragraph">Personal Banking: Tailored solutions for your savings, loans, and daily banking needs. Business Banking: Comprehensive services to support your business growth and financial management.</p>
                                </div>
                            </div>
                        </div>
                        <div className="col-lg-3 col-md-6 col-12">
                            <div className="card shadow text-center h-100">
                                <div className="card-body">
                                    <FaHandshake fontSize={50} className="bg-primary text-white rounded-5 p-1 mb-3" />
                                    <h4 className="heading">Join Us</h4>
                                    <p className="paragraph">Become a part of the Krasv Bank family and experience banking like never before. Whether you are looking to manage your personal finances or grow your business, we have the right solutions for you. Visit our branches or explore our digital banking platform to get started.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <footer className="footer">
          <p>&copy; 2024 Krasv Bank. All rights reserved.</p>
          <div className="footer-links">
            <a href="/privacy">Privacy Policy</a>
            <a href="/terms">Terms of Service</a>
          </div>
        </footer>
        </>
    );
}

export default LandingPage;