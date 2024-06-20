import React from 'react';
import { Outlet, useLocation } from 'react-router-dom';
import Header from './Header';

function Layout() {
    const location = useLocation();
    const hideHeaderPaths = ['/dashboard']; // Add more paths if needed

    const shouldHideHeader = hideHeaderPaths.includes(location.pathname);

    return (
        <>
            {!shouldHideHeader && <Header />}
            <main>
                <Outlet />
            </main>
        </>
    );
}

export default Layout;
