import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api'; // Correct base URL

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Add a global error handler (optional)
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (!error.response) {
            // Network error (e.g., server down)
            const networkError = new Error("Network error. Please check your connection.");
            console.error("Network Error:", networkError);
            return Promise.reject(networkError);
        } else {
            // Other errors (e.g., 404, 500)
            console.error("API Error:", error.response.status, error.response.data);

            // Log more details about the error
            if (error.config) {
                console.error("Request URL:", error.config.url);
                console.error("Request Method:", error.config.method);
                console.error("Request Data:", error.config.data); // Log request data if available
            }

            return Promise.reject(error); // Pass the original error along
        }
    }
);
export const saveConfiguration = (config) => {
    return api.post('/config', config)
        .catch(error => {
            console.error("Error saving configuration:", error);
            throw error; // Re-throw to be handled by the caller
        });
};

export const startSystem = () => {
    return api.post('/start')
        .catch(error => {
            console.error("Error starting system:", error);
            throw error;
        });
};

export const stopSystem = () => {
    return api.post('/stop')
        .catch(error => {
            console.error("Error stopping system:", error);
            throw error;
        });
};

export const getTotalTickets = () => {
    return api.get('/tickets/total')
        .catch(error => {
            console.error("Error getting total tickets:", error);
            throw error;
        });
};

export const getAvailableTickets = () => {
    return api.get('/tickets/available')

};

export const getConfiguration = () => {
    return api.get('/config')
        .catch(error => {
            console.error("Error getting configuration:", error);
            throw error;
        });
};

export const getLogs = () => {
    return api.get('/logs');
};

export const getSalesData = () => api.get('/sales');