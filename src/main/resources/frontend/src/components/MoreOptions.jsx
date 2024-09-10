// MoreOptions.jsx
import React, { useState } from 'react';
import './MoreOptions.css';

const MoreOptions = () => {
    const [isBoxVisible, setBoxVisible] = useState(false);

    const toggleBoxVisibility = () => {
        setBoxVisible(!isBoxVisible);
    };

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

    const handleDelete = async () => {
        console.log('Delete button clicked');
        try {
            const response = await fetch('http://localhost:8080/clear', {
                method: 'POST',
            });

            if (response.ok) {
                console.log('Delete request was successful.');
            } else {
                console.error('Failed to delete.');
            }
        } catch (error) {
            console.error('Error occurred while making delete request:', error);
        }
    };

    return (
        <div className="more-options-container">
            <button onClick={toggleBoxVisibility} className="toggle-button">
                ...
            </button>
            <div className={`options-box ${isBoxVisible ? 'show-options-box' : ''}`}>
                <div className="upload-button">
                    <input type="file" onChange={handleFileUpload} />
                    <button onClick={handleFileUpload}>
                        <img
                            src={`${process.env.PUBLIC_URL}/images1.png`}
                            alt="images1"
                            className="button-icon"
                        />
                    </button>
                </div>
                <div className="delete-button">
                    <button onClick={handleDelete}>
                        <img
                            src={`${process.env.PUBLIC_URL}/bin.png`} // Update this path as needed
                            alt="bin"
                            className="delete-icon" // Apply the new class
                        />
                    </button>
                </div>
            </div>
        </div>
    );
};

export default MoreOptions;
