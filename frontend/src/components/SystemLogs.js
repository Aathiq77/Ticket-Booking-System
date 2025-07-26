import React, { useState, useEffect } from 'react';
import { getLogs } from '../services/api';

function SystemLogs() {
    const [logs, setLogs] = useState([]);

    useEffect(() => {
        const fetchLogs = async () => {
            try {
                const response = await getLogs();
                setLogs(response.data); // Update logs state with fetched data
            } catch (error) {
                console.error("Error fetching logs:", error);
            }
        };

        fetchLogs(); // Fetch logs initially

        const intervalId = setInterval(fetchLogs, 5000); // Poll every 5 seconds (adjust as needed)

        return () => clearInterval(intervalId); // Clear interval on unmount
    }, []);

    return (
        <div className="card mt-3">
            <div className="card-header">
                <h4>System Logs</h4>
            </div>
            <div className="card-body">
                <ul className="list-group">
                    {logs.map((log) => (
                        <li key={log} className="list-group-item">{log}</li>
                    ))}
                </ul>
            </div>
        </div>
    );
}

export default SystemLogs;