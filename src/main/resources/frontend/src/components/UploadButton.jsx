import React from 'react';
import './UploadButton.css';

const UploadButton = () => {
    const handleFileUpload = async (e) => {
    //     // const file = e.target.files[0];
    //     // if (file) {
    //         const formData = new FormData();
    //         formData.append('file', file);

            await fetch('http://localhost:8080/load', {
                method: 'POST',
                // body: formData,
            });

    };

    return (
        <div className="upload-button">
            <input type="file" onChange={handleFileUpload} />
            <button>Upload PDF</button>
        </div>
    );
};

export default UploadButton;
