import React from 'react';
import { CheckCircle, AlertCircle, Info, AlertTriangle, X } from 'lucide-react';

const Alert = ({ 
  type = 'info', 
  title, 
  message, 
  onClose,
  className = '' 
}) => {
  const types = {
    success: {
      bg: 'bg-gradient-to-r from-emerald-50 to-green-50',
      border: 'border-emerald-200',
      icon: CheckCircle,
      iconColor: 'text-emerald-600',
      titleColor: 'text-emerald-900',
      messageColor: 'text-emerald-700'
    },
    error: {
      bg: 'bg-gradient-to-r from-rose-50 to-red-50',
      border: 'border-rose-200',
      icon: AlertCircle,
      iconColor: 'text-rose-600',
      titleColor: 'text-rose-900',
      messageColor: 'text-rose-700'
    },
    warning: {
      bg: 'bg-gradient-to-r from-amber-50 to-orange-50',
      border: 'border-amber-200',
      icon: AlertTriangle,
      iconColor: 'text-amber-600',
      titleColor: 'text-amber-900',
      messageColor: 'text-amber-700'
    },
    info: {
      bg: 'bg-gradient-to-r from-blue-50 to-cyan-50',
      border: 'border-blue-200',
      icon: Info,
      iconColor: 'text-blue-600',
      titleColor: 'text-blue-900',
      messageColor: 'text-blue-700'
    }
  };
  
  const config = types[type];
  const Icon = config.icon;
  
  return (
    <div className={`${config.bg} border-2 ${config.border} rounded-xl p-4 shadow-sm ${className}`}>
      <div className="flex items-start gap-3">
        <Icon className={`w-5 h-5 ${config.iconColor} flex-shrink-0 mt-0.5`} />
        <div className="flex-1">
          {title && (
            <h4 className={`font-semibold ${config.titleColor} mb-1`}>{title}</h4>
          )}
          {message && (
            <p className={`text-sm ${config.messageColor}`}>{message}</p>
          )}
        </div>
        {onClose && (
          <button
            onClick={onClose}
            className={`${config.iconColor} hover:opacity-70 transition-opacity`}
          >
            <X className="w-5 h-5" />
          </button>
        )}
      </div>
    </div>
  );
};

export default Alert;
