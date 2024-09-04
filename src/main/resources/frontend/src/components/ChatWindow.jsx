import React, { useState, useEffect } from 'react';
import Message from './Message';
import UploadButton from './UploadButton';
import './ChatWindow.css';

const ChatWindow = () => {
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');

    const sendMessage = async () => {
        if (input.trim()) {
            setInput('');
            const userMessage = { text: input, user: true };
            setMessages((prevMessages) => [...prevMessages, userMessage]);

            const response = await fetch(`http://localhost:8080/chat?query=${encodeURIComponent(input)}`);
            const data = await response.text();

            const botMessage = { text: data, user: false };
            setMessages((prevMessages) => [...prevMessages, botMessage]);
        }
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') sendMessage();
    };

    return (
        <div className="chat-window">
            <div className="chat-header">
                <img src={process.env.PUBLIC_URL + '/Vodafone.png'} alt="Vodafone Logo"/>
            </div>
            <div className="messages">
                {messages.map((msg, index) => (
                    <Message key={index} text={msg.text} user={msg.user}/>
                ))}
            </div>
            <div className="input-container">
                <input
                    type="text"
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyPress={handleKeyPress}
                    placeholder="Type your query here..."
                    className="chat-input"
                />
                <button onClick={sendMessage} className="send-button">→</button>
                <UploadButton/>
                <div className={"text-under"}>Be careful. Vodafone can response BS.</div>
            </div>
        </div>
    );

};

export default ChatWindow;
