import React from 'react';

const Card = ({ children, className = '', hover = false, padding = 'p-6' }) => {
  return (
    <div 
      className={`bg-white rounded-2xl shadow-sm border border-slate-100 ${padding} ${
        hover ? 'transition-all duration-300 hover:shadow-xl hover:border-cyan-200 hover:-translate-y-1' : ''
      } ${className}`}
    >
      {children}
    </div>
  );
};

export default Card;
