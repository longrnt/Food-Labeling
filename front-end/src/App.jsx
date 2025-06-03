import FoodListPage from "./components/FoodListPage.jsx";
import LabelManagerPage from "./components/LabelManagerPage.jsx";
import './css/App.css';
import {useState} from "react";

function App() {

    const [activeTab, setActiveTab] = useState('foods');

    return (
        <div className="admin-dashboard">
            <div className="tab-buttons">
                <button
                    className={activeTab === 'foods' ? 'active' : ''}
                    onClick={() => setActiveTab('foods')}
                >
                    Food Management
                </button>
                <button
                    className={activeTab === 'labels' ? 'active' : ''}
                    onClick={() => setActiveTab('labels')}
                >
                    Label Management
                </button>
            </div>

            <div className="tab-content">
                {activeTab === 'foods' && <FoodListPage />}
                {activeTab === 'labels' && <LabelManagerPage />}
            </div>
        </div>
    );
}

export default App
