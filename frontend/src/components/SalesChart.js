import React, { useState, useEffect } from 'react';
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    Legend,
    ResponsiveContainer,
    } from 'recharts';
    import { getSalesData } from '../services/api';

    function SalesChart() {
    const [salesData, setSalesData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
        try {
            const response = await getSalesData();
            const formattedData = Object.entries(response.data).map(
            ([timestamp, sales]) => ({
                time: timestamp, // Make sure this is a valid time format
                sales: sales,
            })
            );
            setSalesData(formattedData);
        } catch (error) {
            console.error('Error fetching sales data:', error);
        }
        };

        fetchData(); // Fetch initial data on mount

        const intervalId = setInterval(fetchData, 5000); // Poll every 5 seconds

        return () => clearInterval(intervalId); // Cleanup on unmount
    }, []);

    return (
        <div style={{ width: '100%', height: 300 }}>
        <ResponsiveContainer>
            <LineChart
            data={salesData}
            margin={{
                top: 5,
                right: 30,
                left: 20,
                bottom: 5,
            }}
            >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="time" /> {/* Make sure 'time' matches your data */}
            <YAxis />
            <Tooltip />
            <Legend />
            <Line
                type="monotone"
                dataKey="sales"
                stroke="#8884d8"
                activeDot={{ r: 8 }}
            />
            </LineChart>
        </ResponsiveContainer>
        </div>
    );
}

export default SalesChart;