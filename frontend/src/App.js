import React, { useState, useEffect } from 'react';
import './App.css';
import Configuration from './components/Configuration';
import ControlPanel from './components/ControlPanel';
import TicketAvailability from './components/TicketAvailability';
import SystemLogs from './components/SystemLogs';
import SalesChart from './components/SalesChart';
import { getConfiguration, getLogs, getTotalTickets, getAvailableTickets } from './services/api'; // Import only necessary functions

function App() {
    const [configSaved, setConfigSaved] = useState(false);

    useEffect(() => {
        const checkConfig = async () => {
        try {
            const response = await getConfiguration();
            setConfigSaved(response.data !== null);
        } catch (error) {
            console.error('Error fetching configuration:', error);
            setConfigSaved(false);
        }
        };

        checkConfig();
    }, []);

    const handleConfigurationSaved = (isSaved) => {
        setConfigSaved(isSaved);
    };

    return (
        <div className="container">
        <h1>Event Ticketing System</h1>
        <Configuration onConfigurationSaved={handleConfigurationSaved} />
        <ControlPanel configSaved={configSaved} />
        <TicketAvailability />
        <SystemLogs />
        <SalesChart />
        </div>
    );
}

export default App;