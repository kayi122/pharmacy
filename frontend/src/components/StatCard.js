import React from 'react';
import { TrendingUp, TrendingDown } from 'lucide-react';

const StatCard = ({ 
  title, 
  value, 
  icon: Icon, 
  trend, 
  trendValue, 
  color = 'blue',
  description,
  loading = false 
}) => {
  const colors = {
    blue: 'from-blue-500 to-cyan-600',
    green: 'from-emerald-500 to-green-600',
    purple: 'from-purple-500 to-indigo-600',
    orange: 'from-orange-500 to-amber-600',
    pink: 'from-pink-500 to-rose-600',
    teal: 'from-teal-500 to-cyan-600',
  };
  
  const bgColors = {
    blue: 'bg-blue-50',
    green: 'bg-emerald-50',
    purple: 'bg-purple-50',
    orange: 'bg-orange-50',
    pink: 'bg-pink-50',
    teal: 'bg-teal-50',
  };
  
  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6 hover:shadow-xl hover:border-cyan-200 hover:-translate-y-1 transition-all duration-300">
      <div className="flex items-start justify-between mb-4">
        <div className="flex-1">
          <p className="text-sm font-medium text-slate-600 mb-1">{title}</p>
          {loading ? (
            <div className="h-8 w-24 bg-slate-200 animate-pulse rounded"></div>
          ) : (
            <h3 className="text-3xl font-bold text-slate-900">{value}</h3>
          )}
        </div>
        <div className={`p-3 rounded-xl bg-gradient-to-br ${colors[color]} shadow-lg`}>
          <Icon className="w-6 h-6 text-white" />
        </div>
      </div>
      
      <div className="flex items-center justify-between">
        {trend !== undefined && (
          <div className={`flex items-center gap-1 text-sm font-semibold ${
            trend > 0 ? 'text-emerald-600' : 'text-rose-600'
          }`}>
            {trend > 0 ? (
              <TrendingUp className="w-4 h-4" />
            ) : (
              <TrendingDown className="w-4 h-4" />
            )}
            <span>{trendValue || `${Math.abs(trend)}%`}</span>
          </div>
        )}
        {description && (
          <p className="text-xs text-slate-500">{description}</p>
        )}
      </div>
    </div>
  );
};

export default StatCard;
