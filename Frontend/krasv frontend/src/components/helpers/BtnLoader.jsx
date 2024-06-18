import React from 'react';

function BtnLoader() {
    return (
        <button className="btn btn-sm btn-secondary" type="button" disabled>
            <span className="spinner-border spinner-border-sm me-1" aria-hidden="true" />
            <span role="status">Loading</span>
        </button>
    );
}

export default BtnLoader;
