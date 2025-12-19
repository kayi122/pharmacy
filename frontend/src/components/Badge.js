import React from 'react';

const Badge = ({ children, variant = 'default', size = 'md', className = '' }) => {
  const baseStyles = 'inline-flex items-center font-semibold rounded-full';
  
  const variants = {
    default: 'bg-slate-100 text-slate-800',
    primary: 'bg-gradient-to-r from-cyan-500 to-blue-600 text-white shadow-lg shadow-cyan-500/30',
    success: 'bg-gradient-to-r from-emerald-500 to-green-600 text-white shadow-lg shadow-emerald-500/30',
    warning: 'bg-gradient-to-r from-amber-500 to-orange-600 text-white shadow-lg shadow-amber-500/30',
    danger: 'bg-gradient-to-r from-rose-500 to-red-600 text-white shadow-lg shadow-rose-500/30',
    info: 'bg-gradient-to-r from-blue-500 to-indigo-600 text-white shadow-lg shadow-blue-500/30',
    purple: 'bg-purple-100 text-purple-800',
    pink: 'bg-pink-100 text-pink-800',
    teal: 'bg-teal-100 text-teal-800',
    indigo: 'bg-indigo-100 text-indigo-800',
  };
  
  const sizes = {
    sm: 'px-2 py-0.5 text-xs',
    md: 'px-2.5 py-1 text-xs',
    lg: 'px-3 py-1.5 text-sm',
  };
  
  return (
    <span className={`${baseStyles} ${variants[variant]} ${sizes[size]} ${className}`}>
      {children}
    </span>
  );
};

export default Badge;
