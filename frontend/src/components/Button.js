import React from 'react';
import { Loader } from 'lucide-react';

const Button = ({ 
  children, 
  variant = 'primary', 
  size = 'md', 
  loading = false, 
  disabled = false,
  icon: Icon,
  onClick,
  className = '',
  type = 'button'
}) => {
  const baseStyles = 'inline-flex items-center justify-center gap-2 font-semibold rounded-xl transition-all duration-200 focus:ring-4 focus:outline-none';
  
  const variants = {
    primary: 'bg-gradient-to-r from-cyan-500 to-blue-600 text-white hover:from-cyan-600 hover:to-blue-700 shadow-lg shadow-cyan-500/30 focus:ring-cyan-300',
    secondary: 'bg-slate-100 text-slate-700 hover:bg-slate-200 focus:ring-slate-300',
    success: 'bg-gradient-to-r from-emerald-500 to-green-600 text-white hover:from-emerald-600 hover:to-green-700 shadow-lg shadow-emerald-500/30 focus:ring-emerald-300',
    danger: 'bg-gradient-to-r from-rose-500 to-red-600 text-white hover:from-rose-600 hover:to-red-700 shadow-lg shadow-rose-500/30 focus:ring-rose-300',
    warning: 'bg-gradient-to-r from-amber-500 to-orange-600 text-white hover:from-amber-600 hover:to-orange-700 shadow-lg shadow-amber-500/30 focus:ring-amber-300',
    outline: 'border-2 border-slate-200 bg-white text-slate-700 hover:bg-slate-50 focus:ring-slate-300',
    ghost: 'bg-transparent text-slate-700 hover:bg-slate-100 focus:ring-slate-300',
  };
  
  const sizes = {
    sm: 'px-3 py-1.5 text-sm',
    md: 'px-4 py-2.5 text-sm',
    lg: 'px-6 py-3 text-base',
    xl: 'px-8 py-4 text-lg',
  };
  
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled || loading}
      className={`${baseStyles} ${variants[variant]} ${sizes[size]} ${
        (disabled || loading) ? 'opacity-50 cursor-not-allowed' : ''
      } ${className}`}
    >
      {loading ? (
        <Loader className="w-4 h-4 animate-spin" />
      ) : Icon ? (
        <Icon className="w-4 h-4" />
      ) : null}
      {children}
    </button>
  );
};

export default Button;
