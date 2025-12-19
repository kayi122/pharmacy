import React from 'react';

const SimplePieChart = ({ data, title, size = 200 }) => {
  if (!data || data.length === 0) return null;
  
  const total = data.reduce((sum, item) => sum + item.value, 0);
  let currentAngle = -90;
  
  const colors = [
    { start: '#06b6d4', end: '#3b82f6' }, // cyan to blue
    { start: '#10b981', end: '#22c55e' }, // emerald to green
    { start: '#a855f7', end: '#6366f1' }, // purple to indigo
    { start: '#f97316', end: '#f59e0b' }, // orange to amber
    { start: '#ec4899', end: '#f43f5e' }, // pink to rose
    { start: '#14b8a6', end: '#06b6d4' }, // teal to cyan
  ];
  
  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6">
      {title && <h3 className="text-lg font-bold text-slate-900 mb-6">{title}</h3>}
      <div className="flex items-center justify-center gap-8">
        <div className="relative" style={{ width: size, height: size }}>
          <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
            {data.map((item, index) => {
              const percentage = (item.value / total) * 100;
              const angle = (percentage / 100) * 360;
              const radius = size / 2 - 10;
              const centerX = size / 2;
              const centerY = size / 2;
              
              const startAngle = currentAngle;
              const endAngle = currentAngle + angle;
              currentAngle = endAngle;
              
              const startRad = (startAngle * Math.PI) / 180;
              const endRad = (endAngle * Math.PI) / 180;
              
              const x1 = centerX + radius * Math.cos(startRad);
              const y1 = centerY + radius * Math.sin(startRad);
              const x2 = centerX + radius * Math.cos(endRad);
              const y2 = centerY + radius * Math.sin(endRad);
              
              const largeArc = angle > 180 ? 1 : 0;
              
              const pathData = [
                `M ${centerX} ${centerY}`,
                `L ${x1} ${y1}`,
                `A ${radius} ${radius} 0 ${largeArc} 1 ${x2} ${y2}`,
                'Z'
              ].join(' ');
              
              const color = colors[index % colors.length];
              
              return (
                <g key={index}>
                  <defs>
                    <linearGradient id={`gradient-${index}`} x1="0%" y1="0%" x2="100%" y2="100%">
                      <stop offset="0%" stopColor={color.start} />
                      <stop offset="100%" stopColor={color.end} />
                    </linearGradient>
                  </defs>
                  <path
                    d={pathData}
                    fill={`url(#gradient-${index})`}
                    className="hover:opacity-80 transition-opacity cursor-pointer"
                  />
                </g>
              );
            })}
          </svg>
          <div className="absolute inset-0 flex items-center justify-center">
            <div className="bg-white rounded-full w-24 h-24 flex items-center justify-center shadow-lg">
              <div className="text-center">
                <div className="text-2xl font-bold text-slate-900">{total}</div>
                <div className="text-xs text-slate-500">Total</div>
              </div>
            </div>
          </div>
        </div>
        
        <div className="space-y-3">
          {data.map((item, index) => {
            const percentage = ((item.value / total) * 100).toFixed(1);
            const colorClasses = [
              'from-cyan-500 to-blue-600',
              'from-emerald-500 to-green-600',
              'from-purple-500 to-indigo-600',
              'from-orange-500 to-amber-600',
              'from-pink-500 to-rose-600',
              'from-teal-500 to-cyan-600',
            ];
            return (
              <div key={index} className="flex items-center gap-3">
                <div className={`w-4 h-4 rounded bg-gradient-to-br ${colorClasses[index % colorClasses.length]} shadow-lg`}></div>
                <div className="flex-1">
                  <div className="text-sm font-semibold text-slate-900">{item.label}</div>
                  <div className="text-xs text-slate-500">{item.value} ({percentage}%)</div>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default SimplePieChart;
