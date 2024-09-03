import React, { useState, useEffect } from 'react';
import Message from './Message';
import UploadButton from './UploadButton';
import './ChatWindow.css';

const ChatWindow = () => {
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');

    const sendMessage = async () => {
        if (input.trim()) {
            setMessages([...messages, { text: input, user: true }]);

            const response = await fetch(`http://localhost:8080/chat?query=${encodeURIComponent(input)}`);
            const data = await response.text();

            setMessages([...messages, { text: input, user: true }, { text: data, user: false }]);
            setInput('');
        }
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') sendMessage();
    };

    return (
        <div className="chat-window">
            <div className="messages">
                {messages.map((msg, index) => (
                    <Message key={index} text={msg.text} user={msg.user} />
                ))}
            </div>
            <div className="input-area">
                <input
                    type="text"
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyPress={handleKeyPress}
                    placeholder="Type your query here..."
                />
                <button onClick={sendMessage}>Send</button>
            </div>
            <UploadButton />
        </div>
    );
};

export default ChatWindow;
