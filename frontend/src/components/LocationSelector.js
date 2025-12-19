import React, { useState, useEffect } from 'react';
import axios from 'axios';

const LocationSelector = ({ value, onChange, required = false }) => {
  const [provinces, setProvinces] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [sectors, setSectors] = useState([]);
  const [cells, setCells] = useState([]);

  const [selectedProvince, setSelectedProvince] = useState('');
  const [selectedDistrict, setSelectedDistrict] = useState('');
  const [selectedSector, setSelectedSector] = useState('');
  const [selectedCell, setSelectedCell] = useState('');

  const API_BASE = 'http://localhost:8080/api/locations';

  useEffect(() => {
    fetchProvinces();
  }, []);

  const fetchProvinces = async () => {
    try {
      const response = await axios.get(`${API_BASE}/provinces`);
      setProvinces(response.data);
    } catch (error) {
      console.error('Error fetching provinces:', error);
    }
  };

  const fetchDistricts = async (provinceId) => {
    try {
      const response = await axios.get(`${API_BASE}/districts?provinceId=${provinceId}`);
      setDistricts(response.data);
      setSectors([]);
      setCells([]);
    } catch (error) {
      console.error('Error fetching districts:', error);
    }
  };

  const fetchSectors = async (districtId) => {
    try {
      const response = await axios.get(`${API_BASE}/sectors?districtId=${districtId}`);
      setSectors(response.data);
      setCells([]);
    } catch (error) {
      console.error('Error fetching sectors:', error);
    }
  };

  const fetchCells = async (sectorId) => {
    try {
      const response = await axios.get(`${API_BASE}/cells?sectorId=${sectorId}`);
      setCells(response.data);
    } catch (error) {
      console.error('Error fetching cells:', error);
    }
  };

  const handleProvinceChange = (e) => {
    const provinceId = e.target.value;
    setSelectedProvince(provinceId);
    setSelectedDistrict('');
    setSelectedSector('');
    setSelectedCell('');
    setDistricts([]);
    setSectors([]);
    setCells([]);
    onChange(null);
    if (provinceId) fetchDistricts(provinceId);
  };

  const handleDistrictChange = (e) => {
    const districtId = e.target.value;
    setSelectedDistrict(districtId);
    setSelectedSector('');
    setSelectedCell('');
    setSectors([]);
    setCells([]);
    onChange(null);
    if (districtId) fetchSectors(districtId);
  };

  const handleSectorChange = (e) => {
    const sectorId = e.target.value;
    setSelectedSector(sectorId);
    setSelectedCell('');
    setCells([]);
    onChange(null);
    if (sectorId) fetchCells(sectorId);
  };

  const handleCellChange = (e) => {
    const cellId = e.target.value;
    setSelectedCell(cellId);
    onChange(cellId || null);
  };

  return (
    <div className="location-selector">
      <div className="form-group">
        <label>Province {required && <span className="text-danger">*</span>}</label>
        <select
          className="form-control"
          value={selectedProvince}
          onChange={handleProvinceChange}
          required={required}
        >
          <option value="">Select Province</option>
          {provinces.map((province) => (
            <option key={province.id} value={province.id}>
              {province.province}
            </option>
          ))}
        </select>
      </div>

      {selectedProvince && (
        <div className="form-group">
          <label>District {required && <span className="text-danger">*</span>}</label>
          <select
            className="form-control"
            value={selectedDistrict}
            onChange={handleDistrictChange}
            required={required}
          >
            <option value="">Select District</option>
            {districts.map((district) => (
              <option key={district.id} value={district.id}>
                {district.district}
              </option>
            ))}
          </select>
        </div>
      )}

      {selectedDistrict && (
        <div className="form-group">
          <label>Sector {required && <span className="text-danger">*</span>}</label>
          <select
            className="form-control"
            value={selectedSector}
            onChange={handleSectorChange}
            required={required}
          >
            <option value="">Select Sector</option>
            {sectors.map((sector) => (
              <option key={sector.id} value={sector.id}>
                {sector.sector}
              </option>
            ))}
          </select>
        </div>
      )}

      {selectedSector && (
        <div className="form-group">
          <label>Cell {required && <span className="text-danger">*</span>}</label>
          <select
            className="form-control"
            value={selectedCell}
            onChange={handleCellChange}
            required={required}
          >
            <option value="">Select Cell</option>
            {cells.map((cell) => (
              <option key={cell.id} value={cell.id}>
                {cell.cell}
              </option>
            ))}
          </select>
        </div>
      )}
    </div>
  );
};

export default LocationSelector;
