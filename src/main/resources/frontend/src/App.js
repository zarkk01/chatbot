import React from 'react';
import ChatWindow from './components/ChatWindow';
import { Scrollbars } from 'react-custom-scrollbars-2';
import './App.css';

function App() {
    return (
        <div className="App">
            <Scrollbars style={{ width: '100%', height: '100%' }}>
                <ChatWindow />
            </Scrollbars>
        </div>
    );
}

export default App;
