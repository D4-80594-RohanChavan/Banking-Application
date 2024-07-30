import { Routes, Route, useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import { useEffect } from 'react';
import axiosV1 from './utils/axiosV1';
import './App.css';
import ProtectedRoute from './components/ProtectedRoute';
import PageNotFound from './components/error-pages/PageNotFound';
import LandingPage from './components/landing-page/LandingPage';
import SignIn from './components/auth/SignIn';
import SignUp from './components/auth/SignUp';
import About from './components/About';
import Contact from './components/Contact';
import Terms from './components/Terms';
import Privacy from './components/Privacy';
import Layout from './components/Layout';

// Set axios authorization header if token exists in localStorage
if (localStorage.getItem('token')) {
    axiosV1.defaults.headers.common.Authorization = localStorage.getItem('token');
} else {
    delete axiosV1.defaults.headers.common.Authorization;
}

function App() {
    const navigate = useNavigate();

    useEffect(() => {
        // Axios interceptor for handling unauthorized (401) responses
        const interceptor = axiosV1.interceptors.response.use(
            (response) => response,
            (error) => {
                if (error?.response?.status === 401) {
                    // Show unauthorized alert and redirect to sign-in page
                    Swal.fire({
                        icon: 'error',
                        title: 'Unauthorized! Please sign in to access this page.',
                        showConfirmButton: false,
                        timer: 3000,
                    });
                    delete axiosV1.defaults.headers.common.Authorization; // Remove token from axios headers
                    localStorage.clear(); // Clear local storage
                    navigate('/sign-in'); // Redirect to sign-in page
                }
                return Promise.reject(error);
            }
        );

        return () => {
            // Clean up interceptor on component unmount
            axiosV1.interceptors.response.eject(interceptor);
        };
    }, [navigate]);

    return (
        <Routes>
            <Route path="/" element={<Layout />}>
                <Route index element={<LandingPage />} />
                <Route path="about" element={<About />} />
                <Route path="contact" element={<Contact />} />
                <Route path="terms" element={<Terms />} />
                <Route path="privacy" element={<Privacy />} />
                <Route path="sign-in" element={<SignIn />} />
                <Route path="sign-up" element={<SignUp />} />
                <Route path="dashboard" element={<ProtectedRoute />} />
                <Route path="*" element={<PageNotFound />} />
            </Route>
        </Routes>
    );
}

export default App;
