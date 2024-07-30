import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';

function PageNotFound() {
    useEffect(() => {
        document.title = '404 - Page Not Found';
    }, []);

    return (
        <div className="d-flex justify-content-center align-items-center min-vh-100 bg-telegram">
            <div className="text-center">
                <p className="display-1 mb-0">404</p>
                <p className="display-2">Page Not Found</p>
                <p>The page you are attempting to access may have been removed or requires authentication to proceed.</p>
                <Link to="/sign-in" className="btn btn-sm btn-primary">Return to website</Link>
            </div>
        </div>
    );
}

export default PageNotFound;
