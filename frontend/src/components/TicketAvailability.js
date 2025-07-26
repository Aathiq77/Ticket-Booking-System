import React, { useState, useEffect } from 'react';
import { getTotalTickets, getAvailableTickets } from '../services/api';

function TicketAvailability() {
    const [totalTickets, setTotalTickets] = useState(0);
    const [availableTickets, setAvailableTickets] = useState(0);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        const fetchTickets = async () => {
            setIsLoading(true);
            try {
                const totalResponse = await getTotalTickets();
                setTotalTickets(totalResponse.data);

                const availableResponse = await getAvailableTickets();
                setAvailableTickets(availableResponse.data);
            } catch (error) {
                console.error("Error fetching ticket data:", error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchTickets();

        const intervalId = setInterval(fetchTickets, 5000); // Poll every 5 seconds

        return () => clearInterval(intervalId);
    }, []);

    const percentage = totalTickets === 0 ? 0 : (availableTickets / totalTickets) * 100;

    return (
        <div className="card mt-3">
            <div className="card-header">
                <h4>Ticket Availability</h4>
            </div>
            <div className="card-body">
                <p>Current status of available tickets.</p>
                {isLoading ? (
                    <p>Loading...</p>
                ) : (
                    <>
                        <p>Available Tickets: {availableTickets}</p>
                        <div className="progress">
                            <div
                                className="progress-bar"
                                role="progressbar"
                                style={{ width: `${percentage}%` }}
                                aria-valuenow={availableTickets}
                                aria-valuemin="0"
                                aria-valuemax={totalTickets}
                            ></div>
                        </div>
                        <p>Total Tickets: {totalTickets}</p>
                    </>
                )}
            </div>
        </div>
    );
}

export default TicketAvailability;