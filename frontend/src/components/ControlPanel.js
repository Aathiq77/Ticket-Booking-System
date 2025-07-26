import React, { useState } from 'react';
import { startSystem, stopSystem } from '../services/api';

function ControlPanel({ configSaved }) {
    const [startMessage, setStartMessage] = useState('');
    const [stopMessage, setStopMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleStart = async () => {
        if (!configSaved) {
            setStartMessage("Please save configuration first.");
            return;
        }

        setIsLoading(true);
        try {
            const response = await startSystem();
            setStartMessage(response.data);
            setStopMessage('');
        } catch (error) {
            if (error.response && error.response.status === 400 && error.response.data === "System is already running") {
                setStartMessage("System is already running");
            } else {
                setStartMessage('Error starting the system');
                console.error("Error starting system:", error);
            }
        } finally {
            setIsLoading(false);
        }
    };

    const handleStop = async () => {
        setIsLoading(true);
        try {
            const response = await stopSystem();
            setStopMessage(response.data);
            setStartMessage('');
        } catch (error) {
            setStopMessage('Error stopping the system');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="card mt-3">
            <div className="card-header">
                <h4>Control Panel</h4>
            </div>
            <div className="card-body">
                <button className="btn btn-success me-2" onClick={handleStart} disabled={isLoading || !configSaved}>
                    {isLoading ? 'Loading...' : 'Start System'}
                </button>
                <button className="btn btn-danger" onClick={handleStop} disabled={isLoading}>
                    {isLoading ? 'Loading...' : 'Stop System'}
                </button>
                {startMessage && <p className="mt-3">{startMessage}</p>}
                {stopMessage && <p className="mt-3">{stopMessage}</p>}
            </div>
        </div>
    );
}

export default ControlPanel;