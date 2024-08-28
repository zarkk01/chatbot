import React, { useState, useEffect, useRef } from 'react';
import MessageBubble from '../MessageBubble';
import InputBox from '../InputBox';
import './ChatWindow.css';
import { useChatAPI } from '../hooks/useChatAPI'; // Import the hook

const ChatWindow = () => {
    const [messages, setMessages] = useState([]);
    const chatEndRef = useRef(null);
    const { sendMessage, loading, error } = useChatAPI(); // Destructure the hook

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const scrollToBottom = () => {
        chatEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    const handleSendMessage = async (message) => {
        const userMessage = { type: 'user', content: message };
        setMessages([...messages, userMessage]);

        // Get response from the backend API
        try {
            const response = await sendMessage(message);
            const botMessage = { type: 'bot', content: response };
            setMessages(prevMessages => [...prevMessages, botMessage]);
        } catch (err) {
            console.error('Error fetching chatbot response:', err);
        }
    };

    return (
        <div className="chat-window">
            <div className="messages-container">
                {messages.map((msg, index) => (
                    <MessageBubble key={index} message={msg} />
                ))}
                <div ref={chatEndRef} />
            </div>
            <InputBox onSendMessage={handleSendMessage} />
        </div>
    );
};

export default ChatWindow;
