import React, { useState } from 'react';
import axios from 'axios';

export default function App() {
  const [formData, setFormData] = useState({
    Open: '',
    High: '',
    Low: '',
    Close: '',
    Volume: ''
  });
  const [result, setResult] = useState(null);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: parseFloat(e.target.value)
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post('http://localhost:8080/api/predict', formData);
      setResult(res.data.prediction === 1 ? '📈 Likely to go up' : '📉 Likely to go down');
    } catch (err) {
      console.error(err);
      setResult('Error fetching prediction');
    }
  };

  return (
    <div className="max-w-xl mx-auto p-6 mt-10 bg-white rounded-lg shadow-md">
      <h1 className="text-2xl font-bold mb-4 text-center">MarketMind Predictor</h1>
      <form onSubmit={handleSubmit} className="space-y-4">
        {['Open', 'High', 'Low', 'Close', 'Volume'].map((field) => (
          <div key={field}>
            <label className="block mb-1 font-medium">{field}</label>
            <input
              type="number"
              step="any"
              name={field}
              value={formData[field]}
              onChange={handleChange}
              className="w-full border border-gray-300 rounded p-2"
              required
            />
          </div>
        ))}
        <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700">
          Predict
        </button>
      </form>
      {result && (
        <div className="mt-6 text-center text-xl font-semibold">
          Prediction: {result}
        </div>
      )}
    </div>
  );
}
