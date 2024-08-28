import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import ChatPage from './pages/ChatPage';
import './App.css';

function App() {
    return (
        <Router>
            <div className="App">
                <Switch>
                    <Route path="/" component={ChatPage} />
                </Switch>
            </div>
        </Router>
    );
}

export default App;
