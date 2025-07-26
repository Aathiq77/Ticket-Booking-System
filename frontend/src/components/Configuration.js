import React, { useState } from 'react';
import { saveConfiguration } from '../services/api';

function Configuration({ onConfigurationSaved }) {
    const [totalTickets, setTotalTickets] = useState('');
    const [ticketReleaseRate, setTicketReleaseRate] = useState('');
    const [customerRetrievalRate, setCustomerRetrievalRate] = useState('');
    const [maxTicketCapacity, setMaxTicketCapacity] = useState('');
    const [numVendors, setNumVendors] = useState('');
    const [numCustomers, setNumCustomers] = useState('');
    const [error, setError] = useState(''); // State for displaying error messages

    const handleSubmit = async (event) => {
        event.preventDefault();

        // Reset error state
        setError('');

        // Input Validation:
        const fields = [
        { name: 'totalTickets', value: totalTickets, label: 'Total Tickets' },
        { name: 'ticketReleaseRate', value: ticketReleaseRate, label: 'Ticket Release Rate' },
        { name: 'customerRetrievalRate', value: customerRetrievalRate, label: 'Customer Retrieval Rate' },
        { name: 'maxTicketCapacity', value: maxTicketCapacity, label: 'Max Ticket Capacity' },
        { name: 'numVendors', value: numVendors, label: 'Number of Vendors' },
        { name: 'numCustomers', value: numCustomers, label: 'Number of Customers' },
        ];

        for (const field of fields) {
        if (!field.value || field.value.trim() === '') {
            setError(`Please enter a value for ${field.label}.`);
            return;
        }

        const numValue = parseInt(field.value);

        if (isNaN(numValue)) {
            setError(`${field.label} must be a number.`);
            return;
        }

        if (numValue <= 0) {
            setError(`${field.label} must be a positive number.`);
            return;
        }
        }

        const config = {
        totalTickets: parseInt(totalTickets),
        ticketReleaseRate: parseInt(ticketReleaseRate),
        customerRetrievalRate: parseInt(customerRetrievalRate),
        maxTicketCapacity: parseInt(maxTicketCapacity),
        numVendors: parseInt(numVendors),
        numCustomers: parseInt(numCustomers),
        };

        try {
        const response = await saveConfiguration(config);
        onConfigurationSaved(true); // Notify App that config is saved successfully
        // Optionally: Display a success message here
        } catch (error) {
        console.error("Error saving configuration:", error);
        if (error.response && error.response.data) {
            setError(error.response.data); 
        } else {
            setError('Error saving configuration. Please try again.');
        }
        }
    };

    return (
        <div className="card mt-3">
        <div className="card-header">
            <h4>System Configuration</h4>
        </div>
        <div className="card-body">
            <form onSubmit={handleSubmit}>
            <div className="mb-3">
                <label htmlFor="totalTickets" className="form-label">Total Tickets:</label>
                <input
                type="text"
                className="form-control"
                id="totalTickets"
                value={totalTickets}
                onChange={(e) => setTotalTickets(e.target.value)}
                />
            </div>
            <div className="mb-3">
                <label htmlFor="ticketReleaseRate" className="form-label">Ticket Release Rate (seconds):</label>
                <input
                type="text"
                className="form-control"
                id="ticketReleaseRate"
                value={ticketReleaseRate}
                onChange={(e) => setTicketReleaseRate(e.target.value)}
                />
            </div>
            <div className="mb-3">
                <label htmlFor="customerRetrievalRate" className="form-label">Customer Retrieval Rate (seconds):</label>
                <input
                type="text"
                className="form-control"
                id="customerRetrievalRate"
                value={customerRetrievalRate}
                onChange={(e) => setCustomerRetrievalRate(e.target.value)}
                />
            </div>
            <div className="mb-3">
                <label htmlFor="maxTicketCapacity" className="form-label">Max Ticket Capacity:</label>
                <input
                type="text"
                className="form-control"
                id="maxTicketCapacity"
                value={maxTicketCapacity}
                onChange={(e) => setMaxTicketCapacity(e.target.value)}
                />
            </div>
            <div className="mb-3">
                <label htmlFor="numVendors" className="form-label">Number of Vendors:</label>
                <input
                type="text"
                className="form-control"
                id="numVendors"
                value={numVendors}
                onChange={(e) => setNumVendors(e.target.value)}
                />
            </div>
            <div className="mb-3">
                <label htmlFor="numCustomers" className="form-label">Number of Customers:</label>
                <input
                type="text"
                className="form-control"
                id="numCustomers"
                value={numCustomers}
                onChange={(e) => setNumCustomers(e.target.value)}
                />
            </div>
            {error && <div className="alert alert-danger">{error}</div>}
            <button type="submit" className="btn btn-primary">Save Configuration</button>
            </form>
        </div>
        </div>
    );
}

export default Configuration;