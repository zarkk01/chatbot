import React, { useState, useRef, useEffect } from 'react';
import Message from './Message';
import MoreOptions from './MoreOptions';
import './ChatWindow.css';

const ChatWindow = () => {
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');
    const [isAtBottom, setIsAtBottom] = useState(true);
    const messagesEndRef = useRef(null);
    const chatContainerRef = useRef(null);

    const sendMessage = async () => {
        if (input.trim()) {
            const userMessage = { text: input, user: true };
            setMessages((prevMessages) => [...prevMessages, userMessage]);
            setInput('');

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
        const handleScroll = () => {
            if (chatContainerRef.current) {
                const { scrollTop, scrollHeight, clientHeight } = chatContainerRef.current;
                const isBottom = scrollTop + clientHeight >= scrollHeight - 10; // Adjust threshold as needed
                setIsAtBottom(isBottom);
            }
        };

        const chatContainer = chatContainerRef.current;
        if (chatContainer) {
            chatContainer.addEventListener('scroll', handleScroll);
            // Initial check to see if already at bottom
            handleScroll();
        }

        return () => {
            if (chatContainer) {
                chatContainer.removeEventListener('scroll', handleScroll);
            }
        };
    }, []);

    useEffect(() => {
        scrollToBottom(); // Scroll to bottom on messages update
    }, [messages]);

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
                <MoreOptions />

                <div className="text-under">Keep in mind that Chatbot responses may not always be relevant.</div>
            </div>
            {/*{!isAtBottom && <ScrollToBottom scrollToBottom={scrollToBottom} />} /!* Conditionally render ScrollToBottom *!/*/}
        </div>
    );
};

export default ChatWindow;
