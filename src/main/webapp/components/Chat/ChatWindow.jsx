import React, { useState, useEffect, useRef } from 'react';
import MessageBubble from '../MessageBubble';
import InputBox from '../InputBox';
import './ChatWindow.css';
import { useChatAPI } from '../hooks/useChatAPI'; // Import the hook

const ChatWindow = () => {
    const [messages, setMessages] = useState([]); //store list of chat messages from user and bot
    const chatEndRef = useRef(null); //a reference to DOM element at end of chat window use for scrolling
    const { sendMessage, loading, error } = useChatAPI(); // Destructure the hook

    //keeps view scrolled to the bottom, showing most recent messages
    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    //smoothly scroll to bottom showing the most recent messages
    const scrollToBottom = () => {
        chatEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };
    //call it when a n ew message is sent
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
    //return main chat window container , scrollable area with chat messages, scroll anchor, input area
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
