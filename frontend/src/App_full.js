import React, { useState, useEffect } from 'react';
import { Mail, Lock, User, Phone, Eye, EyeOff, CheckCircle, AlertCircle, Loader, ArrowRight, Shield, LogOut, Home, Package, Users, ShoppingCart, BarChart3, Menu, X, Search, ChevronLeft, ChevronRight, TrendingUp, Activity, FileText, UserCircle, DollarSign, Calendar } from 'lucide-react';
import CustomerApp from './CustomerApp';
import Pagination from './components/Pagination';
import Card from './components/Card';
import Button from './components/Button';
import StatCard from './components/StatCard';
import Badge from './components/Badge';
import Alert from './components/Alert';
import SimpleBarChart from './components/SimpleBarChart';
import SimplePieChart from './components/SimplePieChart';

// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';

// Utility function for API calls
const apiCall = async (endpoint, options = {}) => {
  try {
    const token = localStorage.getItem('token');
    const headers = {
      'Content-Type': 'application/json',
      ...(token && { 'Authorization': `Bearer ${token}` }),
      ...options.headers,
    };

    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      ...options,
      headers,
    });

    const data = await response.json();
    
    if (!response.ok) {
      throw new Error(data.message || 'Something went wrong');
    }
    
    return data;
  } catch (error) {
    throw error;
  }
};

// Utility Functions for Export and Print
const exportToCSV = (data, filename) => {
  if (!data || data.length === 0) {
    alert('No data to export');
    return;
  }

  const headers = Object.keys(data[0]).filter(key => typeof data[0][key] !== 'object');
  const csvContent = [
    headers.join(','),
    ...data.map(row => 
      headers.map(header => {
        const value = row[header];
        return typeof value === 'string' && value.includes(',') ? `"${value}"` : value;
      }).join(',')
    )
  ].join('\n');

  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
  const link = document.createElement('a');
  link.href = URL.createObjectURL(blob);
  link.download = `${filename}_${new Date().toISOString().split('T')[0]}.csv`;
  link.click();
};

const printSalesReceipt = (sales) => {
  if (!sales || sales.length === 0) {
    alert('No sales to print');
    return;
  }

  const printWindow = window.open('', '', 'width=800,height=600');
  const total = sales.reduce((sum, sale) => sum + (sale.totalPrice || 0), 0);
  
  printWindow.document.write(`
    <html>
      <head>
        <title>Sales Receipt</title>
        <style>
          body { font-family: Arial, sans-serif; padding: 20px; }
          h1 { text-align: center; color: #333; }
          table { width: 100%; border-collapse: collapse; margin-top: 20px; }
          th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
          th { background-color: #4CAF50; color: white; }
          .total { font-weight: bold; font-size: 18px; text-align: right; margin-top: 20px; }
          .footer { text-align: center; margin-top: 30px; color: #666; }
        </style>
      </head>
      <body>
        <h1>Pharmacy Management System</h1>
        <h2>Sales Receipt</h2>
        <p><strong>Date:</strong> ${new Date().toLocaleDateString()}</p>
        <table>
          <thead>
            <tr>
              <th>Date</th>
              <th>Medicine</th>
              <th>Customer</th>
              <th>Quantity</th>
              <th>Total (RWF)</th>
            </tr>
          </thead>
          <tbody>
            ${sales.map(sale => `
              <tr>
                <td>${new Date(sale.saleDate).toLocaleDateString()}</td>
                <td>${sale.medicine?.name || 'N/A'}</td>
                <td>${sale.customerName || 'Walk-in'}</td>
                <td>${sale.quantity}</td>
                <td>${sale.totalPrice.toFixed(2)}</td>
              </tr>
            `).join('')}
          </tbody>
        </table>
        <div class="total">Total: RWF ${total.toFixed(2)}</div>
        <div class="footer">
          <p>Thank you for your business!</p>
          <p>Printed on ${new Date().toLocaleString()}</p>
        </div>
      </body>
    </html>
  `);
  
  printWindow.document.close();
  printWindow.print();
};

