import React from 'react';

const Select = ({ 
  label, 
  value, 
  onChange, 
  options = [],
  placeholder = 'Select an option',
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
          <Icon className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400 pointer-events-none z-10" />
        )}
        <select
          value={value}
          onChange={onChange}
          required={required}
          disabled={disabled}
          className={`w-full ${Icon ? 'pl-10' : 'pl-4'} pr-10 py-3 bg-white border-2 ${
            error ? 'border-rose-300 focus:border-rose-500 focus:ring-rose-200' : 'border-slate-200 focus:border-cyan-500 focus:ring-cyan-200'
          } rounded-xl text-slate-900 focus:ring-4 focus:outline-none transition-all duration-200 appearance-none cursor-pointer ${
            disabled ? 'bg-slate-50 cursor-not-allowed' : ''
          }`}
          {...props}
        >
          <option value="" disabled>{placeholder}</option>
          {options.map((option, index) => (
            <option key={index} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
        <div className="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none">
          <svg className="w-5 h-5 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
          </svg>
        </div>
      </div>
      {error && (
        <p className="mt-1.5 text-sm text-rose-600 font-medium">{error}</p>
      )}
    </div>
  );
};

export default Select;
