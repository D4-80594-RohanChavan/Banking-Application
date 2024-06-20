import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosV1 from '../utils/axiosV1';
import AdminDashboard from './admin-dashboard/AdminDashboard';
import Dashboard from './dasboard/Dashboard';

function ProtectedRoute() {
    const [token] = useState(localStorage.getItem('token'));
    const [user] = useState(JSON.parse(localStorage.getItem('user')));
    const navigate = useNavigate();

    useEffect(() => {
        if (token === null || token === '' || token !== axiosV1.defaults.headers.common.Authorization) {
            delete axiosV1.defaults.headers.common.Authorization;
            localStorage.clear();
            navigate('/sign-in');
        }
    });

    return (
        token && (
            <div>
                {user?.role === 'ROLE_ADMIN' ? <AdminDashboard /> : <Dashboard />}
            </div>
        )
    );
}

export default ProtectedRoute;
