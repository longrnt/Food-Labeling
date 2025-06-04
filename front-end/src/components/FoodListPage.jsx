import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../css/FoodListPage.css';

const API_BASE = 'http://localhost:8080';

const FoodListPage = () => {
    const [foods, setFoods] = useState([]);
    const [labels, setLabels] = useState([]);
    const [selectedLabels, setSelectedLabels] = useState([]);
    const [sortBy, setSortBy] = useState('foodName');
    const [sortOrder, setSortOrder] = useState('asc');
    const [pageNumber, setPageNumber] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const pageSize = 5;

    useEffect(() => {
        axios.get(`${API_BASE}/labels/getAll/counts`)
            .then(res => setLabels(res.data))
            .catch(err => console.error('Error fetching labels:', err));
    }, []);

    useEffect(() => {
        const params = {
            labels: selectedLabels.join(','),
            pageNumber,
            pageSize,
            sortBy,
            sortOrder,
        };
        axios.get(`${API_BASE}/foods/getByLabels`, { params })
            .then(res => {
                setFoods(res.data.content);
                setTotalPages(res.data.totalPages);
            })
            .catch(err => console.error('Error fetching foods:', err));
    }, [selectedLabels, pageNumber, sortBy, sortOrder]);

    const handleLabelChange = (e) => {
        const { value, checked } = e.target;
        setSelectedLabels(prev =>
            checked ? [...prev, value] : prev.filter(label => label !== value)
        );
        setPageNumber(0);
    };

    const handleSortByChange = (e) => setSortBy(e.target.value);
    const handleSortOrderChange = (e) => setSortOrder(e.target.value);
    const handlePrevPage = () => setPageNumber(prev => Math.max(prev - 1, 0));
    const handleNextPage = () => setPageNumber(prev => Math.min(prev + 1, totalPages - 1));

    const handleLabelToggle = (foodId, labelId, isAssigned) => {
        const url = `${API_BASE}/foods/${foodId}/labels/${labelId}`;
        const method = isAssigned ? 'delete' : 'post';
        axios({ method, url })
            .then(() => {
                // Refresh the food list after updating labels
                const params = {
                    labels: selectedLabels.join(','),
                    pageNumber,
                    pageSize,
                    sortBy,
                    sortOrder,
                };
                //Re-render food table
                axios.get(`${API_BASE}/foods/getByLabels`, { params })
                    .then(res => {
                        setFoods(res.data.content);
                        setTotalPages(res.data.totalPages);
                    })
                    .catch(err => console.error('Error fetching foods:', err));

                //Re-render "Filter by Labels" checklist
                axios.get(`${API_BASE}/labels/getAll/counts`)
                    .then(res => setLabels(res.data))
                    .catch(err => console.error('Error fetching labels:', err));
            })
            .catch(err => console.error('Error updating label:', err));
    };

    return (
        <div className="container">
            <h1>Food List</h1>

            <div className="filters">
                <div className="filter-group">
                    <label><strong>Filter by Labels:</strong></label>
                    <div className="checkbox-group">
                        {labels.map(label => (
                            <label key={label.labelName}>
                                <input
                                    type="checkbox"
                                    value={label.labelName}
                                    checked={selectedLabels.includes(label.labelName)}
                                    onChange={handleLabelChange}
                                />
                                {label.labelName} ({label.foodCount})
                            </label>
                        ))}
                    </div>
                </div>

                <div className="filter-group">
                    <label><strong>Sort By:</strong></label>
                    <select value={sortBy} onChange={handleSortByChange}>
                        <option value="foodName">Name</option>
                        <option value="foodId">ID</option>
                    </select>

                    <select value={sortOrder} onChange={handleSortOrderChange}>
                        <option value="asc">Ascending</option>
                        <option value="desc">Descending</option>
                    </select>
                </div>
            </div>

            <table className="food-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Food Name</th>
                    <th>Labels</th>
                    <th>Assign/Unassign Labels</th>
                </tr>
                </thead>
                <tbody>
                {foods.map(food => (
                    <tr key={food.foodId}>
                        <td>{food.foodId}</td>
                        <td>{food.foodName}</td>
                        <td>
                            {food.labels.map((label, idx) => (
                                <span key={idx} className="label-pill">{label}</span>
                            ))}
                        </td>
                        <td>
                            {labels.map(label => {
                                const isAssigned = food.labels.includes(label.labelName);
                                return (
                                    <label key={label.labelName}>
                                        <input
                                            type="checkbox"
                                            checked={isAssigned}
                                            onChange={() => handleLabelToggle(food.foodId, label.labelId, isAssigned)}
                                        />
                                        {label.labelName}
                                    </label>
                                );
                            })}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div className="pagination">
                <button onClick={handlePrevPage} disabled={pageNumber === 0}>Previous</button>
                <span>Page {pageNumber + 1} of {totalPages}</span>
                <button onClick={handleNextPage} disabled={pageNumber + 1 >= totalPages}>Next</button>
            </div>
        </div>
    );
}

export default FoodListPage;