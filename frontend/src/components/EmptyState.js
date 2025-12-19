import React from 'react';

const EmptyState = ({ 
  icon: Icon, 
  title = 'No data available', 
  description,
  action,
  actionLabel,
  className = '' 
}) => {
  return (
    <div className={`flex flex-col items-center justify-center py-16 px-4 ${className}`}>
      {Icon && (
        <div className="p-4 bg-gradient-to-br from-slate-100 to-slate-200 rounded-2xl mb-4 shadow-inner">
          <Icon className="w-12 h-12 text-slate-400" />
        </div>
      )}
      <h3 className="text-xl font-bold text-slate-900 mb-2">{title}</h3>
      {description && (
        <p className="text-sm text-slate-500 text-center max-w-md mb-6">{description}</p>
      )}
      {action && (
        <button
          onClick={action}
          className="px-6 py-3 bg-gradient-to-r from-cyan-500 to-blue-600 text-white rounded-xl hover:from-cyan-600 hover:to-blue-700 shadow-lg shadow-cyan-500/30 transition-all font-semibold"
        >
          {actionLabel || 'Get Started'}
        </button>
      )}
    </div>
  );
};

export default EmptyState;
