import React from 'react';

const Input = ({ 
  label, 
  type = 'text', 
  value, 
  onChange, 
  placeholder, 
  required = false,
  error,
  icon: Icon,
  className = '',
  disabled = false,
  ...props 
}) => {
  return (
    <div className={className}>
      {label && (
        <label className="block text-sm font-semibold text-slate-700 mb-2">
          {label} {required && <span className="text-rose-500">*</span>}
        </label>
      )}
      <div className="relative">
        {Icon && (
          <Icon className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400" />
        )}
        <input
          type={type}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          required={required}
          disabled={disabled}
          className={`w-full ${Icon ? 'pl-10' : 'pl-4'} pr-4 py-3 bg-white border-2 ${
            error ? 'border-rose-300 focus:border-rose-500 focus:ring-rose-200' : 'border-slate-200 focus:border-cyan-500 focus:ring-cyan-200'
          } rounded-xl text-slate-900 placeholder-slate-400 focus:ring-4 focus:outline-none transition-all duration-200 ${
            disabled ? 'bg-slate-50 cursor-not-allowed' : ''
          }`}
          {...props}
        />
      </div>
      {error && (
        <p className="mt-1.5 text-sm text-rose-600 font-medium">{error}</p>
      )}
    </div>
  );
};

export default Input;
