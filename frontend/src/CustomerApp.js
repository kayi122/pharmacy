import React, { useState, useEffect } from 'react';
import { ShoppingCart, Package, Phone, Mail, MapPin, Search, X, CheckCircle, Minus, Plus, Heart, Star, TrendingUp, Shield, Clock, Truck, Award, Zap, LogIn, LogOut, User } from 'lucide-react';

const API_BASE_URL = 'http://localhost:8080/api';

const CustomerApp = ({ onStaffLogin }) => {
  const [medicines, setMedicines] = useState([]);
  const [cart, setCart] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('ALL');
  const [showCart, setShowCart] = useState(false);
  const [showCheckout, setShowCheckout] = useState(false);
  const [loading, setLoading] = useState(false);
  const [customerInfo, setCustomerInfo] = useState({ name: '', phone: '', email: '', address: '' });
  const [paymentMethod, setPaymentMethod] = useState('CASH');
  const [deliveryMethod, setDeliveryMethod] = useState('PICKUP');
  const [favorites, setFavorites] = useState([]);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [showSignupModal, setShowSignupModal] = useState(false);
  const [loginForm, setLoginForm] = useState({ email: '', password: '' });
  const [signupForm, setSignupForm] = useState({ 
    firstName: '', 
    lastName: '', 
    email: '', 
    phone: '', 
    password: '',
    confirmPassword: '',
    address: '' 
  });
  const [user, setUser] = useState(null);
  const [authError, setAuthError] = useState('');

  useEffect(() => {
    fetchMedicines();
    // Load favorites from localStorage
    const savedFavorites = localStorage.getItem('favorites');
    if (savedFavorites) {
      setFavorites(JSON.parse(savedFavorites));
    }
    // Check if user is logged in
    const savedUser = localStorage.getItem('customerUser');
    const savedToken = localStorage.getItem('customerToken');
    if (savedUser && savedToken) {
      setUser(JSON.parse(savedUser));
      setIsLoggedIn(true);
    }
  }, []);

  const toggleFavorite = (medicineId) => {
    if (!isLoggedIn) {
      alert('Please login to add favorites');
      setShowLoginModal(true);
      return;
    }
    const newFavorites = favorites.includes(medicineId)
      ? favorites.filter(id => id !== medicineId)
      : [...favorites, medicineId];
    setFavorites(newFavorites);
    localStorage.setItem('favorites', JSON.stringify(newFavorites));
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    setAuthError('');
    
    if (signupForm.password !== signupForm.confirmPassword) {
      setAuthError('Passwords do not match');
      return;
    }

    if (signupForm.password.length < 8) {
      setAuthError('Password must be at least 8 characters');
      return;
    }

    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/auth/customer/signup`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          firstName: signupForm.firstName,
          lastName: signupForm.lastName,
          email: signupForm.email,
          phone: signupForm.phone,
          password: signupForm.password,
          address: signupForm.address
        })
      });

      const data = await response.json();
      
      if (!response.ok) {
        throw new Error(data.message || 'Signup failed');
      }

      // Save user data and token
      const userData = {
        id: data.userId,
        name: `${data.firstName} ${data.lastName}`,
        email: data.email,
        firstName: data.firstName,
        lastName: data.lastName
      };
      
      setUser(userData);
      setIsLoggedIn(true);
      localStorage.setItem('customerUser', JSON.stringify(userData));
      localStorage.setItem('customerToken', data.token);
      
      setShowSignupModal(false);
      setSignupForm({ 
        firstName: '', 
        lastName: '', 
        email: '', 
        phone: '', 
        password: '',
        confirmPassword: '',
        address: '' 
      });
      alert('Account created successfully!');
    } catch (error) {
      setAuthError(error.message || 'Signup failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setAuthError('');
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/auth/customer/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(loginForm)
      });

      const data = await response.json();
      
      if (!response.ok) {
        throw new Error(data.message || 'Login failed');
      }

      // Save user data and token
      const userData = {
        id: data.userId,
        name: `${data.firstName} ${data.lastName}`,
        email: data.email,
        firstName: data.firstName,
        lastName: data.lastName
      };
      
      setUser(userData);
      setIsLoggedIn(true);
      localStorage.setItem('customerUser', JSON.stringify(userData));
      localStorage.setItem('customerToken', data.token);
      
      setShowLoginModal(false);
      setLoginForm({ email: '', password: '' });
      alert('Login successful!');
    } catch (error) {
      setAuthError(error.message || 'Login failed. Please check your credentials.');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    setUser(null);
    setIsLoggedIn(false);
    localStorage.removeItem('customerUser');
    localStorage.removeItem('customerToken');
    setCart([]);
    setFavorites([]);
    localStorage.removeItem('favorites');
    alert('Logged out successfully!');
  };

  const fetchMedicines = async () => {
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/medicines`);
      const data = await response.json();
      console.log('Fetched medicines:', data);
      const available = data.filter(m => m.quantity > 0 && !m.isExpired);
      console.log('Available medicines:', available);
      setMedicines(available);
    } catch (error) {
      console.error('Error fetching medicines:', error);
    } finally {
      setLoading(false);
    }
  };

  const addToCart = (medicine) => {
    if (!isLoggedIn) {
      alert('Please login to add items to cart');
      setShowLoginModal(true);
      return;
    }
    
    const existing = cart.find(item => item.id === medicine.id);
    if (existing) {
      setCart(cart.map(item => 
        item.id === medicine.id 
          ? { ...item, quantity: Math.min(item.quantity + 1, medicine.quantity) }
          : item
      ));
    } else {
      setCart([...cart, { ...medicine, quantity: 1 }]);
    }
  };

  const updateQuantity = (id, change) => {
    setCart(cart.map(item => {
      if (item.id === id) {
        const medicine = medicines.find(m => m.id === id);
        const newQty = item.quantity + change;
        return { ...item, quantity: Math.max(1, Math.min(newQty, medicine.quantity)) };
      }
      return item;
    }).filter(item => item.quantity > 0));
  };

  const removeFromCart = (id) => {
    setCart(cart.filter(item => item.id !== id));
  };

  const getTotal = () => {
    return cart.reduce((sum, item) => sum + (item.sellingPrice * item.quantity), 0);
  };

  const handleCheckout = async () => {
    if (!isLoggedIn) {
      alert('Please login to place an order');
      setShowCheckout(false);
      setShowLoginModal(true);
      return;
    }

    if (!customerInfo.name || !customerInfo.phone) {
      alert('Please provide your name and phone number');
      return;
    }

    setLoading(true);
    try {
      const token = localStorage.getItem('customerToken');
      const userId = user?.id || null;
      
      const sales = cart.map(item => ({
        medicine: { id: item.id },
        user: userId ? { id: userId } : null,
        quantity: item.quantity,
        totalPrice: item.sellingPrice * item.quantity,
        customerName: user?.name || customerInfo.name,
        customerPhone: customerInfo.phone,
        paymentMethod: paymentMethod,
        saleDate: new Date().toISOString()
      }));

      for (const sale of sales) {
        await fetch(`${API_BASE_URL}/sales`, {
          method: 'POST',
          headers: { 
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify(sale)
        });
      }

      printReceipt();
      setCart([]);
      setShowCheckout(false);
      setShowCart(false);
      alert('Order placed successfully!');
    } catch (error) {
      alert('Failed to place order. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const printReceipt = () => {
    const printWindow = window.open('', '', 'width=800,height=600');
    printWindow.document.write(`
      <html>
        <head>
          <title>Receipt</title>
          <style>
            body { font-family: Arial, sans-serif; padding: 40px; }
            .header { text-align: center; margin-bottom: 30px; border-bottom: 2px solid #333; padding-bottom: 20px; }
            .header h1 { margin: 0; color: #2563eb; }
            .info { margin: 20px 0; }
            .info p { margin: 5px 0; }
            table { width: 100%; border-collapse: collapse; margin: 20px 0; }
            th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
            th { background-color: #2563eb; color: white; }
            .total { text-align: right; font-size: 20px; font-weight: bold; margin-top: 20px; }
            .footer { text-align: center; margin-top: 40px; color: #666; }
          </style>
        </head>
        <body>
          <div class="header">
            <h1>Wellness Pharmacy</h1>
            <p>Thank you for your purchase!</p>
          </div>
          <div class="info">
            <p><strong>Customer:</strong> ${customerInfo.name}</p>
            <p><strong>Phone:</strong> ${customerInfo.phone}</p>
            <p><strong>Date:</strong> ${new Date().toLocaleString()}</p>
          </div>
          <table>
            <thead>
              <tr>
                <th>Medicine</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>Total</th>
              </tr>
            </thead>
            <tbody>
              ${cart.map(item => `
                <tr>
                  <td>${item.name}</td>
                  <td>${item.quantity}</td>
                  <td>RWF ${item.sellingPrice.toFixed(2)}</td>
                  <td>RWF ${(item.sellingPrice * item.quantity).toFixed(2)}</td>
                </tr>
              `).join('')}
            </tbody>
          </table>
          <div class="total">
            Total: RWF ${getTotal().toFixed(2)}
          </div>
          <div class="footer">
            <p>Thank you for choosing Wellness Pharmacy!</p>
            <p>For inquiries: 0788123456 | info@wellnesspharmacy.com</p>
          </div>
        </body>
      </html>
    `);
    printWindow.document.close();
    printWindow.print();
  };

  const filteredMedicines = medicines.filter(m => {
    const matchesSearch = !searchTerm || 
      m.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      m.description?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      m.category?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory = selectedCategory === 'ALL' || m.category === selectedCategory;
    return matchesSearch && matchesCategory;
  });

  const categories = ['ALL', ...new Set(medicines.map(m => m.category))];

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50 to-indigo-50">
      {/* Top Bar */}
      <div className="bg-gradient-to-r from-slate-800 to-slate-900 text-white py-2">
        <div className="max-w-7xl mx-auto px-4 flex justify-between items-center text-sm">
          <div className="flex items-center gap-6">
            <span className="flex items-center gap-2">
              <Phone className="w-4 h-4" />
              +250 788 123 456
            </span>
            <span className="hidden md:flex items-center gap-2">
              <Mail className="w-4 h-4" />
              info@wellnesspharmacy.com
            </span>
          </div>
          <div className="flex items-center gap-4">
            <span className="hidden sm:inline text-emerald-300">🕐 Open: Mon-Sat 8AM-8PM</span>
            <button onClick={onStaffLogin} className="text-emerald-300 hover:text-emerald-200 transition font-medium">
              Staff Portal
            </button>
          </div>
        </div>
      </div>

      {/* Main Header */}
      <header className="bg-white shadow-lg sticky top-0 z-50 border-b-4 border-emerald-500">
        <div className="max-w-7xl mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <div className="relative">
                <div className="w-14 h-14 bg-gradient-to-br from-emerald-500 via-teal-500 to-cyan-600 rounded-2xl flex items-center justify-center shadow-lg transform rotate-3">
                  <Package className="w-8 h-8 text-white transform -rotate-3" />
                </div>
                <div className="absolute -top-1 -right-1 w-4 h-4 bg-amber-400 rounded-full animate-pulse"></div>
              </div>
              <div>
                <h1 className="text-3xl font-extrabold bg-gradient-to-r from-emerald-600 via-teal-600 to-cyan-600 bg-clip-text text-transparent">
                  Wellness Pharmacy
                </h1>
                <p className="text-sm text-slate-600 font-medium">Trusted Healthcare Partner Since 2020</p>
              </div>
            </div>
            <div className="flex items-center gap-3">
              <button className="hidden md:flex items-center gap-2 px-4 py-2 rounded-xl bg-slate-100 hover:bg-slate-200 transition-all duration-200">
                <Clock className="w-5 h-5 text-emerald-600" />
                <span className="text-sm font-medium text-slate-700">24/7 Support</span>
              </button>
              
              {isLoggedIn ? (
                <div className="flex items-center gap-3">
                  <div className="hidden md:flex items-center gap-2 px-4 py-2 bg-emerald-50 rounded-xl">
                    <User className="w-5 h-5 text-emerald-600" />
                    <span className="text-sm font-semibold text-emerald-700">{user?.name || user?.email}</span>
                  </div>
                  <button
                    onClick={handleLogout}
                    className="flex items-center gap-2 px-4 py-2 rounded-xl bg-rose-100 hover:bg-rose-200 text-rose-700 transition-all duration-200 font-semibold"
                  >
                    <LogOut className="w-5 h-5" />
                    <span className="hidden sm:inline">Logout</span>
                  </button>
                </div>
              ) : (
                <button
                  onClick={() => setShowLoginModal(true)}
                  className="flex items-center gap-2 px-4 py-2 rounded-xl bg-emerald-100 hover:bg-emerald-200 text-emerald-700 transition-all duration-200 font-semibold"
                >
                  <LogIn className="w-5 h-5" />
                  <span className="hidden sm:inline">Login</span>
                </button>
              )}
              
              <button
                onClick={() => setShowCart(true)}
                className="relative bg-gradient-to-r from-emerald-500 to-teal-600 text-white px-6 py-3 rounded-xl hover:from-emerald-600 hover:to-teal-700 transition-all duration-300 flex items-center gap-2 shadow-lg hover:shadow-xl transform hover:scale-105"
              >
                <ShoppingCart className="w-5 h-5" />
                <span className="font-semibold">Cart</span>
                {cart.length > 0 && (
                  <span className="absolute -top-2 -right-2 bg-rose-500 text-white w-7 h-7 rounded-full flex items-center justify-center text-xs font-bold shadow-lg animate-bounce">
                    {cart.length}
                  </span>
                )}
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <div className="relative overflow-hidden bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 text-white">
        <div className="absolute inset-0 opacity-10">
          <div className="absolute inset-0 bg-gradient-to-r from-emerald-500/20 to-cyan-500/20"></div>
          <div className="absolute top-0 left-0 w-96 h-96 bg-emerald-500 rounded-full blur-3xl opacity-20 animate-pulse"></div>
          <div className="absolute bottom-0 right-0 w-96 h-96 bg-cyan-500 rounded-full blur-3xl opacity-20 animate-pulse delay-700"></div>
        </div>
        <div className="relative max-w-7xl mx-auto px-4 py-20">
          <div className="text-center mb-10">
            <h2 className="text-6xl font-black mb-6 leading-tight">
              Your Health,
              <span className="block bg-gradient-to-r from-emerald-400 via-teal-400 to-cyan-400 bg-clip-text text-transparent">
                Our Mission
              </span>
            </h2>
            <p className="text-xl text-slate-300 mb-8 max-w-3xl mx-auto leading-relaxed">
              Experience hassle-free medicine shopping with verified products, expert guidance, 
              and fast delivery across Rwanda. Quality healthcare at your fingertips.
            </p>
          </div>

          {/* Search Bar */}
          <div className="max-w-3xl mx-auto mb-12">
            <div className="relative group">
              <div className="absolute inset-0 bg-gradient-to-r from-emerald-500 to-cyan-500 rounded-2xl blur-xl opacity-50 group-hover:opacity-75 transition duration-300"></div>
              <div className="relative flex items-center bg-white rounded-2xl shadow-2xl overflow-hidden">
                <Search className="absolute left-6 w-6 h-6 text-slate-400" />
                <input
                  type="text"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  placeholder="Search for medicines, vitamins, supplements..."
                  className="w-full pl-16 pr-6 py-5 text-lg text-slate-900 focus:outline-none"
                />
                <button className="bg-gradient-to-r from-emerald-500 to-teal-600 text-white px-8 py-5 font-semibold hover:from-emerald-600 hover:to-teal-700 transition-all">
                  Search
                </button>
              </div>
            </div>
          </div>

          {/* Trust Badges */}
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6 max-w-4xl mx-auto">
            <div className="bg-white/10 backdrop-blur-md rounded-2xl p-4 text-center border border-white/20 hover:bg-white/20 transition duration-300">
              <Shield className="w-8 h-8 text-emerald-400 mx-auto mb-2" />
              <p className="text-sm font-semibold">100% Authentic</p>
            </div>
            <div className="bg-white/10 backdrop-blur-md rounded-2xl p-4 text-center border border-white/20 hover:bg-white/20 transition duration-300">
              <Truck className="w-8 h-8 text-emerald-400 mx-auto mb-2" />
              <p className="text-sm font-semibold">Fast Delivery</p>
            </div>
            <div className="bg-white/10 backdrop-blur-md rounded-2xl p-4 text-center border border-white/20 hover:bg-white/20 transition duration-300">
              <Award className="w-8 h-8 text-emerald-400 mx-auto mb-2" />
              <p className="text-sm font-semibold">Licensed</p>
            </div>
            <div className="bg-white/10 backdrop-blur-md rounded-2xl p-4 text-center border border-white/20 hover:bg-white/20 transition duration-300">
              <Zap className="w-8 h-8 text-emerald-400 mx-auto mb-2" />
              <p className="text-sm font-semibold">24/7 Support</p>
            </div>
          </div>
        </div>
      </div>

      {/* Categories */}
      <div className="bg-white py-8 shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-2xl font-bold text-slate-900 flex items-center gap-2">
              <span className="w-2 h-8 bg-gradient-to-b from-emerald-500 to-teal-600 rounded-full"></span>
              Shop by Category
            </h2>
            <button className="text-emerald-600 hover:text-emerald-700 font-semibold text-sm flex items-center gap-1">
              View All
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
              </svg>
            </button>
          </div>
          <div className="flex gap-3 overflow-x-auto pb-2 scrollbar-hide">
            {categories.map(cat => (
              <button
                key={cat}
                onClick={() => setSelectedCategory(cat)}
                className={`px-6 py-3 rounded-xl font-bold whitespace-nowrap transition-all duration-300 transform hover:scale-105 ${
                  selectedCategory === cat
                    ? 'bg-gradient-to-r from-emerald-500 to-teal-600 text-white shadow-lg shadow-emerald-500/50'
                    : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                }`}
              >
                {cat === 'ALL' ? '🏠 All' : `💊 ${cat.replace('_', ' ')}`}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Featured Section */}
      <div className="bg-gradient-to-br from-amber-50 to-orange-50 py-12">
        <div className="max-w-7xl mx-auto px-4">
          <div className="grid md:grid-cols-3 gap-6">
            <div className="bg-white rounded-2xl p-6 shadow-lg border-2 border-amber-200 hover:shadow-xl transition duration-300">
              <div className="flex items-start gap-4">
                <div className="w-12 h-12 bg-gradient-to-br from-amber-400 to-orange-500 rounded-xl flex items-center justify-center flex-shrink-0">
                  <TrendingUp className="w-6 h-6 text-white" />
                </div>
                <div>
                  <h3 className="font-bold text-lg text-slate-900 mb-1">Trending Products</h3>
                  <p className="text-sm text-slate-600">Most popular this week</p>
                </div>
              </div>
            </div>
            <div className="bg-white rounded-2xl p-6 shadow-lg border-2 border-rose-200 hover:shadow-xl transition duration-300">
              <div className="flex items-start gap-4">
                <div className="w-12 h-12 bg-gradient-to-br from-rose-400 to-pink-500 rounded-xl flex items-center justify-center flex-shrink-0">
                  <Heart className="w-6 h-6 text-white" />
                </div>
                <div>
                  <h3 className="font-bold text-lg text-slate-900 mb-1">Favorites</h3>
                  <p className="text-sm text-slate-600">{favorites.length} items saved</p>
                </div>
              </div>
            </div>
            <div className="bg-white rounded-2xl p-6 shadow-lg border-2 border-emerald-200 hover:shadow-xl transition duration-300">
              <div className="flex items-start gap-4">
                <div className="w-12 h-12 bg-gradient-to-br from-emerald-400 to-teal-500 rounded-xl flex items-center justify-center flex-shrink-0">
                  <Star className="w-6 h-6 text-white" />
                </div>
                <div>
                  <h3 className="font-bold text-lg text-slate-900 mb-1">Top Rated</h3>
                  <p className="text-sm text-slate-600">Customer favorites</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Products Grid */}
      <div className="max-w-7xl mx-auto px-4 py-12">
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-3xl font-bold text-slate-900">Available Medicines</h2>
            <p className="text-slate-600 mt-1">{filteredMedicines.length} products found</p>
          </div>
          <select className="px-4 py-2 border-2 border-slate-200 rounded-xl font-medium text-slate-700 focus:border-emerald-500 focus:outline-none">
            <option>Sort by: Featured</option>
            <option>Price: Low to High</option>
            <option>Price: High to Low</option>
            <option>Name: A to Z</option>
          </select>
        </div>

        {loading ? (
          <div className="flex justify-center items-center h-96">
            <div className="text-center">
              <div className="relative w-20 h-20 mx-auto mb-4">
                <div className="absolute inset-0 border-4 border-emerald-200 rounded-full"></div>
                <div className="absolute inset-0 border-4 border-emerald-600 rounded-full border-t-transparent animate-spin"></div>
              </div>
              <p className="text-slate-600 font-medium">Loading medicines...</p>
            </div>
          </div>
        ) : filteredMedicines.length === 0 ? (
          <div className="text-center py-20 bg-white rounded-3xl shadow-lg">
            <div className="w-24 h-24 bg-gradient-to-br from-slate-100 to-slate-200 rounded-full flex items-center justify-center mx-auto mb-6">
              <Package className="w-12 h-12 text-slate-400" />
            </div>
            <h3 className="text-2xl font-bold text-slate-900 mb-2">No Medicines Found</h3>
            <p className="text-slate-600 mb-6 max-w-md mx-auto">
              We couldn't find any medicines matching your search. Try different keywords or browse by category.
            </p>
            <button 
              onClick={onStaffLogin} 
              className="px-6 py-3 bg-gradient-to-r from-emerald-500 to-teal-600 text-white rounded-xl hover:from-emerald-600 hover:to-teal-700 transition font-semibold shadow-lg"
            >
              Staff Login to Add Products
            </button>
          </div>
        ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {filteredMedicines.map((medicine, index) => {
            const getImageForMedicine = (name, category, idx) => {
              const nameLower = name.toLowerCase();
              if (nameLower.includes('paracetamol')) return '/images/painkillers.jpg';
              if (nameLower.includes('amoxicillin')) return '/images/Amoxicillin.jpg';
              if (nameLower.includes('vitamin')) return '/images/VitaminD3.jpg';
              if (nameLower.includes('metformin')) return '/images/metformin_sr.jpg';
              
              const categoryImages = {
                'ANTIBIOTIC': '/images/antibiotic.jpg',
                'PAINKILLER': '/images/painkillers.jpg',
                'ANTIVIRAL': '/images/antivira.jpg',
                'ANTIFUNGAL': '/images/antifugal.jpg',
                'ANTIHISTAMINE': '/images/antibiotic.jpg',
                'CARDIOVASCULAR': '/images/metformin_sr.jpg',
                'DIABETES': '/images/metformin_sr.jpg',
                'VITAMINS': '/images/VitaminD3.jpg',
                'SUPPLEMENTS': '/images/VitaminD3.jpg',
                'ANTISEPTIC': '/images/antifugal.jpg',
                'OTHER': '/images/antibiotic.jpg'
              };
              
              return categoryImages[category] || '/images/antibiotic.jpg';
            };

            const isFavorite = favorites.includes(medicine.id);
            
            return (
              <div key={medicine.id} className="group bg-white rounded-2xl shadow-lg overflow-hidden hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-2 border-2 border-transparent hover:border-emerald-200">
                <div className="relative h-56 overflow-hidden bg-gradient-to-br from-slate-100 to-slate-200">
                  <img 
                    src={getImageForMedicine(medicine.name, medicine.category, index)}
                    alt={medicine.name}
                    className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
                    onError={(e) => {
                      e.target.style.display = 'none';
                      e.target.parentElement.classList.add('bg-gradient-to-br', 'from-emerald-100', 'to-teal-100');
                    }}
                  />
                  <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-black/20 to-transparent"></div>
                  
                  {/* Favorite Button */}
                  <button
                    onClick={() => toggleFavorite(medicine.id)}
                    className={`absolute top-3 right-3 w-10 h-10 rounded-full flex items-center justify-center transition-all duration-300 ${
                      isFavorite 
                        ? 'bg-rose-500 text-white shadow-lg scale-110' 
                        : 'bg-white/90 text-slate-400 hover:text-rose-500 hover:bg-white'
                    }`}
                  >
                    <Heart className={`w-5 h-5 ${isFavorite ? 'fill-current' : ''}`} />
                  </button>

                  {/* Stock Badge */}
                  <div className="absolute bottom-3 left-3">
                    <span className={`px-3 py-1 rounded-full text-xs font-bold backdrop-blur-md ${
                      medicine.quantity > 50 
                        ? 'bg-emerald-500/90 text-white' 
                        : medicine.quantity > 10 
                        ? 'bg-amber-500/90 text-white' 
                        : 'bg-rose-500/90 text-white'
                    }`}>
                      {medicine.quantity > 50 ? '✓ In Stock' : medicine.quantity > 10 ? '⚠ Low Stock' : '🔥 Almost Gone'}
                    </span>
                  </div>
                </div>

                <div className="p-5">
                  <div className="mb-3">
                    <span className="inline-block px-3 py-1 bg-gradient-to-r from-emerald-100 to-teal-100 text-emerald-700 text-xs font-bold rounded-lg border border-emerald-200">
                      {medicine.category}
                    </span>
                  </div>
                  
                  <h3 className="text-lg font-bold text-slate-900 mb-2 line-clamp-1 group-hover:text-emerald-600 transition-colors">
                    {medicine.name}
                  </h3>
                  
                  {medicine.description && (
                    <p className="text-sm text-slate-600 mb-3 line-clamp-2 leading-relaxed">
                      {medicine.description}
                    </p>
                  )}

                  {medicine.company && (
                    <p className="text-xs text-slate-500 mb-3 flex items-center gap-1">
                      <Shield className="w-3 h-3" />
                      {medicine.company.name}
                    </p>
                  )}

                  <div className="flex items-center justify-between pt-3 border-t border-slate-100">
                    <div>
                      <p className="text-xs text-slate-500 mb-1">Price</p>
                      <span className="text-2xl font-black bg-gradient-to-r from-emerald-600 to-teal-600 bg-clip-text text-transparent">
                        RWF {medicine.sellingPrice.toFixed(2)}
                      </span>
                    </div>
                    <button
                      onClick={() => addToCart(medicine)}
                      className="bg-gradient-to-r from-emerald-500 to-teal-600 text-white px-5 py-2.5 rounded-xl hover:from-emerald-600 hover:to-teal-700 transition-all duration-300 font-bold shadow-lg hover:shadow-xl transform hover:scale-105 flex items-center gap-2"
                    >
                      <ShoppingCart className="w-4 h-4" />
                      Add
                    </button>
                  </div>

                  {/* Rating Stars */}
                  <div className="flex items-center gap-1 mt-3 pt-3 border-t border-slate-100">
                    {[1, 2, 3, 4, 5].map((star) => (
                      <Star key={star} className="w-4 h-4 fill-amber-400 text-amber-400" />
                    ))}
                    <span className="text-xs text-slate-600 ml-2">(4.8)</span>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
        )}
      </div>

      {/* Cart Sidebar */}
      {showCart && (
        <div className="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex justify-end">
          <div className="w-full max-w-lg bg-white shadow-2xl flex flex-col h-full">
            <div className="flex justify-between items-center p-6 border-b-2 border-slate-200 bg-gradient-to-r from-emerald-50 to-teal-50">
              <div>
                <h2 className="text-2xl font-black text-slate-900">Shopping Cart</h2>
                <p className="text-sm text-slate-600">{cart.length} {cart.length === 1 ? 'item' : 'items'}</p>
              </div>
              <button 
                onClick={() => setShowCart(false)} 
                className="w-10 h-10 bg-white rounded-full flex items-center justify-center hover:bg-slate-100 transition shadow-lg"
              >
                <X className="w-6 h-6 text-slate-600" />
              </button>
            </div>

            <div className="flex-1 overflow-y-auto p-6 bg-gradient-to-br from-slate-50 to-white">
              {cart.length === 0 ? (
                <div className="flex flex-col items-center justify-center h-full text-center">
                  <div className="w-24 h-24 bg-gradient-to-br from-slate-100 to-slate-200 rounded-full flex items-center justify-center mb-4">
                    <ShoppingCart className="w-12 h-12 text-slate-400" />
                  </div>
                  <h3 className="text-xl font-bold text-slate-900 mb-2">Your cart is empty</h3>
                  <p className="text-slate-600">Start shopping to add items to your cart</p>
                </div>
              ) : (
                <div className="space-y-4">
                  {cart.map(item => (
                    <div key={item.id} className="bg-white rounded-xl p-4 shadow-md border-2 border-slate-100 hover:border-emerald-200 transition-all duration-300">
                      <div className="flex gap-4">
                        <div className="w-20 h-20 bg-gradient-to-br from-emerald-100 to-teal-100 rounded-lg flex items-center justify-center flex-shrink-0">
                          <Package className="w-8 h-8 text-emerald-600" />
                        </div>
                        <div className="flex-1">
                          <div className="flex justify-between items-start mb-2">
                            <h3 className="font-bold text-slate-900 text-sm leading-tight pr-2">{item.name}</h3>
                            <button 
                              onClick={() => removeFromCart(item.id)} 
                              className="text-rose-500 hover:text-rose-700 transition flex-shrink-0"
                            >
                              <X className="w-5 h-5" />
                            </button>
                          </div>
                          <p className="text-sm text-slate-600 mb-3">RWF {item.sellingPrice.toFixed(2)} each</p>
                          <div className="flex items-center justify-between">
                            <div className="flex items-center gap-2">
                              <button
                                onClick={() => updateQuantity(item.id, -1)}
                                className="w-8 h-8 bg-slate-100 rounded-lg flex items-center justify-center hover:bg-emerald-100 hover:text-emerald-600 transition-all"
                              >
                                <Minus className="w-4 h-4" />
                              </button>
                              <span className="w-10 text-center font-bold text-slate-900">{item.quantity}</span>
                              <button
                                onClick={() => updateQuantity(item.id, 1)}
                                className="w-8 h-8 bg-slate-100 rounded-lg flex items-center justify-center hover:bg-emerald-100 hover:text-emerald-600 transition-all"
                              >
                                <Plus className="w-4 h-4" />
                              </button>
                            </div>
                            <span className="font-black text-emerald-600">
                              RWF {(item.sellingPrice * item.quantity).toFixed(2)}
                            </span>
                          </div>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>

            {cart.length > 0 && (
              <div className="border-t-2 border-slate-200 p-6 bg-white shadow-2xl">
                <div className="mb-4">
                  <label className="block text-sm font-bold text-slate-700 mb-2">Payment Method</label>
                  <select
                    value={paymentMethod}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                    className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium text-slate-700"
                  >
                    <option value="CASH">💵 Cash on Delivery</option>
                    <option value="MOBILE_MONEY">📱 Mobile Money (MTN/Airtel)</option>
                    <option value="CARD">💳 Credit/Debit Card</option>
                    <option value="BANK_TRANSFER">🏦 Bank Transfer</option>
                  </select>
                </div>
                
                <div className="bg-gradient-to-br from-slate-50 to-slate-100 rounded-xl p-4 mb-4">
                  <div className="flex justify-between items-center mb-2">
                    <span className="text-slate-600">Subtotal</span>
                    <span className="font-bold text-slate-900">RWF {getTotal().toFixed(2)}</span>
                  </div>
                  <div className="flex justify-between items-center mb-2">
                    <span className="text-slate-600">Delivery</span>
                    <span className="font-bold text-emerald-600">FREE</span>
                  </div>
                  <div className="border-t-2 border-slate-200 mt-3 pt-3 flex justify-between items-center">
                    <span className="text-xl font-black text-slate-900">Total</span>
                    <span className="text-2xl font-black bg-gradient-to-r from-emerald-600 to-teal-600 bg-clip-text text-transparent">
                      RWF {getTotal().toFixed(2)}
                    </span>
                  </div>
                </div>
                
                <button
                  onClick={() => { setShowCart(false); setShowCheckout(true); }}
                  className="w-full bg-gradient-to-r from-emerald-500 to-teal-600 text-white py-4 rounded-xl hover:from-emerald-600 hover:to-teal-700 transition-all duration-300 font-black text-lg shadow-lg hover:shadow-xl transform hover:scale-105 flex items-center justify-center gap-2"
                >
                  Proceed to Checkout
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7l5 5m0 0l-5 5m5-5H6" />
                  </svg>
                </button>
              </div>
            )}
          </div>
        </div>
      )}

      {/* Checkout Modal */}
      {showCheckout && (
        <div className="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl max-w-3xl w-full max-h-[90vh] overflow-y-auto">
            <div className="sticky top-0 bg-gradient-to-r from-emerald-50 to-teal-50 border-b-2 border-slate-200 p-6 rounded-t-3xl z-10">
              <div className="flex justify-between items-center">
                <div>
                  <h2 className="text-3xl font-black text-slate-900">Checkout</h2>
                  <p className="text-sm text-slate-600 mt-1">Complete your purchase</p>
                </div>
                <button 
                  onClick={() => setShowCheckout(false)} 
                  className="w-10 h-10 bg-white rounded-full flex items-center justify-center hover:bg-slate-100 transition shadow-lg"
                >
                  <X className="w-6 h-6 text-slate-600" />
                </button>
              </div>
            </div>

            <div className="p-6">
              <div className="grid md:grid-cols-2 gap-6 mb-6">
                <div>
                  <label className="block text-sm font-bold text-slate-700 mb-2 flex items-center gap-2">
                    <svg className="w-4 h-4 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                    </svg>
                    Full Name *
                  </label>
                  <input
                    type="text"
                    value={customerInfo.name}
                    onChange={(e) => setCustomerInfo({ ...customerInfo, name: e.target.value })}
                    className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium transition-all"
                    placeholder="John Doe"
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-bold text-slate-700 mb-2 flex items-center gap-2">
                    <Phone className="w-4 h-4 text-emerald-600" />
                    Phone Number *
                  </label>
                  <input
                    type="tel"
                    value={customerInfo.phone}
                    onChange={(e) => setCustomerInfo({ ...customerInfo, phone: e.target.value })}
                    className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium transition-all"
                    placeholder="+250 788 123 456"
                    required
                  />
                </div>
              </div>

              <div className="mb-6">
                <label className="block text-sm font-bold text-slate-700 mb-2 flex items-center gap-2">
                  <Mail className="w-4 h-4 text-emerald-600" />
                  Email (Optional)
                </label>
                <input
                  type="email"
                  value={customerInfo.email}
                  onChange={(e) => setCustomerInfo({ ...customerInfo, email: e.target.value })}
                  className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium transition-all"
                  placeholder="john@example.com"
                />
              </div>

              <div className="mb-6">
                <label className="block text-sm font-bold text-slate-700 mb-2 flex items-center gap-2">
                  <Truck className="w-4 h-4 text-emerald-600" />
                  Delivery Method *
                </label>
                <div className="grid md:grid-cols-2 gap-4">
                  <button
                    onClick={() => setDeliveryMethod('PICKUP')}
                    className={`p-4 rounded-xl border-2 font-semibold transition-all duration-300 ${
                      deliveryMethod === 'PICKUP'
                        ? 'border-emerald-500 bg-emerald-50 text-emerald-700'
                        : 'border-slate-200 hover:border-slate-300'
                    }`}
                  >
                    <Package className="w-6 h-6 mx-auto mb-2" />
                    Pickup from Pharmacy
                    <p className="text-xs mt-1 opacity-75">FREE</p>
                  </button>
                  <button
                    onClick={() => setDeliveryMethod('HOME_DELIVERY')}
                    className={`p-4 rounded-xl border-2 font-semibold transition-all duration-300 ${
                      deliveryMethod === 'HOME_DELIVERY'
                        ? 'border-emerald-500 bg-emerald-50 text-emerald-700'
                        : 'border-slate-200 hover:border-slate-300'
                    }`}
                  >
                    <Truck className="w-6 h-6 mx-auto mb-2" />
                    Home Delivery
                    <p className="text-xs mt-1 opacity-75">RWF 2,000</p>
                  </button>
                </div>
              </div>

              {deliveryMethod === 'HOME_DELIVERY' && (
                <div className="mb-6">
                  <label className="block text-sm font-bold text-slate-700 mb-2 flex items-center gap-2">
                    <MapPin className="w-4 h-4 text-emerald-600" />
                    Delivery Address *
                  </label>
                  <textarea
                    value={customerInfo.address}
                    onChange={(e) => setCustomerInfo({ ...customerInfo, address: e.target.value })}
                    className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium transition-all"
                    rows="3"
                    placeholder="Enter your full delivery address with landmarks"
                    required={deliveryMethod === 'HOME_DELIVERY'}
                  />
                </div>
              )}

              <div className="bg-gradient-to-br from-slate-50 to-slate-100 rounded-2xl p-6 mb-6 border-2 border-slate-200">
                <h3 className="font-black text-lg text-slate-900 mb-4 flex items-center gap-2">
                  <ShoppingCart className="w-5 h-5 text-emerald-600" />
                  Order Summary
                </h3>
                <div className="space-y-3 mb-4">
                  {cart.map(item => (
                    <div key={item.id} className="flex justify-between text-sm">
                      <span className="text-slate-700">
                        {item.name} <span className="font-bold text-emerald-600">× {item.quantity}</span>
                      </span>
                      <span className="font-bold text-slate-900">RWF {(item.sellingPrice * item.quantity).toFixed(2)}</span>
                    </div>
                  ))}
                </div>
                <div className="border-t-2 border-slate-200 pt-4 space-y-2">
                  <div className="flex justify-between text-sm">
                    <span className="text-slate-600">Subtotal</span>
                    <span className="font-bold text-slate-900">RWF {getTotal().toFixed(2)}</span>
                  </div>
                  <div className="flex justify-between text-sm">
                    <span className="text-slate-600">Payment Method</span>
                    <span className="font-semibold text-slate-900">{paymentMethod.replace('_', ' ')}</span>
                  </div>
                  <div className="flex justify-between text-sm">
                    <span className="text-slate-600">Delivery</span>
                    <span className="font-semibold text-emerald-600">
                      {deliveryMethod === 'PICKUP' ? 'FREE' : 'RWF 2,000.00'}
                    </span>
                  </div>
                  <div className="flex justify-between items-center pt-3 border-t-2 border-slate-300">
                    <span className="text-lg font-black text-slate-900">Total</span>
                    <span className="text-2xl font-black bg-gradient-to-r from-emerald-600 to-teal-600 bg-clip-text text-transparent">
                      RWF {(getTotal() + (deliveryMethod === 'HOME_DELIVERY' ? 2000 : 0)).toFixed(2)}
                    </span>
                  </div>
                </div>
              </div>

              <button
                onClick={handleCheckout}
                disabled={loading}
                className="w-full bg-gradient-to-r from-emerald-500 to-teal-600 text-white py-4 rounded-xl hover:from-emerald-600 hover:to-teal-700 transition-all duration-300 font-black text-lg shadow-lg hover:shadow-xl transform hover:scale-105 flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none"
              >
                {loading ? (
                  <>
                    <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                    Processing...
                  </>
                ) : (
                  <>
                    <CheckCircle className="w-5 h-5" />
                    Place Order
                  </>
                )}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Login Modal */}
      {showLoginModal && (
        <div className="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl max-w-md w-full">
            <div className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b-2 border-slate-200 p-6 rounded-t-3xl">
              <div className="flex justify-between items-center">
                <div>
                  <h2 className="text-3xl font-black text-slate-900">Customer Login</h2>
                  <p className="text-sm text-slate-600 mt-1">Sign in to your account</p>
                </div>
                <button 
                  onClick={() => { setShowLoginModal(false); setAuthError(''); }} 
                  className="w-10 h-10 bg-white rounded-full flex items-center justify-center hover:bg-slate-100 transition shadow-lg"
                >
                  <X className="w-6 h-6 text-slate-600" />
                </button>
              </div>
            </div>

            <form onSubmit={handleLogin} className="p-6">
              {authError && (
                <div className="mb-4 p-3 bg-rose-50 border border-rose-200 rounded-xl text-rose-700 text-sm">
                  {authError}
                </div>
              )}

              <div className="mb-6">
                <label className="block text-sm font-bold text-slate-700 mb-2 flex items-center gap-2">
                  <Mail className="w-4 h-4 text-emerald-600" />
                  Email Address *
                </label>
                <input
                  type="email"
                  value={loginForm.email}
                  onChange={(e) => setLoginForm({ ...loginForm, email: e.target.value })}
                  className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium transition-all"
                  placeholder="your@email.com"
                  required
                />
              </div>

              <div className="mb-6">
                <label className="block text-sm font-bold text-slate-700 mb-2 flex items-center gap-2">
                  <Shield className="w-4 h-4 text-emerald-600" />
                  Password *
                </label>
                <input
                  type="password"
                  value={loginForm.password}
                  onChange={(e) => setLoginForm({ ...loginForm, password: e.target.value })}
                  className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium transition-all"
                  placeholder="••••••••"
                  required
                />
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full bg-gradient-to-r from-emerald-500 to-teal-600 text-white py-4 rounded-xl hover:from-emerald-600 hover:to-teal-700 transition-all duration-300 font-black text-lg shadow-lg hover:shadow-xl transform hover:scale-105 flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none mb-4"
              >
                {loading ? (
                  <>
                    <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                    Signing in...
                  </>
                ) : (
                  <>
                    <LogIn className="w-5 h-5" />
                    Sign In
                  </>
                )}
              </button>

              <p className="text-center text-sm text-slate-600">
                Don't have an account?{' '}
                <button 
                  type="button"
                  onClick={() => { setShowLoginModal(false); setShowSignupModal(true); setAuthError(''); }}
                  className="text-emerald-600 hover:text-emerald-700 font-semibold"
                >
                  Create Account
                </button>
              </p>
            </form>
          </div>
        </div>
      )}

      {/* Signup Modal */}
      {showSignupModal && (
        <div className="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <div className="sticky top-0 bg-gradient-to-r from-emerald-50 to-teal-50 border-b-2 border-slate-200 p-6 rounded-t-3xl z-10">
              <div className="flex justify-between items-center">
                <div>
                  <h2 className="text-3xl font-black text-slate-900">Create Account</h2>
                  <p className="text-sm text-slate-600 mt-1">Join Wellness Pharmacy today</p>
                </div>
                <button 
                  onClick={() => { setShowSignupModal(false); setAuthError(''); }} 
                  className="w-10 h-10 bg-white rounded-full flex items-center justify-center hover:bg-slate-100 transition shadow-lg"
                >
                  <X className="w-6 h-6 text-slate-600" />
                </button>
              </div>
            </div>

            <form onSubmit={handleSignup} className="p-6">
              {authError && (
                <div className="mb-4 p-3 bg-rose-50 border border-rose-200 rounded-xl text-rose-700 text-sm">
                  {authError}
                </div>
              )}

              <div className="grid md:grid-cols-2 gap-6 mb-6">
                <div>
                  <label className="block text-sm font-bold text-slate-700 mb-2">First Name *</label>
                  <input
                    type="text"
                    value={signupForm.firstName}
                    onChange={(e) => setSignupForm({ ...signupForm, firstName: e.target.value })}
                    className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium transition-all"
                    placeholder="John"
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-bold text-slate-700 mb-2">Last Name *</label>
                  <input
                    type="text"
                    value={signupForm.lastName}
                    onChange={(e) => setSignupForm({ ...signupForm, lastName: e.target.value })}
                    className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium transition-all"
                    placeholder="Doe"
                    required
                  />
                </div>
              </div>

              <div className="mb-6">
                <label className="block text-sm font-bold text-slate-700 mb-2">Email Address *</label>
                <input
                  type="email"
                  value={signupForm.email}
                  onChange={(e) => setSignupForm({ ...signupForm, email: e.target.value })}
                  className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium transition-all"
                  placeholder="your@email.com"
                  required
                />
              </div>

              <div className="mb-6">
                <label className="block text-sm font-bold text-slate-700 mb-2">Phone Number *</label>
                <input
                  type="tel"
                  value={signupForm.phone}
                  onChange={(e) => setSignupForm({ ...signupForm, phone: e.target.value })}
                  className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium transition-all"
                  placeholder="07XXXXXXXX"
                  pattern="07[0-9]{8}"
                  required
                />
                <p className="text-xs text-slate-500 mt-1">Format: 07XXXXXXXX</p>
              </div>

              <div className="grid md:grid-cols-2 gap-6 mb-6">
                <div>
                  <label className="block text-sm font-bold text-slate-700 mb-2">Password *</label>
                  <input
                    type="password"
                    value={signupForm.password}
                    onChange={(e) => setSignupForm({ ...signupForm, password: e.target.value })}
                    className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium transition-all"
                    placeholder="••••••••"
                    minLength="8"
                    required
                  />
                  <p className="text-xs text-slate-500 mt-1">Min. 8 characters</p>
                </div>
                <div>
                  <label className="block text-sm font-bold text-slate-700 mb-2">Confirm Password *</label>
                  <input
                    type="password"
                    value={signupForm.confirmPassword}
                    onChange={(e) => setSignupForm({ ...signupForm, confirmPassword: e.target.value })}
                    className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium transition-all"
                    placeholder="••••••••"
                    required
                  />
                </div>
              </div>

              <div className="mb-6">
                <label className="block text-sm font-bold text-slate-700 mb-2">Address (Optional)</label>
                <textarea
                  value={signupForm.address}
                  onChange={(e) => setSignupForm({ ...signupForm, address: e.target.value })}
                  className="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 font-medium transition-all"
                  rows="2"
                  placeholder="Your address"
                />
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full bg-gradient-to-r from-emerald-500 to-teal-600 text-white py-4 rounded-xl hover:from-emerald-600 hover:to-teal-700 transition-all duration-300 font-black text-lg shadow-lg hover:shadow-xl transform hover:scale-105 flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none mb-4"
              >
                {loading ? (
                  <>
                    <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                    Creating Account...
                  </>
                ) : (
                  <>
                    <User className="w-5 h-5" />
                    Create Account
                  </>
                )}
              </button>

              <p className="text-center text-sm text-slate-600">
                Already have an account?{' '}
                <button 
                  type="button"
                  onClick={() => { setShowSignupModal(false); setShowLoginModal(true); setAuthError(''); }}
                  className="text-emerald-600 hover:text-emerald-700 font-semibold"
                >
                  Sign In
                </button>
              </p>
            </form>
          </div>
        </div>
      )}

      {/* Contact Section */}
      <section className="bg-gradient-to-br from-slate-100 to-white py-20 border-t-4 border-emerald-500">
        <div className="max-w-6xl mx-auto px-4">
          <div className="text-center mb-16">
            <h2 className="text-4xl font-black text-slate-900 mb-4">Get In Touch</h2>
            <p className="text-lg text-slate-600">We're here to help. Reach out anytime!</p>
          </div>
          
          <div className="grid md:grid-cols-3 gap-8 mb-12">
            <div className="bg-white rounded-2xl shadow-xl p-8 hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-2 border-2 border-transparent hover:border-emerald-200">
              <div className="w-16 h-16 bg-gradient-to-br from-emerald-400 to-teal-500 rounded-2xl flex items-center justify-center mb-6 shadow-lg">
                <Phone className="w-8 h-8 text-white" />
              </div>
              <h3 className="text-xl font-bold text-slate-900 mb-2">Call Us</h3>
              <p className="text-emerald-600 font-bold text-lg mb-2">+250 788 123 456</p>
              <p className="text-sm text-slate-600">Mon-Sat: 8AM - 8PM</p>
            </div>
            
            <div className="bg-white rounded-2xl shadow-xl p-8 hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-2 border-2 border-transparent hover:border-emerald-200">
              <div className="w-16 h-16 bg-gradient-to-br from-blue-400 to-cyan-500 rounded-2xl flex items-center justify-center mb-6 shadow-lg">
                <Mail className="w-8 h-8 text-white" />
              </div>
              <h3 className="text-xl font-bold text-slate-900 mb-2">Email Us</h3>
              <p className="text-blue-600 font-semibold mb-2">info@wellnesspharmacy.com</p>
              <p className="text-sm text-slate-600">24/7 support available</p>
            </div>
            
            <div className="bg-white rounded-2xl shadow-xl p-8 hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-2 border-2 border-transparent hover:border-emerald-200">
              <div className="w-16 h-16 bg-gradient-to-br from-purple-400 to-pink-500 rounded-2xl flex items-center justify-center mb-6 shadow-lg">
                <MapPin className="w-8 h-8 text-white" />
              </div>
              <h3 className="text-xl font-bold text-slate-900 mb-2">Visit Us</h3>
              <p className="text-purple-600 font-semibold mb-2">Kigali, Rwanda</p>
              <p className="text-sm text-slate-600">KG 123 St, Kimihurura</p>
            </div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 text-white">
        <div className="max-w-7xl mx-auto px-4 py-16">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-12 mb-12">
            <div className="col-span-1 md:col-span-2">
              <div className="flex items-center gap-3 mb-4">
                <div className="w-12 h-12 bg-gradient-to-br from-emerald-500 to-teal-600 rounded-xl flex items-center justify-center">
                  <Package className="w-7 h-7 text-white" />
                </div>
                <h3 className="text-2xl font-black">Wellness Pharmacy</h3>
              </div>
              <p className="text-slate-300 mb-6 leading-relaxed">
                Your trusted healthcare partner providing authentic medicines, expert consultation, 
                and reliable healthcare solutions across Rwanda since 2020.
              </p>
              <div className="flex gap-3">
                <a href="mailto:info@wellnesspharmacy.com" className="w-11 h-11 bg-white/10 hover:bg-emerald-500 rounded-xl flex items-center justify-center transition-all duration-300 backdrop-blur-sm border border-white/10">
                  <Mail className="w-5 h-5" />
                </a>
                <a href="https://wa.me/250788123456" target="_blank" rel="noopener noreferrer" className="w-11 h-11 bg-white/10 hover:bg-emerald-500 rounded-xl flex items-center justify-center transition-all duration-300 backdrop-blur-sm border border-white/10">
                  <Phone className="w-5 h-5" />
                </a>
                <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" className="w-11 h-11 bg-white/10 hover:bg-emerald-500 rounded-xl flex items-center justify-center transition-all duration-300 backdrop-blur-sm border border-white/10">
                  <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z"/>
                  </svg>
                </a>
                <a href="https://linkedin.com" target="_blank" rel="noopener noreferrer" className="w-11 h-11 bg-white/10 hover:bg-emerald-500 rounded-xl flex items-center justify-center transition-all duration-300 backdrop-blur-sm border border-white/10">
                  <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433c-1.144 0-2.063-.926-2.063-2.065 0-1.138.92-2.063 2.063-2.063 1.14 0 2.064.925 2.064 2.063 0 1.139-.925 2.065-2.064 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"/>
                  </svg>
                </a>
              </div>
            </div>
            
            <div>
              <h4 className="text-lg font-bold mb-4 flex items-center gap-2">
                <span className="w-1 h-6 bg-gradient-to-b from-emerald-400 to-teal-500 rounded-full"></span>
                Quick Links
              </h4>
              <ul className="space-y-3">
                <li><button onClick={() => window.scrollTo(0, 0)} className="text-slate-300 hover:text-emerald-400 transition-colors font-medium">Home</button></li>
                <li><button onClick={() => window.scrollTo(0, 0)} className="text-slate-300 hover:text-emerald-400 transition-colors font-medium">Shop Medicines</button></li>
                <li><button onClick={() => window.scrollTo(0, document.body.scrollHeight)} className="text-slate-300 hover:text-emerald-400 transition-colors font-medium">Contact Us</button></li>
                <li><button onClick={onStaffLogin} className="text-slate-300 hover:text-emerald-400 transition-colors font-medium">Staff Login</button></li>
              </ul>
            </div>
            
            <div>
              <h4 className="text-lg font-bold mb-4 flex items-center gap-2">
                <span className="w-1 h-6 bg-gradient-to-b from-emerald-400 to-teal-500 rounded-full"></span>
                Services
              </h4>
              <ul className="space-y-3">
                <li className="text-slate-300 font-medium flex items-center gap-2">
                  <CheckCircle className="w-4 h-4 text-emerald-400" />
                  Online Ordering
                </li>
                <li className="text-slate-300 font-medium flex items-center gap-2">
                  <CheckCircle className="w-4 h-4 text-emerald-400" />
                  Home Delivery
                </li>
                <li className="text-slate-300 font-medium flex items-center gap-2">
                  <CheckCircle className="w-4 h-4 text-emerald-400" />
                  Expert Consultation
                </li>
                <li className="text-slate-300 font-medium flex items-center gap-2">
                  <CheckCircle className="w-4 h-4 text-emerald-400" />
                  24/7 Support
                </li>
              </ul>
            </div>
          </div>
          
          <div className="border-t border-slate-700 pt-8">
            <div className="flex flex-col md:flex-row justify-between items-center gap-4">
              <p className="text-slate-400 text-sm">
                © {new Date().getFullYear()} Wellness Pharmacy. All rights reserved.
              </p>
              <div className="flex items-center gap-6 text-sm">
                <button onClick={() => alert('Privacy Policy')} className="text-slate-400 hover:text-emerald-400 transition-colors">Privacy Policy</button>
                <button onClick={() => alert('Terms of Service')} className="text-slate-400 hover:text-emerald-400 transition-colors">Terms of Service</button>
                <span className="text-slate-600">•</span>
                <span className="text-slate-400">Licensed Pharmacy 🇷🇼</span>
              </div>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default CustomerApp;