// Login Page Component
const LoginPage = ({ onLogin, onSwitchToSignup, onSwitchToForgotPassword }) => {
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await apiCall('/auth/login', {
        method: 'POST',
        body: JSON.stringify(formData),
      });

      if (response.requiresOTP) {
        onLogin(response, 'otp');
      } else {
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(response));
        onLogin(response, 'dashboard');
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen relative overflow-hidden flex items-center justify-center p-4"
      style={{
        backgroundImage: 'url("https://images.unsplash.com/photo-1587854692152-cbe660dbde88?w=1920&q=80")',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundAttachment: 'fixed'
      }}
    >
      {/* Animated Background Overlay */}
      <div className="absolute inset-0 bg-gradient-to-br from-cyan-600/40 via-blue-600/30 to-indigo-600/40 backdrop-blur-sm animate-pulse"></div>
      
      {/* Floating Particles Animation */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute w-96 h-96 bg-cyan-400/20 rounded-full blur-3xl animate-blob animation-delay-2000 top-0 left-0"></div>
        <div className="absolute w-96 h-96 bg-blue-400/20 rounded-full blur-3xl animate-blob top-0 right-0"></div>
        <div className="absolute w-96 h-96 bg-indigo-400/20 rounded-full blur-3xl animate-blob animation-delay-4000 bottom-0 left-1/2"></div>
      </div>

      <div className="w-full max-w-md relative z-10 animate-fadeInUp">
        {/* Glassmorphism Card */}
        <div className="bg-white/20 backdrop-blur-2xl rounded-3xl shadow-2xl border border-white/30 p-8 space-y-6 hover:shadow-cyan-500/20 transition-all duration-500">
          {/* Logo & Title with Animation */}
          <div className="text-center space-y-3 animate-slideDown">
            <div className="inline-flex items-center justify-center w-20 h-20 bg-gradient-to-br from-cyan-400 via-blue-500 to-indigo-600 rounded-full mb-4 shadow-2xl shadow-cyan-500/50 animate-bounce-slow">
              <Shield className="w-10 h-10 text-white drop-shadow-lg" />
            </div>
            <h1 className="text-4xl font-bold text-white drop-shadow-2xl animate-glow">
              Staff Login
            </h1>
            <p className="text-white/90 font-medium drop-shadow-lg">Welcome back! Please login to your account</p>
          </div>

          {/* Error Message */}
          {error && (
            <div className="bg-red-500/20 backdrop-blur-xl border border-red-300/50 rounded-2xl p-4 flex items-start gap-3 animate-shake">
              <AlertCircle className="w-5 h-5 text-red-100 flex-shrink-0 mt-0.5" />
              <p className="text-sm text-red-50 font-medium">{error}</p>
            </div>
          )}

          {/* Login Form */}
          <form onSubmit={handleSubmit} className="space-y-5">
            <div className="animate-slideInLeft">
              <label className="block text-sm font-bold text-white/90 mb-2 drop-shadow-lg">Email Address</label>
              <div className="relative group">
                <Mail className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-white/70 group-focus-within:text-cyan-300 transition-colors z-10" />
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full pl-12 pr-4 py-4 bg-white/10 backdrop-blur-xl border-2 border-white/30 rounded-2xl text-white placeholder:text-white/50 focus:bg-white/20 focus:border-cyan-400 focus:ring-4 focus:ring-cyan-400/30 transition-all duration-300 shadow-lg"
                  placeholder="staff@pharmacy.com"
                  required
                />
              </div>
            </div>

            <div className="animate-slideInRight">
              <label className="block text-sm font-bold text-white/90 mb-2 drop-shadow-lg">Password</label>
              <div className="relative group">
                <Lock className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-white/70 group-focus-within:text-cyan-300 transition-colors z-10" />
                <input
                  type={showPassword ? 'text' : 'password'}
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                  className="w-full pl-12 pr-14 py-4 bg-white/10 backdrop-blur-xl border-2 border-white/30 rounded-2xl text-white placeholder:text-white/50 focus:bg-white/20 focus:border-cyan-400 focus:ring-4 focus:ring-cyan-400/30 transition-all duration-300 shadow-lg"
                  placeholder="••••••••"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-4 top-1/2 -translate-y-1/2 text-white/70 hover:text-cyan-300 transition-colors z-10"
                >
                  {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
                </button>
              </div>
            </div>

            <div className="flex items-center justify-between text-sm animate-fadeIn">
              <label className="flex items-center gap-2 cursor-pointer group">
                <input type="checkbox" className="w-4 h-4 rounded border-2 border-white/50 bg-white/10 text-cyan-500 focus:ring-2 focus:ring-cyan-400/50 transition-all" />
                <span className="text-white/90 font-medium group-hover:text-white transition-colors">Remember me</span>
              </label>
              <button
                type="button"
                onClick={onSwitchToForgotPassword}
                className="text-cyan-200 hover:text-white font-bold transition-colors"
              >
                Forgot password?
              </button>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-gradient-to-r from-cyan-500 via-blue-600 to-indigo-600 text-white py-4 rounded-2xl font-bold text-lg hover:from-cyan-400 hover:via-blue-500 hover:to-indigo-500 hover:shadow-2xl hover:shadow-cyan-500/50 hover:scale-105 active:scale-95 transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:scale-100 flex items-center justify-center gap-3 shadow-xl animate-pulse-slow"
            >
              {loading ? (
                <>
                  <Loader className="w-6 h-6 animate-spin" />
                  Signing in...
                </>
              ) : (
                <>
                  <Shield className="w-5 h-5" />
                  Sign In
                  <ArrowRight className="w-5 h-5" />
                </>
              )}
            </button>
          </form>

          {/* Sign Up Link */}
          <div className="text-center text-sm animate-fadeIn">
            <p className="text-white/80 font-medium">
              Don't have an account?{' '}
              <button onClick={onSwitchToSignup} className="text-cyan-200 hover:text-white font-bold transition-colors underline decoration-2 underline-offset-4">
                Sign up
              </button>
            </p>
          </div>

          {/* Footer Text */}
          <div className="text-center pt-4 border-t border-white/20">
            <p className="text-xs text-white/60">
              🏥 Pharmacy Management System v2.0
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

// Signup Page Component
const SignupPage = ({ onSignup, onSwitchToLogin }) => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
    role: 'CASHIER',
    locationId: '',
  });
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  
  // Location states
  const [provinces, setProvinces] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [sectors, setSectors] = useState([]);
  const [cells, setCells] = useState([]);
  const [villages, setVillages] = useState([]);
  const [selectedProvince, setSelectedProvince] = useState('');
  const [selectedDistrict, setSelectedDistrict] = useState('');
  const [selectedSector, setSelectedSector] = useState('');
  const [selectedCell, setSelectedCell] = useState('');
  const [loadingLocations, setLoadingLocations] = useState(false);

  // Load provinces on component mount
  useEffect(() => {
    const loadProvinces = async () => {
      try {
        const response = await apiCall('/locations/provinces');
        setProvinces(response);
      } catch (err) {
        console.error('Failed to load provinces:', err);
      }
    };
    loadProvinces();
  }, []);

  // Load districts when province changes
  const handleProvinceChange = async (provinceId) => {
    setSelectedProvince(provinceId);
    setSelectedDistrict('');
    setSelectedSector('');
    setSelectedCell('');
    setFormData({ ...formData, locationId: '' });
    setDistricts([]);
    setSectors([]);
    setCells([]);
    setVillages([]);

    if (provinceId) {
      setLoadingLocations(true);
      try {
        const response = await apiCall(`/locations/districts?provinceId=${provinceId}`);
        setDistricts(response);
      } catch (err) {
        console.error('Failed to load districts:', err);
      } finally {
        setLoadingLocations(false);
      }
    }
  };

  // Load sectors when district changes
  const handleDistrictChange = async (districtId) => {
    setSelectedDistrict(districtId);
    setSelectedSector('');
    setSelectedCell('');
    setFormData({ ...formData, locationId: '' });
    setSectors([]);
    setCells([]);
    setVillages([]);

    if (districtId) {
      setLoadingLocations(true);
      try {
        const response = await apiCall(`/locations/sectors?districtId=${districtId}`);
        setSectors(response);
      } catch (err) {
        console.error('Failed to load sectors:', err);
      } finally {
        setLoadingLocations(false);
      }
    }
  };

  // Load cells when sector changes
  const handleSectorChange = async (sectorId) => {
    setSelectedSector(sectorId);
    setSelectedCell('');
    setFormData({ ...formData, locationId: '' });
    setCells([]);
    setVillages([]);

    if (sectorId) {
      setLoadingLocations(true);
      try {
        const response = await apiCall(`/locations/cells?sectorId=${sectorId}`);
        setCells(response);
      } catch (err) {
        console.error('Failed to load cells:', err);
      } finally {
        setLoadingLocations(false);
      }
    }
  };

  // Load villages when cell changes
  const handleCellChange = async (cellId) => {
    setSelectedCell(cellId);
    setFormData({ ...formData, locationId: '' });
    setVillages([]);

    if (cellId) {
      setLoadingLocations(true);
      try {
        const response = await apiCall(`/locations/villages?cellId=${cellId}`);
        setVillages(response);
      } catch (err) {
        console.error('Failed to load villages:', err);
      } finally {
        setLoadingLocations(false);
      }
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    if (formData.password.length < 8) {
      setError('Password must be at least 8 characters');
      return;
    }

    if (!formData.locationId) {
      setError('Please select your complete location');
      return;
    }

    setLoading(true);

    try {
      const { confirmPassword, ...signupData } = formData;
      const response = await apiCall('/auth/signup', {
        method: 'POST',
        body: JSON.stringify(signupData),
      });

      onSignup(response);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-green-50 via-white to-blue-50 flex items-center justify-center p-4">
      <div className="w-full max-w-2xl">
        <div className="bg-white rounded-2xl shadow-2xl p-8 space-y-6">
          {/* Logo & Title */}
          <div className="text-center space-y-2">
            <div className="inline-flex items-center justify-center w-16 h-16 bg-gradient-to-r from-green-500 to-blue-500 rounded-full mb-4">
              <User className="w-8 h-8 text-white" />
            </div>
            <h1 className="text-3xl font-bold bg-gradient-to-r from-green-600 to-blue-600 bg-clip-text text-transparent">
              Create Account
            </h1>
            <p className="text-gray-600">Join our pharmacy management system</p>
          </div>

          {/* Error Message */}
          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-3 flex items-start gap-2">
              <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0 mt-0.5" />
              <p className="text-sm text-red-700">{error}</p>
            </div>
          )}

          {/* Signup Form */}
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">First Name</label>
                <input
                  type="text"
                  value={formData.firstName}
                  onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition"
                  placeholder="John"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Last Name</label>
                <input
                  type="text"
                  value={formData.lastName}
                  onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition"
                  placeholder="Doe"
                  required
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Email</label>
              <div className="relative">
                <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition"
                  placeholder="john@example.com"
                  required
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Phone</label>
              <div className="relative">
                <Phone className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="tel"
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition"
                  placeholder="0781234567"
                  pattern="07[0-9]{8}"
                  required
                />
              </div>
              <p className="text-xs text-gray-500 mt-1">Format: 07XXXXXXXX</p>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Role</label>
              <select
                value={formData.role}
                onChange={(e) => setFormData({ ...formData, role: e.target.value })}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition"
              >
                <option value="CASHIER">Cashier</option>
                <option value="PHARMACIST">Pharmacist</option>
                <option value="INVENTORY_MANAGER">Inventory Manager</option>
                <option value="DOCTOR">Doctor</option>
              </select>
            </div>

            {/* Location Selection */}
            <div className="space-y-4">
              <h3 className="text-lg font-semibold text-gray-900">Location</h3>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Province</label>
                  <select
                    value={selectedProvince}
                    onChange={(e) => handleProvinceChange(e.target.value)}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition"
                    required
                  >
                    <option value="">Select Province</option>
                    {provinces.map((province) => (
                      <option key={province.id} value={province.id}>
                        {province.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">District</label>
                  <select
                    value={selectedDistrict}
                    onChange={(e) => handleDistrictChange(e.target.value)}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition"
                    disabled={!selectedProvince || loadingLocations}
                    required
                  >
                    <option value="">Select District</option>
                    {districts.map((district) => (
                      <option key={district.id} value={district.id}>
                        {district.name}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Sector</label>
                  <select
                    value={selectedSector}
                    onChange={(e) => handleSectorChange(e.target.value)}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition"
                    disabled={!selectedDistrict || loadingLocations}
                    required
                  >
                    <option value="">Select Sector</option>
                    {sectors.map((sector) => (
                      <option key={sector.id} value={sector.id}>
                        {sector.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Cell</label>
                  <select
                    value={selectedCell}
                    onChange={(e) => handleCellChange(e.target.value)}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition"
                    disabled={!selectedSector || loadingLocations}
                    required
                  >
                    <option value="">Select Cell</option>
                    {cells.map((cell) => (
                      <option key={cell.id} value={cell.id}>
                        {cell.name}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Village</label>
                <select
                  value={formData.locationId}
                  onChange={(e) => setFormData({ ...formData, locationId: e.target.value })}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition"
                  disabled={!selectedCell || loadingLocations}
                  required
                >
                  <option value="">Select Village</option>
                  {villages.map((village) => (
                    <option key={village.id} value={village.id}>
                      {village.name}
                    </option>
                  ))}
                </select>
              </div>

              {loadingLocations && (
                <div className="flex items-center gap-2 text-sm text-gray-600">
                  <Loader className="w-4 h-4 animate-spin" />
                  Loading locations...
                </div>
              )}
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Password</label>
                <div className="relative">
                  <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                  <input
                    type={showPassword ? 'text' : 'password'}
                    value={formData.password}
                    onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                    className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition"
                    placeholder="••••••••"
                    required
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Confirm Password</label>
                <div className="relative">
                  <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                  <input
                    type={showPassword ? 'text' : 'password'}
                    value={formData.confirmPassword}
                    onChange={(e) => setFormData({ ...formData, confirmPassword: e.target.value })}
                    className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition"
                    placeholder="••••••••"
                    required
                  />
                </div>
              </div>
            </div>

            <div className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={showPassword}
                onChange={() => setShowPassword(!showPassword)}
                className="rounded border-gray-300 text-green-600 focus:ring-green-500"
              />
              <label className="text-sm text-gray-600">Show password</label>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-gradient-to-r from-green-500 to-blue-500 text-white py-3 rounded-lg font-semibold hover:from-green-600 hover:to-blue-600 transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
            >
              {loading ? (
                <>
                  <Loader className="w-5 h-5 animate-spin" />
                  Creating account...
                </>
              ) : (
                <>
                  Create Account
                  <ArrowRight className="w-5 h-5" />
                </>
              )}
            </button>
          </form>

          {/* Login Link */}
          <div className="text-center text-sm text-gray-600">
            Already have an account?{' '}
            <button onClick={onSwitchToLogin} className="text-green-600 hover:text-green-700 font-semibold">
              Sign in
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

// OTP Verification Page
const OTPPage = ({ email, onVerify, onResend }) => {
  const [otp, setOtp] = useState(['', '', '', '', '', '']);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [resendTimer, setResendTimer] = useState(60);

  useEffect(() => {
    if (resendTimer > 0) {
      const timer = setTimeout(() => setResendTimer(resendTimer - 1), 1000);
      return () => clearTimeout(timer);
    }
  }, [resendTimer]);

  const handleChange = (index, value) => {
    if (value.length > 1) return;
    const newOtp = [...otp];
    newOtp[index] = value;
    setOtp(newOtp);

    if (value && index < 5) {
      document.getElementById(`otp-${index + 1}`)?.focus();
    }
  };

  const handleKeyDown = (index, e) => {
    if (e.key === 'Backspace' && !otp[index] && index > 0) {
      document.getElementById(`otp-${index - 1}`)?.focus();
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const code = otp.join('');
    
    if (code.length !== 6) {
      setError('Please enter all 6 digits');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await apiCall('/auth/verify-otp', {
        method: 'POST',
        body: JSON.stringify({ email, code }),
      });

      localStorage.setItem('token', response.token);
      localStorage.setItem('user', JSON.stringify(response));
      onVerify(response);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleResend = async () => {
    try {
      await apiCall('/auth/resend-otp', {
        method: 'POST',
        body: JSON.stringify({ email }),
      });
      setResendTimer(60);
      setError('');
      alert('OTP sent successfully!');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-50 via-white to-blue-50 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <div className="bg-white rounded-2xl shadow-2xl p-8 space-y-6">
          {/* Icon & Title */}
          <div className="text-center space-y-2">
            <div className="inline-flex items-center justify-center w-16 h-16 bg-gradient-to-r from-purple-500 to-blue-500 rounded-full mb-4">
              <Mail className="w-8 h-8 text-white" />
            </div>
            <h1 className="text-3xl font-bold bg-gradient-to-r from-purple-600 to-blue-600 bg-clip-text text-transparent">
              Verify Your Email
            </h1>
            <p className="text-gray-600">
              We've sent a 6-digit code to<br />
              <span className="font-semibold text-gray-900">{email}</span>
            </p>
          </div>

          {/* Error Message */}
          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-3 flex items-start gap-2">
              <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0 mt-0.5" />
              <p className="text-sm text-red-700">{error}</p>
            </div>
          )}

          {/* OTP Form */}
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="flex justify-center gap-2">
              {otp.map((digit, index) => (
                <input
                  key={index}
                  id={`otp-${index}`}
                  type="text"
                  maxLength={1}
                  value={digit}
                  onChange={(e) => handleChange(index, e.target.value)}
                  onKeyDown={(e) => handleKeyDown(index, e)}
                  className="w-12 h-14 text-center text-2xl font-bold border-2 border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent transition"
                  required
                />
              ))}
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-gradient-to-r from-purple-500 to-blue-500 text-white py-3 rounded-lg font-semibold hover:from-purple-600 hover:to-blue-600 transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
            >
              {loading ? (
                <>
                  <Loader className="w-5 h-5 animate-spin" />
                  Verifying...
                </>
              ) : (
                'Verify Email'
              )}
            </button>
          </form>

          {/* Resend */}
          <div className="text-center text-sm text-gray-600">
            {resendTimer > 0 ? (
              <p>Resend code in {resendTimer}s</p>
            ) : (
              <button onClick={handleResend} className="text-purple-600 hover:text-purple-700 font-semibold">
                Resend OTP
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

// Forgot Password Page
const ForgotPasswordPage = ({ onReset, onSwitchToLogin }) => {
  const API_BASE_URL = 'http://localhost:8080/api';
  const [step, setStep] = useState(1); // 1: email, 2: otp, 3: new password
  const [email, setEmail] = useState('');
  const [otp, setOtp] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [showPassword, setShowPassword] = useState(false);

  const handleRequestOTP = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      await fetch(`${API_BASE_URL}/auth/forgot-password?email=${encodeURIComponent(email)}`, {
        method: 'POST'
      });
      setStep(2);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
    
    if (newPassword !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await apiCall('/auth/reset-password', {
        method: 'POST',
        body: JSON.stringify({ email, otp, newPassword }),
      });
      alert('Password reset successful! Please login with your new password.');
      onReset();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-orange-50 via-white to-red-50 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <div className="bg-white rounded-2xl shadow-2xl p-8 space-y-6">
          {/* Icon & Title */}
          <div className="text-center space-y-2">
            <div className="inline-flex items-center justify-center w-16 h-16 bg-gradient-to-r from-orange-500 to-red-500 rounded-full mb-4">
              <Lock className="w-8 h-8 text-white" />
            </div>
            <h1 className="text-3xl font-bold bg-gradient-to-r from-orange-600 to-red-600 bg-clip-text text-transparent">
              Reset Password
            </h1>
            <p className="text-gray-600">
              {step === 1 && "Enter your email to receive OTP"}
              {step === 2 && "Enter the OTP and new password"}
            </p>
          </div>

          {/* Error Message */}
          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-3 flex items-start gap-2">
              <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0 mt-0.5" />
              <p className="text-sm text-red-700">{error}</p>
            </div>
          )}

          {/* Step 1: Email */}
          {step === 1 && (
            <form onSubmit={handleRequestOTP} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Email</label>
                <div className="relative">
                  <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                  <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent transition"
                    placeholder="your@email.com"
                    required
                  />
                </div>
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full bg-gradient-to-r from-orange-500 to-red-500 text-white py-3 rounded-lg font-semibold hover:from-orange-600 hover:to-red-600 transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
              >
                {loading ? (
                  <>
                    <Loader className="w-5 h-5 animate-spin" />
                    Sending...
                  </>
                ) : (
                  'Send OTP'
                )}
              </button>
            </form>
          )}

          {/* Step 2: OTP & New Password */}
          {step === 2 && (
            <form onSubmit={handleResetPassword} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">OTP Code</label>
                <input
                  type="text"
                  value={otp}
                  onChange={(e) => setOtp(e.target.value)}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent transition text-center text-xl font-mono"
                  placeholder="000000"
                  maxLength={6}
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">New Password</label>
                <div className="relative">
                  <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                  <input
                    type={showPassword ? 'text' : 'password'}
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    className="w-full pl-10 pr-12 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent transition"
                    placeholder="••••••••"
                    minLength={8}
                    required
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
                  >
                    {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
                  </button>
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Confirm Password</label>
                <div className="relative">
                  <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                  <input
                    type={showPassword ? 'text' : 'password'}
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent transition"
                    placeholder="••••••••"
                    required
                  />
                </div>
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full bg-gradient-to-r from-orange-500 to-red-500 text-white py-3 rounded-lg font-semibold hover:from-orange-600 hover:to-red-600 transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
              >
                {loading ? (
                  <>
                    <Loader className="w-5 h-5 animate-spin" />
                    Resetting...
                  </>
                ) : (
                  'Reset Password'
                )}
              </button>
            </form>
          )}

          {/* Back to Login */}
          <div className="text-center text-sm text-gray-600">
            <button onClick={onSwitchToLogin} className="text-orange-600 hover:text-orange-700 font-semibold">
              Back to Login
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

// Dashboard Component with Pagination
const Dashboard = ({ user, onLogout }) => {
  const [currentPage, setCurrentPage] = useState('dashboard');
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [stats, setStats] = useState({});
  const [users, setUsers] = useState([]);
  const [medicines, setMedicines] = useState([]);
  const [sales, setSales] = useState([]);
  const [agents, setAgents] = useState([]);
  const [companies, setCompanies] = useState([]);
  const [pagination, setPagination] = useState({
    pageNumber: 0,
    pageSize: 5,
    totalPages: 0,
    totalElements: 0,
  });
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);
  const [analyticsData, setAnalyticsData] = useState({
    salesByCategory: [],
    salesTrend: [],
    topMedicines: [],
    revenueByMonth: []
  });
  
  // Modal states
  const [showAddMedicineModal, setShowAddMedicineModal] = useState(false);
  const [showAddUserModal, setShowAddUserModal] = useState(false);
  const [showNewSaleModal, setShowNewSaleModal] = useState(false);
  const [showAddAgentModal, setShowAddAgentModal] = useState(false);
  const [showAddCompanyModal, setShowAddCompanyModal] = useState(false);

  // Fetch Dashboard Stats
  useEffect(() => {
    const fetchStats = async () => {
      try {
        const medicineStats = await apiCall('/medicines/stats');
        setStats({
          totalUsers: 0,
          active: 0,
          totalMedicines: medicineStats.totalMedicines || 0,
          lowStockMedicines: medicineStats.lowStockMedicines || 0,
          totalSales: 0,
          totalRevenue: 0,
          todayRevenue: 0,
          ...medicineStats
        });
      } catch (err) {
        console.error('Failed to fetch stats:', err);
        setStats({
          totalUsers: 0,
          active: 0,
          totalMedicines: 0,
          lowStockMedicines: 0,
          totalSales: 0,
          totalRevenue: 0,
          todayRevenue: 0
        });
      }
    };
    fetchStats();
  }, []);

  // Fetch data based on current page
  useEffect(() => {
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage, pagination.pageNumber, searchTerm]);

  const fetchData = async () => {
    setLoading(true);
    try {
      let response;
      if (currentPage === 'users') {
        response = await apiCall('/users');
        const data = Array.isArray(response) ? response : (response.content || []);
        setUsers(data);
        setPagination(prev => ({ ...prev, totalPages: Math.ceil(data.length / prev.pageSize), totalElements: data.length }));
      } else if (currentPage === 'medicines') {
        response = await apiCall('/medicines');
        console.log('Medicines response:', response);
        const data = Array.isArray(response) ? response : (response.content || []);
        setMedicines(data);
        setPagination(prev => ({ ...prev, totalPages: Math.ceil(data.length / prev.pageSize), totalElements: data.length }));
      } else if (currentPage === 'sales') {
        response = await apiCall('/sales');
        const data = Array.isArray(response) ? response : (response.content || []);
        setSales(data);
        setPagination(prev => ({ ...prev, totalPages: Math.ceil(data.length / prev.pageSize), totalElements: data.length }));
      } else if (currentPage === 'agents') {
        response = await apiCall('/agents');
        const data = Array.isArray(response) ? response : (response.content || []);
        setAgents(data);
        setPagination(prev => ({ ...prev, totalPages: Math.ceil(data.length / prev.pageSize), totalElements: data.length }));
      } else if (currentPage === 'companies') {
        response = await apiCall('/companies');
        const data = Array.isArray(response) ? response : (response.content || []);
        setCompanies(data);
        setPagination(prev => ({ ...prev, totalPages: Math.ceil(data.length / prev.pageSize), totalElements: data.length }));
      }
    } catch (err) {
      console.error('Failed to fetch data:', err);
    } finally {
      setLoading(false);
    }
  };

  const handlePageChange = (newPage) => {
    setPagination({ ...pagination, pageNumber: newPage });
  };

  // Helper function to get paginated data
  const getPaginatedData = (data) => {
    const startIndex = pagination.pageNumber * pagination.pageSize;
    const endIndex = startIndex + pagination.pageSize;
    return data.slice(startIndex, endIndex);
  };

  const menuItems = [
    { id: 'dashboard', label: 'Dashboard', icon: Home },
    { id: 'medicines', label: 'Medicines', icon: Package },
    { id: 'sales', label: 'Sales', icon: ShoppingCart },
    { id: 'users', label: 'Users', icon: Users },
    { id: 'agents', label: 'Agents', icon: User },
    { id: 'companies', label: 'Companies', icon: Package },
    { id: 'analytics', label: 'Analytics', icon: Activity },
    { id: 'reports', label: 'Reports', icon: FileText },
    { id: 'profile', label: 'Profile', icon: UserCircle },
  ];

  return (
    <div className="min-h-screen bg-slate-50">
      {/* Top Navigation */}
      <nav className="bg-gradient-to-r from-white via-cyan-50 to-blue-50 shadow-lg border-b border-cyan-100 sticky top-0 z-40">
        <div className="max-w-full px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center gap-4">
              <button
                onClick={() => setSidebarOpen(!sidebarOpen)}
                className="lg:hidden p-2 rounded-xl hover:bg-cyan-100 transition-all"
              >
                {sidebarOpen ? <X className="w-6 h-6 text-slate-700" /> : <Menu className="w-6 h-6 text-slate-700" />}
              </button>
              <div className="flex items-center gap-3">
                <div className="w-11 h-11 bg-gradient-to-br from-cyan-500 via-blue-600 to-indigo-600 rounded-2xl flex items-center justify-center shadow-xl shadow-cyan-500/50 animate-pulse">
                  <Shield className="w-6 h-6 text-white" />
                </div>
                <div className="hidden sm:block">
                  <h1 className="text-xl font-bold bg-gradient-to-r from-cyan-600 to-blue-600 bg-clip-text text-transparent">Pharmacy MS</h1>
                  <p className="text-xs text-slate-500">Management System</p>
                </div>
              </div>
            </div>

            {/* Global Search */}
            <div className="hidden md:block flex-1 max-w-md mx-8">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400" />
                <input
                  type="text"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  placeholder="Search anything..."
                  className="w-full pl-11 pr-4 py-3 bg-white border-2 border-slate-200 rounded-xl text-sm focus:ring-4 focus:ring-cyan-200 focus:border-cyan-500 transition-all shadow-sm"
                />
              </div>
            </div>

            {/* User Menu */}
            <div className="flex items-center gap-4">
              <div className="hidden sm:flex items-center gap-3 px-4 py-2 bg-white rounded-xl shadow-md border border-slate-200">
                <div className="text-right">
                  <p className="text-sm font-bold text-slate-900">{user.firstName} {user.lastName}</p>
                  <Badge variant="primary" size="sm">{user.role}</Badge>
                </div>
                <img 
                  src={`https://ui-avatars.com/api/?name=${user.firstName}+${user.lastName}&background=0ea5e9&color=fff`} 
                  alt={user.firstName} 
                  className="w-10 h-10 rounded-full shadow-lg ring-2 ring-cyan-500"
                />
              </div>
              <button
                onClick={onLogout}
                className="flex items-center gap-2 px-4 py-2 text-rose-600 hover:text-white hover:bg-gradient-to-r hover:from-rose-500 hover:to-red-600 rounded-xl transition-all shadow-md hover:shadow-lg"
              >
                <LogOut className="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>
      </nav>

      <div className="flex">
        {/* Sidebar */}
        <aside className={`
          fixed lg:sticky top-16 left-0 h-[calc(100vh-4rem)] w-64 bg-gradient-to-b from-slate-900 via-slate-800 to-slate-900 z-30 shadow-2xl
          transform transition-transform duration-300 lg:transform-none
          ${sidebarOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}
        `}>
          <div className="p-4">
            <div className="mb-6">
              <h3 className="text-xs font-semibold text-slate-400 uppercase tracking-wider mb-3 px-4">Main Menu</h3>
            </div>
            <nav className="space-y-1">
              {menuItems.map((item) => {
                const Icon = item.icon;
                return (
                  <button
                    key={item.id}
                    onClick={() => {
                      setCurrentPage(item.id);
                      setSidebarOpen(false);
                      setPagination({ ...pagination, pageNumber: 0 });
                      setSearchTerm('');
                    }}
                    className={`
                      w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200
                      ${currentPage === item.id
                        ? 'bg-gradient-to-r from-cyan-500 to-blue-600 text-white shadow-lg shadow-cyan-500/50 scale-105'
                        : 'text-slate-300 hover:bg-slate-800 hover:text-white hover:scale-105 hover:shadow-lg'
                      }
                    `}
                  >
                    <Icon className="w-5 h-5" />
                    <span className="font-semibold">{item.label}</span>
                  </button>
                );
              })}
            </nav>
          </div>
        </aside>

        {/* Main Content */}
        <main className="flex-1 p-6 lg:p-8">
          {/* Dashboard Overview */}
          {currentPage === 'dashboard' && (
            <div className="space-y-6">
              <div className="flex items-center justify-between">
                <div>
                  <h2 className="text-2xl font-bold text-gray-900">Dashboard Overview</h2>
                  <p className="text-sm text-gray-500 mt-1">Welcome back, {user.firstName}!</p>
                </div>
              </div>
              
              {/* Stats Grid */}
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-5">
                <StatCard
                  title="Total Users"
                  value={stats.totalUsers || 0}
                  icon={Users}
                  color="blue"
                  description={`${stats.active || 0} active users`}
                />
                
                <StatCard
                  title="Medicines"
                  value={stats.totalMedicines || 0}
                  icon={Package}
                  color="green"
                  description={`${stats.lowStockMedicines || 0} low stock`}
                />
                
                <StatCard
                  title="Total Sales"
                  value={stats.totalSales || 0}
                  icon={ShoppingCart}
                  color="purple"
                  description="All time sales"
                />
                
                <StatCard
                  title="Total Revenue"
                  value={`RWF ${Number(stats.totalRevenue || 0).toLocaleString()}`}
                  icon={BarChart3}
                  color="orange"
                  description={`RWF ${Number(stats.todayRevenue || 0).toLocaleString()} today`}
                />
              </div>

              {/* Quick Actions */}
              <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6">
                <h3 className="text-lg font-semibold text-gray-900 mb-5">Quick Actions</h3>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                  <button
                    onClick={() => setCurrentPage('medicines')}
                    className="flex flex-col items-center gap-3 p-5 border-2 border-slate-100 rounded-xl hover:border-blue-500 hover:bg-blue-50 transition-all group"
                  >
                    <div className="p-3 bg-blue-50 rounded-xl group-hover:bg-blue-100 transition">
                      <Package className="w-6 h-6 text-blue-600" />
                    </div>
                    <p className="font-semibold text-sm text-gray-700">View Medicines</p>
                  </button>
                  <button
                    onClick={() => setCurrentPage('sales')}
                    className="flex flex-col items-center gap-3 p-5 border-2 border-slate-100 rounded-xl hover:border-emerald-500 hover:bg-emerald-50 transition-all group"
                  >
                    <div className="p-3 bg-emerald-50 rounded-xl group-hover:bg-emerald-100 transition">
                      <ShoppingCart className="w-6 h-6 text-emerald-600" />
                    </div>
                    <p className="font-semibold text-sm text-gray-700">Record Sale</p>
                  </button>
                  <button
                    onClick={() => setCurrentPage('users')}
                    className="flex flex-col items-center gap-3 p-5 border-2 border-slate-100 rounded-xl hover:border-purple-500 hover:bg-purple-50 transition-all group"
                  >
                    <div className="p-3 bg-purple-50 rounded-xl group-hover:bg-purple-100 transition">
                      <Users className="w-6 h-6 text-purple-600" />
                    </div>
                    <p className="font-semibold text-sm text-gray-700">Manage Users</p>
                  </button>
                  <button
                    onClick={() => setCurrentPage('reports')}
                    className="flex flex-col items-center gap-3 p-5 border-2 border-slate-100 rounded-xl hover:border-orange-500 hover:bg-orange-50 transition-all group"
                  >
                    <div className="p-3 bg-orange-50 rounded-xl group-hover:bg-orange-100 transition">
                      <BarChart3 className="w-6 h-6 text-orange-600" />
                    </div>
                    <p className="font-semibold text-sm text-gray-700">View Reports</p>
                  </button>
                </div>
              </div>
            </div>
          )}

          {/* Users Page */}
          {currentPage === 'users' && (
            <div className="space-y-6">
              <div className="flex justify-between items-center">
                <div>
                  <h2 className="text-3xl font-bold bg-gradient-to-r from-cyan-600 to-blue-600 bg-clip-text text-transparent">Users Management</h2>
                  <p className="text-sm text-slate-500 mt-2 flex items-center gap-2">
                    <Users className="w-4 h-4" />
                    Manage system users and permissions
                  </p>
                </div>
                <div className="flex gap-3">
                  <Button variant="outline" onClick={() => exportToCSV(users, 'users')} icon={BarChart3}>
                    Export
                  </Button>
                  <Button variant="primary" onClick={() => setShowAddUserModal(true)} icon={User}>
                    Add User
                  </Button>
                </div>
              </div>

              {loading ? (
                <div className="flex justify-center items-center h-64">
                  <Loader className="w-8 h-8 animate-spin text-blue-500" />
                </div>
              ) : (
                <>
                  <Card className="overflow-hidden p-0">
                    <table className="w-full">
                      <thead className="bg-gradient-to-r from-slate-50 via-cyan-50 to-blue-50 border-b-2 border-cyan-200">
                        <tr>
                          <th className="px-6 py-4 text-left text-xs font-bold text-slate-700 uppercase tracking-wider">Name</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-slate-700 uppercase tracking-wider">Email</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-slate-700 uppercase tracking-wider">Phone</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-slate-700 uppercase tracking-wider">Role</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-slate-700 uppercase tracking-wider">Status</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-slate-100">
                        {getPaginatedData(users).map((u) => (
                          <tr key={u.id} className="hover:bg-gradient-to-r hover:from-cyan-50 hover:to-blue-50 transition-all duration-200 group">
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="flex items-center gap-3">
                                <img 
                                  src={`https://ui-avatars.com/api/?name=${u.firstName}+${u.lastName}&background=0ea5e9&color=fff`}
                                  alt={u.firstName}
                                  className="w-10 h-10 rounded-full shadow-md group-hover:shadow-lg transition-shadow"
                                />
                                <div className="font-semibold text-slate-900">{u.firstName} {u.lastName}</div>
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-600">{u.email}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-600">{u.phone}</td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <Badge variant="info">{u.role}</Badge>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <Badge variant={u.active ? 'success' : 'danger'}>
                                {u.active ? 'Active' : 'Inactive'}
                              </Badge>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </Card>

                  {/* Pagination */}
                  {users.length > 0 && <Pagination currentPage={pagination.pageNumber} totalPages={pagination.totalPages} onPageChange={handlePageChange} />}
                </>
              )}
            </div>
          )}

          {/* Medicines Page */}
          {currentPage === 'medicines' && (
            <div className="space-y-6">
              <div className="flex justify-between items-center">
                <div>
                  <h2 className="text-3xl font-bold bg-gradient-to-r from-emerald-600 to-green-600 bg-clip-text text-transparent">Medicines Inventory</h2>
                  <p className="text-sm text-slate-500 mt-2 flex items-center gap-2">
                    <Package className="w-4 h-4" />
                    Track and manage medicine stock
                  </p>
                </div>
                <div className="flex gap-3">
                  <Button variant="outline" onClick={() => window.print()} icon={BarChart3}>
                    Print
                  </Button>
                  <Button variant="secondary" onClick={() => exportToCSV(medicines, 'medicines')} icon={BarChart3}>
                    Export
                  </Button>
                  <Button variant="success" onClick={() => setShowAddMedicineModal(true)} icon={Package}>
                    Add Medicine
                  </Button>
                </div>
              </div>

              {loading ? (
                <div className="flex justify-center items-center h-64">
                  <Loader className="w-8 h-8 animate-spin text-green-500" />
                </div>
              ) : (
                <>
                  <Card className="overflow-hidden p-0">
                    <table className="w-full">
                      <thead className="bg-gradient-to-r from-slate-50 via-emerald-50 to-green-50 border-b-2 border-emerald-200">
                        <tr>
                          <th className="px-6 py-4 text-left text-xs font-bold text-slate-700 uppercase tracking-wider">Medicine</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-slate-700 uppercase tracking-wider">Category</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-slate-700 uppercase tracking-wider">Price</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-slate-700 uppercase tracking-wider">Stock</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-slate-700 uppercase tracking-wider">Expiry</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-slate-100">
                        {medicines && medicines.length > 0 ? getPaginatedData(medicines).map((m) => (
                          <tr key={m.id} className="hover:bg-gradient-to-r hover:from-emerald-50 hover:to-green-50 transition-all duration-200 group">
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="flex items-center gap-3">
                                <div className="p-2 bg-gradient-to-br from-emerald-100 to-green-100 rounded-lg group-hover:shadow-md transition-shadow">
                                  <Package className="w-5 h-5 text-emerald-600" />
                                </div>
                                <div>
                                  <div className="font-semibold text-slate-900">{m.name}</div>
                                  <div className="text-xs text-slate-500">{m.batchNumber || 'N/A'}</div>
                                </div>
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <Badge variant="purple">{m.category}</Badge>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span className="text-sm font-bold text-emerald-600">RWF {Number(m.sellingPrice || 0).toLocaleString()}</span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <Badge variant={(m.quantity || 0) < 20 ? 'danger' : 'success'}>
                                {m.quantity || 0} units
                              </Badge>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="flex items-center gap-2 text-sm text-slate-600">
                                <Calendar className="w-4 h-4" />
                                {m.expiryDate || 'N/A'}
                              </div>
                            </td>
                          </tr>
                        )) : (
                          <tr>
                            <td colSpan="5" className="px-6 py-8 text-center text-gray-500">
                              {loading ? 'Loading...' : 'No medicines found. Click "Add Medicine" to add your first medicine.'}
                            </td>
                          </tr>
                        )}
                      </tbody>
                    </table>
                  </Card>

                  {/* Pagination */}
                  {medicines.length > 0 && <Pagination currentPage={pagination.pageNumber} totalPages={pagination.totalPages} onPageChange={handlePageChange} />}
                </>
              )}
            </div>
          )}

          {/* Sales Page */}
          {currentPage === 'sales' && (
            <div className="space-y-6">
              <div className="flex justify-between items-center">
                <div className="flex items-center gap-4">
                  <div className="w-14 h-14 bg-gradient-to-br from-purple-500 to-pink-600 rounded-2xl flex items-center justify-center shadow-xl shadow-purple-500/30">
                    <ShoppingCart className="w-7 h-7 text-white" />
                  </div>
                  <div>
                    <h2 className="text-3xl font-bold bg-gradient-to-r from-purple-600 to-pink-600 bg-clip-text text-transparent">Sales Records</h2>
                    <p className="text-sm text-slate-500 mt-1 flex items-center gap-2">
                      <Activity className="w-4 h-4" />
                      View and manage all sales transactions
                    </p>
                  </div>
                </div>
                <div className="flex gap-3">
                  <Button 
                    onClick={() => printSalesReceipt(sales)}
                    variant="outline"
                    className="shadow-lg"
                  >
                    <BarChart3 className="w-4 h-4" />
                    Print
                  </Button>
                  <Button 
                    onClick={() => exportToCSV(sales, 'sales')}
                    variant="outline"
                    className="shadow-lg"
                  >
                    <BarChart3 className="w-4 h-4" />
                    Export
                  </Button>
                  <Button 
                    onClick={() => setShowNewSaleModal(true)}
                    variant="primary"
                    className="shadow-lg shadow-purple-500/30"
                  >
                    <ShoppingCart className="w-4 h-4" />
                    New Sale
                  </Button>
                </div>
              </div>

              {loading ? (
                <div className="flex justify-center items-center h-64">
                  <Loader className="w-8 h-8 animate-spin text-purple-500" />
                </div>
              ) : (
                <>
                  <Card className="overflow-hidden">
                    <table className="w-full">
                      <thead className="bg-gradient-to-r from-slate-50 via-purple-50 to-pink-50 border-b-2 border-purple-200">
                        <tr>
                          <th className="px-6 py-4 text-left text-xs font-bold text-purple-900 uppercase tracking-wider">Date</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-purple-900 uppercase tracking-wider">Customer</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-purple-900 uppercase tracking-wider">Medicine</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-purple-900 uppercase tracking-wider">Quantity</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-purple-900 uppercase tracking-wider">Total</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-slate-100">
                        {getPaginatedData(sales).map((s) => (
                          <tr key={s.id} className="hover:bg-gradient-to-r hover:from-purple-50 hover:to-pink-50 transition-all">
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="flex items-center gap-2">
                                <div className="w-10 h-10 bg-gradient-to-br from-purple-100 to-pink-100 rounded-xl flex items-center justify-center">
                                  <Calendar className="w-5 h-5 text-purple-600" />
                                </div>
                                <span className="text-sm font-medium text-slate-700">
                                  {new Date(s.saleDate).toLocaleDateString()}
                                </span>
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="flex items-center gap-3">
                                <img 
                                  src={`https://ui-avatars.com/api/?name=${s.customerName?.replace(' ', '+')}&background=a855f7&color=fff`} 
                                  alt={s.customerName} 
                                  className="w-10 h-10 rounded-full shadow-md"
                                />
                                <div>
                                  <div className="font-bold text-slate-900">{s.customerName}</div>
                                  <div className="text-xs text-slate-500">{s.customerPhone}</div>
                                </div>
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="flex items-center gap-2">
                                <Package className="w-4 h-4 text-purple-500" />
                                <span className="text-sm font-medium text-slate-900">{s.medicine?.name || 'N/A'}</span>
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <Badge variant="secondary">{s.quantity || 0} units</Badge>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="text-sm font-bold text-purple-600">RWF {Number(s.totalPrice || 0).toFixed(2)}</div>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </Card>

                  {/* Pagination */}
                  {sales.length > 0 && pagination.totalPages > 1 && <Pagination currentPage={pagination.pageNumber} totalPages={pagination.totalPages} onPageChange={handlePageChange} />}
                </>
              )}
            </div>
          )}

          {/* Agents Page */}
          {currentPage === 'agents' && (
            <div className="space-y-6">
              <div className="flex justify-between items-center">
                <div className="flex items-center gap-4">
                  <div className="w-14 h-14 bg-gradient-to-br from-indigo-500 to-purple-600 rounded-2xl flex items-center justify-center shadow-xl shadow-indigo-500/30">
                    <Users className="w-7 h-7 text-white" />
                  </div>
                  <div>
                    <h2 className="text-3xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">Agents Management</h2>
                    <p className="text-sm text-slate-500 mt-1 flex items-center gap-2">
                      <User className="w-4 h-4" />
                      Manage pharmaceutical agents and representatives
                    </p>
                  </div>
                </div>
                <div className="flex gap-3">
                  <Button 
                    onClick={() => exportToCSV(agents, 'agents')}
                    variant="outline"
                    className="shadow-lg"
                  >
                    <BarChart3 className="w-4 h-4" />
                    Export
                  </Button>
                  <Button 
                    onClick={() => setShowAddAgentModal(true)}
                    variant="primary"
                    className="shadow-lg shadow-indigo-500/30"
                  >
                    <User className="w-4 h-4" />
                    Add Agent
                  </Button>
                </div>
              </div>

              {loading ? (
                <div className="flex justify-center items-center h-64">
                  <Loader className="w-8 h-8 animate-spin text-indigo-500" />
                </div>
              ) : (
                <>
                  <Card className="overflow-hidden">
                    <table className="w-full">
                      <thead className="bg-gradient-to-r from-slate-50 via-indigo-50 to-purple-50 border-b-2 border-indigo-200">
                        <tr>
                          <th className="px-6 py-4 text-left text-xs font-bold text-indigo-900 uppercase tracking-wider">Name</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-indigo-900 uppercase tracking-wider">Email</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-indigo-900 uppercase tracking-wider">Phone</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-indigo-900 uppercase tracking-wider">Company</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-indigo-900 uppercase tracking-wider">Medicines</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-slate-100">
                        {getPaginatedData(agents).map((agent) => (
                          <tr key={agent.id} className="hover:bg-gradient-to-r hover:from-indigo-50 hover:to-purple-50 transition-all">
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="flex items-center gap-3">
                                <img 
                                  src={`https://ui-avatars.com/api/?name=${agent.firstName}+${agent.lastName}&background=6366f1&color=fff`} 
                                  alt={`${agent.firstName} ${agent.lastName}`} 
                                  className="w-10 h-10 rounded-full shadow-md"
                                />
                                <span className="font-bold text-slate-900">{agent.firstName} {agent.lastName}</span>
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-600">{agent.email}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-slate-700">{agent.phone}</td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <Badge variant="secondary">{agent.companyName || 'N/A'}</Badge>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <Badge variant="primary">
                                <Package className="w-3 h-3" />
                                {agent.medicineCount || 0} medicines
                              </Badge>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </Card>
                  {agents.length > 0 && pagination.totalPages > 1 && <Pagination currentPage={pagination.pageNumber} totalPages={pagination.totalPages} onPageChange={handlePageChange} />}
                </>
              )}
            </div>
          )}

          {/* Companies Page */}
          {currentPage === 'companies' && (
            <div className="space-y-6">
              <div className="flex justify-between items-center">
                <div className="flex items-center gap-4">
                  <div className="w-14 h-14 bg-gradient-to-br from-teal-500 to-cyan-600 rounded-2xl flex items-center justify-center shadow-xl shadow-teal-500/30">
                    <Package className="w-7 h-7 text-white" />
                  </div>
                  <div>
                    <h2 className="text-3xl font-bold bg-gradient-to-r from-teal-600 to-cyan-600 bg-clip-text text-transparent">Companies Management</h2>
                    <p className="text-sm text-slate-500 mt-1 flex items-center gap-2">
                      <Package className="w-4 h-4" />
                      Manage pharmaceutical companies and suppliers
                    </p>
                  </div>
                </div>
                <div className="flex gap-3">
                  <Button 
                    onClick={() => exportToCSV(companies, 'companies')}
                    variant="outline"
                    className="shadow-lg"
                  >
                    <BarChart3 className="w-4 h-4" />
                    Export
                  </Button>
                  <Button 
                    onClick={() => setShowAddCompanyModal(true)}
                    variant="primary"
                    className="shadow-lg shadow-teal-500/30"
                  >
                    <Package className="w-4 h-4" />
                    Add Company
                  </Button>
                </div>
              </div>

              {loading ? (
                <div className="flex justify-center items-center h-64">
                  <Loader className="w-8 h-8 animate-spin text-teal-500" />
                </div>
              ) : (
                <>
                  <Card className="overflow-hidden">
                    <table className="w-full">
                      <thead className="bg-gradient-to-r from-slate-50 via-teal-50 to-cyan-50 border-b-2 border-teal-200">
                        <tr>
                          <th className="px-6 py-4 text-left text-xs font-bold text-teal-900 uppercase tracking-wider">Name</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-teal-900 uppercase tracking-wider">Email</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-teal-900 uppercase tracking-wider">Phone</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-teal-900 uppercase tracking-wider">Address</th>
                          <th className="px-6 py-4 text-left text-xs font-bold text-teal-900 uppercase tracking-wider">Medicines</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-slate-100">
                        {getPaginatedData(companies).map((company) => (
                          <tr key={company.id} className="hover:bg-gradient-to-r hover:from-teal-50 hover:to-cyan-50 transition-all">
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="flex items-center gap-3">
                                <div className="w-10 h-10 bg-gradient-to-br from-teal-100 to-cyan-100 rounded-xl flex items-center justify-center">
                                  <Package className="w-5 h-5 text-teal-600" />
                                </div>
                                <span className="font-bold text-slate-900">{company.name}</span>
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-600">{company.email}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-slate-700">{company.phone}</td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <Badge variant="secondary">{company.address || 'N/A'}</Badge>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <Badge variant="success">
                                <Package className="w-3 h-3" />
                                {company.medicineCount || 0} medicines
                              </Badge>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </Card>
                  {companies.length > 0 && pagination.totalPages > 1 && <Pagination currentPage={pagination.pageNumber} totalPages={pagination.totalPages} onPageChange={handlePageChange} />}
                </>
              )}
            </div>
          )}

          {/* Reports Page */}
          {currentPage === 'reports' && (
            <div className="space-y-6">
              <div className="flex items-center gap-4">
                <div className="w-14 h-14 bg-gradient-to-br from-orange-500 to-amber-600 rounded-2xl flex items-center justify-center shadow-xl shadow-orange-500/30">
                  <BarChart3 className="w-7 h-7 text-white" />
                </div>
                <div>
                  <h2 className="text-3xl font-bold bg-gradient-to-r from-orange-600 to-amber-600 bg-clip-text text-transparent">Reports & Analytics</h2>
                  <p className="text-sm text-slate-500 mt-1 flex items-center gap-2">
                    <Activity className="w-4 h-4" />
                    Generate and export various reports
                  </p>
                </div>
              </div>
              
              {/* Report Cards */}
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
                <Card className="hover:shadow-xl transition-all">
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-lg font-bold text-slate-900">Sales Report</h3>
                    <div className="p-3 bg-gradient-to-br from-purple-100 to-pink-100 rounded-xl">
                      <ShoppingCart className="w-6 h-6 text-purple-600" />
                    </div>
                  </div>
                  <p className="text-sm text-slate-600 mb-4">Export all sales transactions</p>
                  <Button
                    onClick={async () => {
                      try {
                        const allSales = await apiCall('/sales');
                        exportToCSV(allSales, 'sales_report');
                      } catch (err) {
                        alert('Failed to export sales report');
                      }
                    }}
                    variant="primary"
                    className="w-full bg-gradient-to-r from-purple-500 to-pink-600 hover:from-purple-600 hover:to-pink-700"
                  >
                    Export Report
                  </Button>
                </Card>

                <Card className="hover:shadow-xl transition-all">
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-lg font-bold text-slate-900">Inventory Report</h3>
                    <div className="p-3 bg-gradient-to-br from-emerald-100 to-green-100 rounded-xl">
                      <Package className="w-6 h-6 text-emerald-600" />
                    </div>
                  </div>
                  <p className="text-sm text-slate-600 mb-4">Export medicine inventory</p>
                  <Button
                    onClick={async () => {
                      try {
                        const allMedicines = await apiCall('/medicines');
                        exportToCSV(allMedicines, 'inventory_report');
                      } catch (err) {
                        alert('Failed to export inventory report');
                      }
                    }}
                    variant="success"
                    className="w-full"
                  >
                    Export Report
                  </Button>
                </Card>

                <Card className="hover:shadow-xl transition-all">
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-lg font-bold text-slate-900">Users Report</h3>
                    <div className="p-3 bg-gradient-to-br from-blue-100 to-cyan-100 rounded-xl">
                      <Users className="w-6 h-6 text-blue-600" />
                    </div>
                  </div>
                  <p className="text-sm text-slate-600 mb-4">Export user information</p>
                  <Button
                    onClick={async () => {
                      try {
                        const allUsers = await apiCall('/users');
                        exportToCSV(allUsers, 'users_report');
                      } catch (err) {
                        alert('Failed to export users report');
                      }
                    }}
                    variant="primary"
                    className="w-full bg-gradient-to-r from-blue-500 to-cyan-600 hover:from-blue-600 hover:to-cyan-700"
                  >
                    Export Report
                  </Button>
                </Card>

                <Card className="hover:shadow-xl transition-all">
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-lg font-bold text-slate-900">Low Stock Report</h3>
                    <div className="p-3 bg-gradient-to-br from-orange-100 to-amber-100 rounded-xl">
                      <AlertCircle className="w-6 h-6 text-orange-600" />
                    </div>
                  </div>
                  <p className="text-sm text-slate-600 mb-4">Export low stock medicines</p>
                  <Button
                    onClick={async () => {
                      try {
                        const lowStock = await apiCall('/medicines/low-stock?threshold=20');
                        exportToCSV(lowStock, 'low_stock_report');
                      } catch (err) {
                        alert('Failed to export low stock report');
                      }
                    }}
                    variant="warning"
                    className="w-full"
                  >
                    Export Report
                  </Button>
                </Card>

                <Card className="hover:shadow-xl transition-all">
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-lg font-bold text-slate-900">Expired Medicines</h3>
                    <div className="p-3 bg-gradient-to-br from-red-100 to-rose-100 rounded-xl">
                      <AlertCircle className="w-6 h-6 text-red-600" />
                    </div>
                  </div>
                  <p className="text-sm text-slate-600 mb-4">Export expired medicines</p>
                  <Button
                    onClick={async () => {
                      try {
                        const expired = await apiCall('/medicines/expired');
                        exportToCSV(expired, 'expired_medicines_report');
                      } catch (err) {
                        alert('Failed to export expired medicines report');
                      }
                    }}
                    variant="danger"
                    className="w-full"
                  >
                    Export Expired CSV
                  </Button>
                </Card>

                <Card className="hover:shadow-xl transition-all">
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-lg font-bold text-slate-900">Revenue Report</h3>
                    <div className="p-3 bg-gradient-to-br from-indigo-100 to-purple-100 rounded-xl">
                      <BarChart3 className="w-6 h-6 text-indigo-600" />
                    </div>
                  </div>
                  <p className="text-slate-600 mb-4">View revenue statistics</p>
                  <Button
                    onClick={() => {
                      alert(`Total Revenue: RWF ${Number(stats.totalRevenue || 0).toFixed(2)}\nToday's Revenue: RWF ${Number(stats.todayRevenue || 0).toFixed(2)}`);
                    }}
                    variant="primary"
                    className="w-full bg-gradient-to-r from-indigo-500 to-purple-600 hover:from-indigo-600 hover:to-purple-700"
                  >
                    View Revenue Stats
                  </Button>
                </Card>
              </div>

              {/* Print All Reports */}
              <Card className="p-6">
                <div>
                  <h3 className="text-xl font-bold text-slate-900 mb-2 flex items-center gap-2">
                    <BarChart3 className="w-6 h-6 text-orange-600" />
                    Generate Complete Report
                  </h3>
                  <p className="text-slate-600 mb-4">Generate a comprehensive report with all data</p>
                  <Button
                    onClick={async () => {
                    try {
                      const [allSales, allMedicines, allUsers] = await Promise.all([
                        apiCall('/sales'),
                        apiCall('/medicines'),
                        apiCall('/users')
                      ]);
                      
                      const printWindow = window.open('', '', 'width=1000,height=800');
                      printWindow.document.write(`
                        <html>
                          <head>
                            <title>Complete Pharmacy Report</title>
                            <style>
                              body { font-family: Arial, sans-serif; padding: 20px; }
                              h1 { color: #333; border-bottom: 3px solid #4CAF50; padding-bottom: 10px; }
                              h2 { color: #666; margin-top: 30px; }
                              .stats { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin: 20px 0; }
                              .stat-card { background: #f5f5f5; padding: 15px; border-radius: 8px; text-align: center; }
                              .stat-value { font-size: 24px; font-weight: bold; color: #4CAF50; }
                              .stat-label { color: #666; margin-top: 5px; }
                            </style>
                          </head>
                          <body>
                            <h1>Pharmacy Management System - Complete Report</h1>
                            <p><strong>Generated:</strong> ${new Date().toLocaleString()}</p>
                            
                            <div class="stats">
                              <div class="stat-card">
                                <div class="stat-value">${allMedicines.length}</div>
                                <div class="stat-label">Total Medicines</div>
                              </div>
                              <div class="stat-card">
                                <div class="stat-value">${allUsers.length}</div>
                                <div class="stat-label">Total Users</div>
                              </div>
                              <div class="stat-card">
                                <div class="stat-value">${allSales.length}</div>
                                <div class="stat-label">Total Sales</div>
                              </div>
                              <div class="stat-card">
                                <div class="stat-value">RWF ${allSales.reduce((sum, s) => sum + (s.totalPrice || 0), 0).toFixed(2)}</div>
                                <div class="stat-label">Total Revenue</div>
                              </div>
                            </div>

                            <h2>Recent Sales (Last 10)</h2>
                            <table style="width:100%; border-collapse: collapse; margin-top: 10px;">
                              <tr style="background: #4CAF50; color: white;">
                                <th style="border: 1px solid #ddd; padding: 8px;">Date</th>
                                <th style="border: 1px solid #ddd; padding: 8px;">Medicine</th>
                                <th style="border: 1px solid #ddd; padding: 8px;">Quantity</th>
                                <th style="border: 1px solid #ddd; padding: 8px;">Total</th>
                              </tr>
                              ${allSales.slice(0, 10).map(sale => `
                                <tr>
                                  <td style="border: 1px solid #ddd; padding: 8px;">${new Date(sale.saleDate).toLocaleDateString()}</td>
                                  <td style="border: 1px solid #ddd; padding: 8px;">${sale.medicine?.name || 'N/A'}</td>
                                  <td style="border: 1px solid #ddd; padding: 8px;">${sale.quantity}</td>
                                  <td style="border: 1px solid #ddd; padding: 8px;">RWF ${sale.totalPrice}</td>
                                </tr>
                              `).join('')}
                            </table>

                            <h2>Low Stock Medicines</h2>
                            <table style="width:100%; border-collapse: collapse; margin-top: 10px;">
                              <tr style="background: #FF9800; color: white;">
                                <th style="border: 1px solid #ddd; padding: 8px;">Medicine</th>
                                <th style="border: 1px solid #ddd; padding: 8px;">Category</th>
                                <th style="border: 1px solid #ddd; padding: 8px;">Quantity</th>
                                <th style="border: 1px solid #ddd; padding: 8px;">Price</th>
                              </tr>
                              ${allMedicines.filter(m => m.quantity < 20).map(med => `
                                <tr>
                                  <td style="border: 1px solid #ddd; padding: 8px;">${med.name}</td>
                                  <td style="border: 1px solid #ddd; padding: 8px;">${med.category}</td>
                                  <td style="border: 1px solid #ddd; padding: 8px;">${med.quantity}</td>
                                  <td style="border: 1px solid #ddd; padding: 8px;">RWF ${med.price}</td>
                                </tr>
                              `).join('')}
                            </table>
                          </body>
                        </html>
                      `);
                      printWindow.document.close();
                      printWindow.print();
                    } catch (err) {
                      alert('Failed to generate complete report');
                    }
                  }}
                  variant="primary"
                  className="w-full bg-gradient-to-r from-orange-500 to-amber-600 hover:from-orange-600 hover:to-amber-700"
                  size="lg"
                >
                  <BarChart3 className="w-5 h-5" />
                  Generate & Print Complete Report
                </Button>
                </div>
              </Card>
            </div>
          )}

          {/* Analytics Page */}
          {currentPage === 'analytics' && (
            <div className="space-y-6">
              <div>
                <h2 className="text-2xl font-bold bg-gradient-to-r from-cyan-600 to-blue-600 bg-clip-text text-transparent">Advanced Analytics</h2>
                <p className="text-sm text-slate-500 mt-1">Data-driven insights for better decisions</p>
              </div>

              {/* Advanced Stats Grid */}
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-5">
                <StatCard
                  title="Total Revenue"
                  value={`RWF ${Number(stats.totalRevenue || 0).toLocaleString()}`}
                  icon={DollarSign}
                  color="green"
                  trend={12.5}
                  description="vs last month"
                />
                <StatCard
                  title="Total Sales"
                  value={stats.totalSales || 0}
                  icon={ShoppingCart}
                  color="blue"
                  trend={8.2}
                  description="vs last month"
                />
                <StatCard
                  title="Active Users"
                  value={stats.active || 0}
                  icon={Users}
                  color="purple"
                  trend={-2.1}
                  description="vs last month"
                />
                <StatCard
                  title="Products"
                  value={stats.totalMedicines || 0}
                  icon={Package}
                  color="orange"
                  trend={5.3}
                  description="new this month"
                />
              </div>

              {/* Charts Grid */}
              <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                <SimpleBarChart
                  title="Sales by Category"
                  data={[
                    { label: 'Antibiotics', value: 145 },
                    { label: 'Pain Relief', value: 230 },
                    { label: 'Vitamins', value: 178 },
                    { label: 'First Aid', value: 95 },
                    { label: 'Supplements', value: 167 },
                    { label: 'Others', value: 89 }
                  ]}
                  height={300}
                />
                
                <SimplePieChart
                  title="Revenue Distribution"
                  data={[
                    { label: 'Direct Sales', value: 45000 },
                    { label: 'Online Orders', value: 32000 },
                    { label: 'Bulk Orders', value: 18000 },
                    { label: 'Others', value: 9000 }
                  ]}
                  size={250}
                />
              </div>

              {/* Top Performing Products */}
              <Card>
                <h3 className="text-lg font-bold text-slate-900 mb-4 flex items-center gap-2">
                  <TrendingUp className="w-5 h-5 text-emerald-600" />
                  Top Performing Products
                </h3>
                <div className="space-y-3">
                  {medicines.slice(0, 5).map((medicine, index) => (
                    <div key={index} className="flex items-center justify-between p-4 bg-gradient-to-r from-slate-50 to-cyan-50 rounded-xl hover:shadow-md transition-all">
                      <div className="flex items-center gap-4">
                        <div className="w-10 h-10 rounded-full bg-gradient-to-br from-cyan-500 to-blue-600 flex items-center justify-center text-white font-bold shadow-lg">
                          {index + 1}
                        </div>
                        <div>
                          <p className="font-semibold text-slate-900">{medicine.name}</p>
                          <p className="text-xs text-slate-500">{medicine.category}</p>
                        </div>
                      </div>
                      <div className="text-right">
                        <p className="font-bold text-emerald-600">RWF {Number(medicine.sellingPrice || 0).toFixed(2)}</p>
                        <Badge variant="success" size="sm">{medicine.quantity || 0} in stock</Badge>
                      </div>
                    </div>
                  ))}
                </div>
              </Card>

              {/* Recent Activity */}
              <Card>
                <h3 className="text-lg font-bold text-slate-900 mb-4 flex items-center gap-2">
                  <Activity className="w-5 h-5 text-blue-600" />
                  Recent Activity
                </h3>
                <div className="space-y-3">
                  {sales.slice(0, 5).map((sale, index) => (
                    <div key={index} className="flex items-start gap-3 p-3 hover:bg-slate-50 rounded-xl transition-all">
                      <div className="p-2 bg-blue-100 rounded-lg">
                        <ShoppingCart className="w-4 h-4 text-blue-600" />
                      </div>
                      <div className="flex-1">
                        <p className="text-sm font-semibold text-slate-900">{sale.customerName || 'Walk-in Customer'}</p>
                        <p className="text-xs text-slate-500">Purchased {sale.medicine?.name || 'Product'}</p>
                      </div>
                      <div className="text-right">
                        <p className="text-sm font-bold text-emerald-600">RWF {Number(sale.totalPrice || 0).toFixed(2)}</p>
                        <p className="text-xs text-slate-500">{new Date(sale.saleDate).toLocaleDateString()}</p>
                      </div>
                    </div>
                  ))}
                </div>
              </Card>
            </div>
          )}

          {/* Profile Page */}
          {currentPage === 'profile' && (
            <div className="space-y-6">
              <div>
                <h2 className="text-2xl font-bold bg-gradient-to-r from-cyan-600 to-blue-600 bg-clip-text text-transparent">My Profile</h2>
                <p className="text-sm text-slate-500 mt-1">Manage your account settings</p>
              </div>

              <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* Profile Card */}
                <Card className="lg:col-span-1">
                  <div className="text-center">
                    <div className="inline-block relative mb-4">
                      <img 
                        src={`https://ui-avatars.com/api/?name=${user.firstName}+${user.lastName}&background=0ea5e9&color=fff&size=128`}
                        alt={user.firstName}
                        className="w-32 h-32 rounded-full shadow-2xl border-4 border-white"
                      />
                      <div className="absolute bottom-0 right-0 w-8 h-8 bg-emerald-500 rounded-full border-4 border-white flex items-center justify-center">
                        <CheckCircle className="w-4 h-4 text-white" />
                      </div>
                    </div>
                    <h3 className="text-xl font-bold text-slate-900">{user.firstName} {user.lastName}</h3>
                    <Badge variant="primary" className="mt-2">{user.role}</Badge>
                    <p className="text-sm text-slate-500 mt-4">{user.email}</p>
                  </div>
                  
                  <div className="mt-6 space-y-3">
                    <div className="flex items-center justify-between py-3 border-t border-slate-100">
                      <span className="text-sm font-medium text-slate-600">Phone</span>
                      <span className="text-sm text-slate-900">{user.phone}</span>
                    </div>
                    <div className="flex items-center justify-between py-3 border-t border-slate-100">
                      <span className="text-sm font-medium text-slate-600">Status</span>
                      <Badge variant={user.active ? "success" : "danger"}>{user.active ? 'Active' : 'Inactive'}</Badge>
                    </div>
                    <div className="flex items-center justify-between py-3 border-t border-slate-100">
                      <span className="text-sm font-medium text-slate-600">Member Since</span>
                      <span className="text-sm text-slate-900">Jan 2024</span>
                    </div>
                  </div>
                </Card>

                {/* Settings Card */}
                <Card className="lg:col-span-2">
                  <h3 className="text-lg font-bold text-slate-900 mb-6">Account Settings</h3>
                  <div className="space-y-4">
                    <div>
                      <label className="block text-sm font-semibold text-slate-700 mb-2">First Name</label>
                      <input
                        type="text"
                        defaultValue={user.firstName}
                        className="w-full px-4 py-3 bg-white border-2 border-slate-200 rounded-xl focus:border-cyan-500 focus:ring-4 focus:ring-cyan-200 focus:outline-none transition-all"
                      />
                    </div>
                    
                    <div>
                      <label className="block text-sm font-semibold text-slate-700 mb-2">Last Name</label>
                      <input
                        type="text"
                        defaultValue={user.lastName}
                        className="w-full px-4 py-3 bg-white border-2 border-slate-200 rounded-xl focus:border-cyan-500 focus:ring-4 focus:ring-cyan-200 focus:outline-none transition-all"
                      />
                    </div>
                    
                    <div>
                      <label className="block text-sm font-semibold text-slate-700 mb-2">Email</label>
                      <input
                        type="email"
                        defaultValue={user.email}
                        className="w-full px-4 py-3 bg-white border-2 border-slate-200 rounded-xl focus:border-cyan-500 focus:ring-4 focus:ring-cyan-200 focus:outline-none transition-all"
                      />
                    </div>
                    
                    <div>
                      <label className="block text-sm font-semibold text-slate-700 mb-2">Phone</label>
                      <input
                        type="tel"
                        defaultValue={user.phone}
                        className="w-full px-4 py-3 bg-white border-2 border-slate-200 rounded-xl focus:border-cyan-500 focus:ring-4 focus:ring-cyan-200 focus:outline-none transition-all"
                      />
                    </div>
                    
                    <div className="flex gap-3 pt-4">
                      <Button variant="primary" className="flex-1">
                        <CheckCircle className="w-4 h-4" />
                        Save Changes
                      </Button>
                      <Button variant="outline">
                        Cancel
                      </Button>
                    </div>
                  </div>
                </Card>
              </div>

              {/* Security Settings */}
              <Card>
                <h3 className="text-lg font-bold text-slate-900 mb-6 flex items-center gap-2">
                  <Shield className="w-5 h-5 text-cyan-600" />
                  Security Settings
                </h3>
                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Current Password</label>
                    <input
                      type="password"
                      className="w-full px-4 py-3 bg-white border-2 border-slate-200 rounded-xl focus:border-cyan-500 focus:ring-4 focus:ring-cyan-200 focus:outline-none transition-all"
                      placeholder="••••••••"
                    />
                  </div>
                  
                  <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">New Password</label>
                    <input
                      type="password"
                      className="w-full px-4 py-3 bg-white border-2 border-slate-200 rounded-xl focus:border-cyan-500 focus:ring-4 focus:ring-cyan-200 focus:outline-none transition-all"
                      placeholder="••••••••"
                    />
                  </div>
                  
                  <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Confirm New Password</label>
                    <input
                      type="password"
                      className="w-full px-4 py-3 bg-white border-2 border-slate-200 rounded-xl focus:border-cyan-500 focus:ring-4 focus:ring-cyan-200 focus:outline-none transition-all"
                      placeholder="••••••••"
                    />
                  </div>
                  
                  <Button variant="primary">
                    <Shield className="w-4 h-4" />
                    Update Password
                  </Button>
                </div>
              </Card>
            </div>
          )}
        </main>

        {/* Add Medicine Modal */}
        {showAddMedicineModal && (
          <AddMedicineModal 
            onClose={() => setShowAddMedicineModal(false)} 
            onSuccess={() => { setShowAddMedicineModal(false); fetchData(); }}
          />
        )}

        {/* Add User Modal */}
        {showAddUserModal && (
          <AddUserModal 
            onClose={() => setShowAddUserModal(false)} 
            onSuccess={() => { setShowAddUserModal(false); fetchData(); }}
          />
        )}

        {/* New Sale Modal */}
        {showNewSaleModal && (
          <NewSaleModal 
            user={user}
            onClose={() => setShowNewSaleModal(false)} 
            onSuccess={() => { setShowNewSaleModal(false); fetchData(); }}
          />
        )}

        {/* Add Agent Modal */}
        {showAddAgentModal && (
          <AddAgentModal 
            onClose={() => setShowAddAgentModal(false)} 
            onSuccess={() => { setShowAddAgentModal(false); fetchData(); }}
          />
        )}

        {/* Add Company Modal */}
        {showAddCompanyModal && (
          <AddCompanyModal 
            onClose={() => setShowAddCompanyModal(false)} 
            onSuccess={() => { setShowAddCompanyModal(false); fetchData(); }}
          />
        )}
      </div>
    </div>
  );
};

// Add Medicine Modal Component
const AddMedicineModal = ({ onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    name: '',
    category: 'PAINKILLER',
    description: '',
    purchasePrice: '',
    sellingPrice: '',
    quantity: '',
    manufactureDate: '',
    expiryDate: '',
    batchNumber: '',
    companyId: ''
  });
  const [categories, setCategories] = useState([]);
  const [companies, setCompanies] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [categoriesRes, companiesRes] = await Promise.all([
          apiCall('/medicines/categories'),
          apiCall('/companies')
        ]);
        setCategories(categoriesRes);
        setCompanies(companiesRes);
      } catch (err) {
        console.error('Failed to fetch data:', err);
        setCategories(['ANTIBIOTIC', 'PAINKILLER', 'ANTIVIRAL', 'ANTIFUNGAL', 'ANTIHISTAMINE', 'CARDIOVASCULAR', 'DIABETES', 'VITAMINS', 'SUPPLEMENTS', 'ANTISEPTIC', 'OTHER']);
        setCompanies([]);
      }
    };
    fetchData();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const medicineData = {
        name: formData.name,
        category: formData.category,
        description: formData.description,
        purchasePrice: parseFloat(formData.purchasePrice),
        sellingPrice: parseFloat(formData.sellingPrice),
        quantity: parseInt(formData.quantity),
        manufactureDate: formData.manufactureDate,
        expiryDate: formData.expiryDate,
        batchNumber: formData.batchNumber,
        company: formData.companyId ? { id: parseInt(formData.companyId) } : null
      };
      
      await apiCall('/medicines', {
        method: 'POST',
        body: JSON.stringify(medicineData)
      });
      alert('Medicine added successfully!');
      onSuccess();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-xl shadow-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        <div className="p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-900">Add New Medicine</h2>
            <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
              <X className="w-6 h-6" />
            </button>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-3 mb-4 flex items-start gap-2">
              <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0 mt-0.5" />
              <p className="text-sm text-red-700">{error}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Medicine Name *</label>
                <input
                  type="text"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  placeholder="e.g., Paracetamol 500mg"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Category *</label>
                <select
                  value={formData.category}
                  onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  required
                >
                  {categories.map((cat) => (
                    <option key={cat} value={cat}>
                      {cat.replace('_', ' ')}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Purchase Price (RWF) *</label>
                <input
                  type="number"
                  step="0.01"
                  value={formData.purchasePrice}
                  onChange={(e) => setFormData({ ...formData, purchasePrice: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  placeholder="500"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Selling Price (RWF) *</label>
                <input
                  type="number"
                  step="0.01"
                  value={formData.sellingPrice}
                  onChange={(e) => setFormData({ ...formData, sellingPrice: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  placeholder="800"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Quantity *</label>
                <input
                  type="number"
                  value={formData.quantity}
                  onChange={(e) => setFormData({ ...formData, quantity: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  placeholder="100"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Batch Number *</label>
                <input
                  type="text"
                  value={formData.batchNumber}
                  onChange={(e) => setFormData({ ...formData, batchNumber: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  placeholder="BATCH2024001"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Manufacture Date *</label>
                <input
                  type="date"
                  value={formData.manufactureDate}
                  onChange={(e) => setFormData({ ...formData, manufactureDate: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Expiry Date *</label>
                <input
                  type="date"
                  value={formData.expiryDate}
                  onChange={(e) => setFormData({ ...formData, expiryDate: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Company</label>
                <select
                  value={formData.companyId}
                  onChange={(e) => setFormData({ ...formData, companyId: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                >
                  <option value="">Select Company (Optional)</option>
                  {companies.map((company) => (
                    <option key={company.id} value={company.id}>
                      {company.name}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Description</label>
              <textarea
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                rows="3"
              />
            </div>

            <div className="flex gap-3 pt-4">
              <button
                type="submit"
                disabled={loading}
                className="flex-1 bg-green-600 text-white py-2 rounded-lg hover:bg-green-700 transition disabled:opacity-50 flex items-center justify-center gap-2"
              >
                {loading ? <Loader className="w-5 h-5 animate-spin" /> : <CheckCircle className="w-5 h-5" />}
                {loading ? 'Adding...' : 'Add Medicine'}
              </button>
              <button
                type="button"
                onClick={onClose}
                className="px-6 py-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

// Add User Modal Component
const AddUserModal = ({ onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    role: 'CASHIER',
    locationId: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      await apiCall('/auth/signup', {
        method: 'POST',
        body: JSON.stringify(formData)
      });
      alert('User added successfully!');
      onSuccess();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-xl shadow-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        <div className="p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-900">Add New User</h2>
            <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
              <X className="w-6 h-6" />
            </button>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-3 mb-4 flex items-start gap-2">
              <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0 mt-0.5" />
              <p className="text-sm text-red-700">{error}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">First Name *</label>
                <input
                  type="text"
                  value={formData.firstName}
                  onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Last Name *</label>
                <input
                  type="text"
                  value={formData.lastName}
                  onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Email *</label>
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Phone *</label>
                <input
                  type="tel"
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  pattern="07[0-9]{8}"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Password *</label>
                <input
                  type="password"
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  minLength="8"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Role *</label>
                <select
                  value={formData.role}
                  onChange={(e) => setFormData({ ...formData, role: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="CASHIER">Cashier</option>
                  <option value="PHARMACIST">Pharmacist</option>
                  <option value="INVENTORY_MANAGER">Inventory Manager</option>
                  <option value="DOCTOR">Doctor</option>
                  <option value="ADMIN">Admin</option>
                </select>
              </div>
            </div>

            <div className="flex gap-3 pt-4">
              <button
                type="submit"
                disabled={loading}
                className="flex-1 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition disabled:opacity-50 flex items-center justify-center gap-2"
              >
                {loading ? <Loader className="w-5 h-5 animate-spin" /> : <CheckCircle className="w-5 h-5" />}
                {loading ? 'Adding...' : 'Add User'}
              </button>
              <button
                type="button"
                onClick={onClose}
                className="px-6 py-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

// New Sale Modal Component
const NewSaleModal = ({ user, onClose, onSuccess }) => {
  const [medicines, setMedicines] = useState([]);
  const [formData, setFormData] = useState({
    medicineId: '',
    quantity: '',
    customerName: '',
    customerPhone: '',
    paymentMethod: 'CASH'
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [selectedMedicine, setSelectedMedicine] = useState(null);

  useEffect(() => {
    const fetchMedicines = async () => {
      try {
        const response = await apiCall('/medicines');
        setMedicines(response);
      } catch (err) {
        console.error('Failed to fetch medicines:', err);
      }
    };
    fetchMedicines();
  }, []);

  const handleMedicineChange = (medicineId) => {
    setFormData({ ...formData, medicineId });
    const medicine = medicines.find(m => m.id === parseInt(medicineId));
    setSelectedMedicine(medicine);
  };

  const calculateTotal = () => {
    if (selectedMedicine && formData.quantity) {
      return (selectedMedicine.price * parseInt(formData.quantity)).toFixed(2);
    }
    return '0.00';
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const saleData = {
        medicine: { id: parseInt(formData.medicineId) },
        user: { id: user.userId },
        quantity: parseInt(formData.quantity),
        totalPrice: parseFloat(calculateTotal()),
        customerName: formData.customerName || null,
        customerPhone: formData.customerPhone || null,
        paymentMethod: formData.paymentMethod,
        saleDate: new Date().toISOString()
      };

      await apiCall('/sales', {
        method: 'POST',
        body: JSON.stringify(saleData)
      });
      alert('Sale recorded successfully!');
      onSuccess();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-xl shadow-2xl max-w-2xl w-full">
        <div className="p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-900">New Sale</h2>
            <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
              <X className="w-6 h-6" />
            </button>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-3 mb-4 flex items-start gap-2">
              <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0 mt-0.5" />
              <p className="text-sm text-red-700">{error}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Select Medicine *</label>
              <select
                value={formData.medicineId}
                onChange={(e) => handleMedicineChange(e.target.value)}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                required
              >
                <option value="">Choose a medicine</option>
                {medicines.map((medicine) => (
                  <option key={medicine.id} value={medicine.id}>
                    {medicine.name} - RWF {medicine.price} (Stock: {medicine.quantity})
                  </option>
                ))}
              </select>
            </div>

            {selectedMedicine && (
              <div className="bg-blue-50 border border-blue-200 rounded-lg p-3">
                <p className="text-sm text-blue-900">
                  <strong>Price:</strong> RWF {Number(selectedMedicine.price || 0).toFixed(2)} | 
                  <strong> Available:</strong> {selectedMedicine.quantity || 0} units
                </p>
              </div>
            )}

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Quantity *</label>
              <input
                type="number"
                min="1"
                max={selectedMedicine?.quantity || 999}
                value={formData.quantity}
                onChange={(e) => setFormData({ ...formData, quantity: e.target.value })}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                required
              />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Customer Name</label>
                <input
                  type="text"
                  value={formData.customerName}
                  onChange={(e) => setFormData({ ...formData, customerName: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                  placeholder="Optional"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Customer Phone</label>
                <input
                  type="tel"
                  value={formData.customerPhone}
                  onChange={(e) => setFormData({ ...formData, customerPhone: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                  placeholder="Optional"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Payment Method *</label>
              <select
                value={formData.paymentMethod}
                onChange={(e) => setFormData({ ...formData, paymentMethod: e.target.value })}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                required
              >
                <option value="CASH">Cash</option>
                <option value="MOBILE_MONEY">Mobile Money</option>
                <option value="CARD">Card</option>
                <option value="BANK_TRANSFER">Bank Transfer</option>
              </select>
            </div>

            <div className="bg-green-50 border border-green-200 rounded-lg p-4">
              <p className="text-lg font-bold text-green-900">
                Total: RWF {Number(calculateTotal() || 0).toFixed(2)}
              </p>
            </div>

            <div className="flex gap-3 pt-4">
              <button
                type="submit"
                disabled={loading}
                className="flex-1 bg-purple-600 text-white py-2 rounded-lg hover:bg-purple-700 transition disabled:opacity-50 flex items-center justify-center gap-2"
              >
                {loading ? <Loader className="w-5 h-5 animate-spin" /> : <CheckCircle className="w-5 h-5" />}
                {loading ? 'Processing...' : 'Complete Sale'}
              </button>
              <button
                type="button"
                onClick={onClose}
                className="px-6 py-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

// Add Agent Modal Component
const AddAgentModal = ({ onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    companyName: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      await apiCall('/agents', {
        method: 'POST',
        body: JSON.stringify(formData)
      });
      alert('Agent added successfully!');
      onSuccess();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-xl shadow-2xl max-w-2xl w-full">
        <div className="p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-900">Add New Agent</h2>
            <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
              <X className="w-6 h-6" />
            </button>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-3 mb-4 flex items-start gap-2">
              <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0 mt-0.5" />
              <p className="text-sm text-red-700">{error}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">First Name *</label>
                <input
                  type="text"
                  value={formData.firstName}
                  onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Last Name *</label>
                <input
                  type="text"
                  value={formData.lastName}
                  onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Email *</label>
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Phone *</label>
                <input
                  type="tel"
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                  pattern="07[0-9]{8}"
                  required
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Company Name</label>
              <input
                type="text"
                value={formData.companyName}
                onChange={(e) => setFormData({ ...formData, companyName: e.target.value })}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              />
            </div>

            <div className="flex gap-3 pt-4">
              <button
                type="submit"
                disabled={loading}
                className="flex-1 bg-indigo-600 text-white py-2 rounded-lg hover:bg-indigo-700 transition disabled:opacity-50 flex items-center justify-center gap-2"
              >
                {loading ? <Loader className="w-5 h-5 animate-spin" /> : <CheckCircle className="w-5 h-5" />}
                {loading ? 'Adding...' : 'Add Agent'}
              </button>
              <button
                type="button"
                onClick={onClose}
                className="px-6 py-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

// Add Company Modal Component
const AddCompanyModal = ({ onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    name: '',
    registrationNumber: '',
    email: '',
    phone: '',
    address: '',
    country: 'Rwanda',
    locationId: ''
  });
  const [provinces, setProvinces] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchProvinces = async () => {
      try {
        const response = await apiCall('/locations/provinces');
        setProvinces(response);
      } catch (err) {
        console.error('Failed to fetch provinces:', err);
      }
    };
    fetchProvinces();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const companyData = {
        name: formData.name,
        registrationNumber: formData.registrationNumber,
        email: formData.email,
        phone: formData.phone,
        address: formData.address,
        country: formData.country,
        location: formData.locationId ? { id: parseInt(formData.locationId) } : null
      };
      
      await apiCall('/companies', {
        method: 'POST',
        body: JSON.stringify(companyData)
      });
      alert('Company added successfully!');
      onSuccess();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-xl shadow-2xl max-w-2xl w-full">
        <div className="p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-900">Add New Company</h2>
            <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
              <X className="w-6 h-6" />
            </button>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-3 mb-4 flex items-start gap-2">
              <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0 mt-0.5" />
              <p className="text-sm text-red-700">{error}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Company Name *</label>
                <input
                  type="text"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent"
                  placeholder="Viva Pharma Ltd"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Registration Number *</label>
                <input
                  type="text"
                  value={formData.registrationNumber}
                  onChange={(e) => setFormData({ ...formData, registrationNumber: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent"
                  placeholder="REG2024029"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Email *</label>
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent"
                  placeholder="company@example.com"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Phone *</label>
                <input
                  type="tel"
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent"
                  placeholder="0781234567"
                  required
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Address *</label>
              <input
                type="text"
                value={formData.address}
                onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent"
                placeholder="KG 18 Ave, Kigali"
                required
              />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Country *</label>
                <input
                  type="text"
                  value={formData.country}
                  onChange={(e) => setFormData({ ...formData, country: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent"
                  placeholder="Rwanda"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Location (Province)</label>
                <select
                  value={formData.locationId}
                  onChange={(e) => setFormData({ ...formData, locationId: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent"
                >
                  <option value="">Select Province (Optional)</option>
                  {provinces.map((province) => (
                    <option key={province.id} value={province.id}>
                      {province.name}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="flex gap-3 pt-4">
              <button
                type="submit"
                disabled={loading}
                className="flex-1 bg-teal-600 text-white py-2 rounded-lg hover:bg-teal-700 transition disabled:opacity-50 flex items-center justify-center gap-2"
              >
                {loading ? <Loader className="w-5 h-5 animate-spin" /> : <CheckCircle className="w-5 h-5" />}
                {loading ? 'Adding...' : 'Add Company'}
              </button>
              <button
                type="button"
                onClick={onClose}
                className="px-6 py-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

// Main App Component
// Public Product Catalog Component
const PublicCatalog = ({ onSwitchToLogin }) => {
  const [medicines, setMedicines] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('ALL');

  useEffect(() => {
    fetchMedicines();
  }, []);

  const fetchMedicines = async () => {
    setLoading(true);
    try {
      const response = await apiCall('/medicines/non-expired');
      setMedicines(response);
    } catch (err) {
      console.error('Failed to fetch medicines:', err);
    } finally {
      setLoading(false);
    }
  };

  const filteredMedicines = medicines.filter(med => {
    const matchesSearch = med.name.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory = selectedCategory === 'ALL' || med.category === selectedCategory;
    return matchesSearch && matchesCategory;
  });

  const categories = ['ALL', ...new Set(medicines.map(m => m.category))];

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
      {/* Header */}
      <nav className="bg-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center gap-2">
              <div className="w-10 h-10 bg-gradient-to-r from-blue-500 to-green-500 rounded-lg flex items-center justify-center">
                <Shield className="w-6 h-6 text-white" />
              </div>
              <h1 className="text-2xl font-bold bg-gradient-to-r from-blue-600 to-green-600 bg-clip-text text-transparent">
                Pharmacy Store
              </h1>
            </div>
            <button
              onClick={onSwitchToLogin}
              className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
            >
              Login
            </button>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <div className="bg-gradient-to-r from-blue-600 to-green-600 text-white py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-4xl font-bold mb-4">Welcome to Our Pharmacy</h2>
          <p className="text-xl mb-8">Browse our wide selection of quality medicines</p>
          
          {/* Search Bar */}
          <div className="max-w-2xl mx-auto">
            <div className="relative">
              <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-6 h-6 text-gray-400" />
              <input
                type="text"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                placeholder="Search medicines..."
                className="w-full pl-12 pr-4 py-4 rounded-lg text-gray-900 text-lg focus:ring-4 focus:ring-blue-300"
              />
            </div>
          </div>
        </div>
      </div>

      {/* Category Filter */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div className="flex gap-2 overflow-x-auto pb-2">
          {categories.map(category => (
            <button
              key={category}
              onClick={() => setSelectedCategory(category)}
              className={`px-4 py-2 rounded-lg font-medium whitespace-nowrap transition ${
                selectedCategory === category
                  ? 'bg-blue-600 text-white'
                  : 'bg-white text-gray-700 hover:bg-gray-100'
              }`}
            >
              {category}
            </button>
          ))}
        </div>
      </div>

      {/* Products Grid */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {loading ? (
          <div className="flex justify-center items-center h-64">
            <Loader className="w-12 h-12 animate-spin text-blue-600" />
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {filteredMedicines.map(medicine => (
              <div key={medicine.id} className="bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-2xl transition">
                <div className="bg-gradient-to-r from-blue-500 to-green-500 h-32 flex items-center justify-center">
                  <Package className="w-16 h-16 text-white" />
                </div>
                <div className="p-4">
                  <h3 className="text-lg font-bold text-gray-900 mb-2">{medicine.name}</h3>
                  <p className="text-sm text-gray-600 mb-2">{medicine.manufacturer}</p>
                  <div className="flex items-center justify-between mb-2">
                    <span className="px-2 py-1 bg-blue-100 text-blue-800 text-xs font-semibold rounded">
                      {medicine.category}
                    </span>
                    <span className={`px-2 py-1 text-xs font-semibold rounded ${
                      (medicine.quantity || 0) > 20 ? 'bg-green-100 text-green-800' : 'bg-orange-100 text-orange-800'
                    }`}>
                      {(medicine.quantity || 0) > 0 ? 'In Stock' : 'Out of Stock'}
                    </span>
                  </div>
                  <div className="flex items-center justify-between mt-4">
                    <span className="text-2xl font-bold text-green-600">RWF {Number(medicine.price || 0).toFixed(2)}</span>
                  </div>
                  {medicine.description && (
                    <p className="text-sm text-gray-500 mt-2 line-clamp-2">{medicine.description}</p>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}

        {!loading && filteredMedicines.length === 0 && (
          <div className="text-center py-12">
            <Package className="w-16 h-16 text-gray-400 mx-auto mb-4" />
            <p className="text-xl text-gray-600">No medicines found</p>
          </div>
        )}
      </div>

      {/* Footer */}
      <footer className="bg-gray-900 text-white py-8 mt-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <p className="text-lg mb-2">Contact us: 0781234567 | info@pharmacy.com</p>
          <p className="text-sm text-gray-400">© 2024 Pharmacy Management System. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default function App() {
  const [currentView, setCurrentView] = useState('catalog');
  const [user, setUser] = useState(null);
  const [pendingEmail, setPendingEmail] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('token');
    const savedUser = localStorage.getItem('user');
    if (token && savedUser) {
      setUser(JSON.parse(savedUser));
      setCurrentView('dashboard');
    }
  }, []);

  const handleLogin = (userData, nextView) => {
    setUser(userData);
    if (nextView === 'otp') {
      setPendingEmail(userData.email);
      setCurrentView('otp');
    } else {
      setCurrentView('dashboard');
    }
  };

  const handleSignup = (userData) => {
    setPendingEmail(userData.email);
    setCurrentView('otp');
  };

  const handleOTPVerify = (userData) => {
    setUser(userData);
    setCurrentView('dashboard');
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
    setCurrentView('login');
  };

  return (
    <>
      {currentView === 'catalog' && (
        <CustomerApp onStaffLogin={() => setCurrentView('login')} />
      )}

      {currentView === 'old-catalog' && (
        <PublicCatalog
          onSwitchToLogin={() => setCurrentView('login')}
        />
      )}

      {currentView === 'login' && (
        <LoginPage
          onLogin={handleLogin}
          onSwitchToSignup={() => setCurrentView('signup')}
          onSwitchToForgotPassword={() => setCurrentView('forgot-password')}
        />
      )}
      
      {currentView === 'signup' && (
        <SignupPage
          onSignup={handleSignup}
          onSwitchToLogin={() => setCurrentView('login')}
        />
      )}
      
      {currentView === 'otp' && (
        <OTPPage
          email={pendingEmail}
          onVerify={handleOTPVerify}
          onResend={() => {}}
        />
      )}
      
      {currentView === 'forgot-password' && (
        <ForgotPasswordPage
          onReset={() => setCurrentView('login')}
          onSwitchToLogin={() => setCurrentView('login')}
        />
      )}
      
      {currentView === 'dashboard' && user && (
        <Dashboard user={user} onLogout={handleLogout} />
      )}
    </>
  );
}