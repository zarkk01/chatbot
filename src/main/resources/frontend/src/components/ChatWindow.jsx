import React, { useState, useRef, useEffect } from 'react';
import Message from './Message';
import UploadButton from './UploadButton';
import ScrollToBottom from './ScrollToBottom';
import './ChatWindow.css';

const ChatWindow = () => {
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');
    const messagesEndRef = useRef(null);
    const chatContainerRef = useRef(null);

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

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    useEffect(() => {
        scrollToBottom(); // Scroll to bottom on initial render
    }, [messages]); // Run when messages update

    return (
        <div className="chat-window" ref={chatContainerRef}>
            <div className="chat-header">
                <img src={process.env.PUBLIC_URL + '/Vodafone.png'} alt="Vodafone Logo"/>
            </div>
            <div className="messages">
                {messages.map((msg, index) => (
                    <Message key={index} text={msg.text} user={msg.user}/>
                ))}
                <div ref={messagesEndRef} /> {/* Empty div for scroll-to-bottom */}
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
                <div className="text-under">Keep in mind that Chatbot responses may not always be relevant.</div>
            </div>
            <ScrollToBottom scrollToBottom={scrollToBottom} /> {/* Integrate ScrollToBottom */}
        </div>
    );
};

export default ChatWindow;
