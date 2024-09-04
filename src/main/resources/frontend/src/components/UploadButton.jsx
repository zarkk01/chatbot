import React from 'react';
import './UploadButton.css';

const UploadButton = () => {
    const handleFileUpload = async (e) => {
        const url = window.prompt('Please enter an HTTPS URL:');
        if (url && url.startsWith('https://')) {
            try {
                const response = await fetch(`http://localhost:8080/load?file=${encodeURIComponent(url)}`, {
                    method: 'POST',
                });

                if (response.ok) {
                    console.log('File uploaded successfully.');
                } else {
                    console.error('Failed to upload the file.');
                }
            } catch (error) {
                console.error('Error occurred while uploading the file:', error);
            }
        } else {
            alert('Invalid URL. Please enter a valid HTTPS URL.');
        }
    };

    return (
        <div className="upload-button">
            <input type="file" onChange={handleFileUpload}/>
            <button onClick={handleFileUpload}>📋</button>
        </div>
    );
};

export default UploadButton;
