import React from 'react';
import './ScrollToBottom.css'; // Ensure CSS is imported

const ScrollToBottom = ({ scrollToBottom }) => {
    return (
        <div className="scroll-to-bottom-container">
            <button className="scroll-to-bottom" onClick={scrollToBottom}>
                ↓
            </button>
        </div>
    );
};

export default ScrollToBottom;
