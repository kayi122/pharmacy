import React from 'react';

const SimpleBarChart = ({ data, title, height = 300 }) => {
  if (!data || data.length === 0) return null;
  
  const maxValue = Math.max(...data.map(d => d.value));
  
  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6">
      {title && <h3 className="text-lg font-bold text-slate-900 mb-6">{title}</h3>}
      <div className="flex items-end justify-between gap-4" style={{ height: `${height}px` }}>
        {data.map((item, index) => {
          const barHeight = (item.value / maxValue) * 100;
          return (
            <div key={index} className="flex-1 flex flex-col items-center gap-2">
              <div className="w-full flex flex-col items-center justify-end" style={{ height: '100%' }}>
                <div className="relative group w-full flex justify-center">
                  <div
                    className="w-full max-w-[60px] bg-gradient-to-t from-cyan-500 to-blue-600 rounded-t-lg transition-all duration-300 hover:from-cyan-600 hover:to-blue-700 shadow-lg hover:shadow-xl"
                    style={{ height: `${barHeight}%` }}
                  >
                    <div className="absolute -top-8 left-1/2 -translate-x-1/2 bg-slate-900 text-white px-2 py-1 rounded text-xs font-semibold opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap">
                      {item.value}
                    </div>
                  </div>
                </div>
              </div>
              <div className="text-xs font-medium text-slate-600 text-center w-full truncate">
                {item.label}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default SimpleBarChart;
