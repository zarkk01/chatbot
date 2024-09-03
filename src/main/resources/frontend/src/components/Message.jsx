import React from 'react';
import './Message.css';

const Message = ({ text, user }) => {
    return (
        <div className={`message ${user ? 'user-message' : 'bot-message'}`}>
            {text}
        </div>
    );
};

export default Message;
