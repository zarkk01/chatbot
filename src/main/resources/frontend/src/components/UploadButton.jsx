import React from 'react';
import './UploadButton.css';

const UploadButton = () => {
    const handleFileUpload = async (e) => {
            console.log("blabla")
            await fetch('http://localhost:8080/load', {
                method: 'POST',
            });

    };

    return (
        <div className="upload-button">
            <input type="file" onChange={handleFileUpload}/>
            <button onClick={handleFileUpload}>Upload PDF</button>
        </div>
    );
};

export default UploadButton;
