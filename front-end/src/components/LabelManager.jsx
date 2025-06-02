import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './LabelManager.css';

const API_BASE = 'http://localhost:6060';

const LabelManager = () => {
    const [labels, setLabels] = useState([]);
    const [newLabelName, setNewLabelName] = useState('');
    const [editingLabelId, setEditingLabelId] = useState(null);
    const [editingLabelName, setEditingLabelName] = useState('');

    useEffect(() => {
        fetchLabels();
    }, []);

    const fetchLabels = async () => {
        try {
            const response = await axios.get(`${API_BASE}/labels/getAll`);
            setLabels(response.data);
        } catch (error) {
            console.error('Error fetching labels:', error);
        }
    };

    const handleAddLabel = async () => {
        if (!newLabelName.trim()) return;
        try {
            await axios.post(`${API_BASE}/labels/add`, { labelName: newLabelName });
            setNewLabelName('');
            fetchLabels();
        } catch (error) {
            console.error('Error adding label:', error);
        }
    };

    const handleEditClick = (label) => {
        setEditingLabelId(label.labelId);
        setEditingLabelName(label.labelName);
    };

    const handleUpdateLabel = async (labelId) => {
        if (!editingLabelName.trim()) return;
        try {
            await axios.put(`${API_BASE}/labels/update/${labelId}`, { labelName: editingLabelName });
            setEditingLabelId(null);
            setEditingLabelName('');
            fetchLabels();
        } catch (error) {
            console.error('Error updating label:', error);
        }
    };

    const handleDeleteLabel = async (labelId) => {
        try {
            await axios.delete(`${API_BASE}/labels/delete/${labelId}`);
            fetchLabels();
        } catch (error) {
            console.error('Error deleting label:', error);
        }
    };

    return (
        <div className="label-manager">
            <h2>Label Management</h2>
            <div className="add-label">
                <input
                    type="text"
                    value={newLabelName}
                    onChange={(e) => setNewLabelName(e.target.value)}
                    placeholder="Enter new label name"
                />
                <button onClick={handleAddLabel}>Add Label</button>
            </div>
            <table className="label-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Label Name</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {labels.map((label) => (
                    <tr key={label.labelId}>
                        <td>{label.labelId}</td>
                        <td>
                            {editingLabelId === label.labelId ? (
                                <input
                                    type="text"
                                    value={editingLabelName}
                                    onChange={(e) => setEditingLabelName(e.target.value)}
                                />
                            ) : (
                                label.labelName
                            )}
                        </td>
                        <td>
                            {editingLabelId === label.labelId ? (
                                <>
                                    <button onClick={() => handleUpdateLabel(label.labelId)}>Save</button>
                                    <button onClick={() => setEditingLabelId(null)}>Cancel</button>
                                </>
                            ) : (
                                <>
                                    <button onClick={() => handleEditClick(label)}>Edit</button>
                                    <button onClick={() => handleDeleteLabel(label.labelId)}>Delete</button>
                                </>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default LabelManager;
