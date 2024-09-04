import React, { useState, useEffect } from 'react';
import './Message.css';

const Message = ({ text, user }) => {
    const logoUrl = process.env.PUBLIC_URL + '/Vodafone-Symbol1.png'; // Reference the image from the public folder
    const [displayedText, setDisplayedText] = useState('');
    useEffect(() => {
        if (!user) {
            let first = true;
            let currentIndex = 0;

            const typingInterval = setInterval(() => {
                currentIndex++;
                if (first) {
                    setDisplayedText((prev) => text[0] + prev + text[currentIndex]);
                    first = false;
                } else {
                    setDisplayedText((prev) => prev + text[currentIndex]);
                }

                if (currentIndex > text.length - 2) {
                    clearInterval(typingInterval);
                }
            }, 40);

            return () => clearInterval(typingInterval);
        } else {
            setDisplayedText(text);
    }
    }, [text, user]);

    return (
        <div className="message-wrapper">
            {!user && (
                <div className="avatar-container">
                    <img
                        src={`${process.env.PUBLIC_URL}/Vodafone-Symbol1.png`}
                        alt="Bot Avatar"
                        className="avatar"
                    />
                </div>
            )}
            <div className={`message ${user ? 'user-message' : 'bot-message'}`}>
                <div className={`${user ? 'message-user-container' : 'message-container'}`}>
                    {displayedText}
                </div>
            </div>
        </div>
    );

};

export default Message;
