import React, { useState } from 'react';
import { Mail, Lock, Eye, EyeOff, AlertCircle, Loader, ArrowRight, Shield } from 'lucide-react';

const API_BASE_URL = 'http://localhost:8080/api';

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
      <div className="absolute inset-0 bg-gradient-to-br from-cyan-600/40 via-blue-600/30 to-indigo-600/40 backdrop-blur-sm animate-pulse"></div>
      
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute w-96 h-96 bg-cyan-400/20 rounded-full blur-3xl animate-blob animation-delay-2000 top-0 left-0"></div>
        <div className="absolute w-96 h-96 bg-blue-400/20 rounded-full blur-3xl animate-blob top-0 right-0"></div>
        <div className="absolute w-96 h-96 bg-indigo-400/20 rounded-full blur-3xl animate-blob animation-delay-4000 bottom-0 left-1/2"></div>
      </div>

      <div className="w-full max-w-md relative z-10 animate-fadeInUp">
        <div className="bg-white/20 backdrop-blur-2xl rounded-3xl shadow-2xl border border-white/30 p-8 space-y-6 hover:shadow-cyan-500/20 transition-all duration-500">
          <div className="text-center space-y-3 animate-slideDown">
            <div className="inline-flex items-center justify-center w-20 h-20 bg-gradient-to-br from-cyan-400 via-blue-500 to-indigo-600 rounded-full mb-4 shadow-2xl shadow-cyan-500/50 animate-bounce-slow">
              <Shield className="w-10 h-10 text-white drop-shadow-lg" />
            </div>
            <h1 className="text-4xl font-bold text-white drop-shadow-2xl animate-glow">
              Staff Login
            </h1>
            <p className="text-white/90 font-medium drop-shadow-lg">Welcome back! Please login to your account</p>
          </div>

          {error && (
            <div className="bg-red-500/20 backdrop-blur-xl border border-red-300/50 rounded-2xl p-4 flex items-start gap-3 animate-shake">
              <AlertCircle className="w-5 h-5 text-red-100 flex-shrink-0 mt-0.5" />
              <p className="text-sm text-red-50 font-medium">{error}</p>
            </div>
          )}

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

          <div className="text-center text-sm animate-fadeIn">
            <p className="text-white/80 font-medium">
              Don't have an account?{' '}
              <button onClick={onSwitchToSignup} className="text-cyan-200 hover:text-white font-bold transition-colors underline decoration-2 underline-offset-4">
                Sign up
              </button>
            </p>
          </div>

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

export default LoginPage;
