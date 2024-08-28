import { useState } from 'react';
import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export const useChatAPI = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const sendMessage = async (query) => {
        setLoading(true);
        try {
            const response = await axios.get(`${API_URL}/chat`, { params: { query } });
            return response.data;
        } catch (err) {
            setError(err);
            console.error('Failed to send message:', err);
        } finally {
            setLoading(false);
        }
    };

    return { sendMessage, loading, error };
};
