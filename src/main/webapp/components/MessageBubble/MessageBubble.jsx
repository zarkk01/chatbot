import React from 'react';
import './MessageBubble.css';

const MessageBubble = ({ message }) => {
    const isUserMessage = message.type === 'user';
    return (
        <div className={`message-bubble ${isUserMessage ? 'user-message' : 'bot-message'}`}>
            {message.content}
        </div>
    );
};

export default MessageBubble;
