import React from 'react';
import Pagination from './Pagination';

const Table = ({ 
  columns, 
  data, 
  loading = false,
  emptyMessage = 'No data available',
  pagination,
  onPageChange,
  className = ''
}) => {
  return (
    <div className={`bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden ${className}`}>
      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-gradient-to-r from-slate-50 to-slate-100 border-b-2 border-slate-200">
            <tr>
              {columns.map((column, index) => (
                <th
                  key={index}
                  className="px-6 py-4 text-left text-xs font-bold text-slate-700 uppercase tracking-wider"
                  style={{ width: column.width }}
                >
                  {column.header}
                </th>
              ))}
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100">
            {loading ? (
              <tr>
                <td colSpan={columns.length} className="px-6 py-12 text-center">
                  <div className="flex justify-center items-center space-x-2">
                    <div className="w-2 h-2 bg-cyan-500 rounded-full animate-bounce" style={{ animationDelay: '0ms' }}></div>
                    <div className="w-2 h-2 bg-cyan-500 rounded-full animate-bounce" style={{ animationDelay: '150ms' }}></div>
                    <div className="w-2 h-2 bg-cyan-500 rounded-full animate-bounce" style={{ animationDelay: '300ms' }}></div>
                  </div>
                  <p className="mt-3 text-sm text-slate-500 font-medium">Loading data...</p>
                </td>
              </tr>
            ) : data.length === 0 ? (
              <tr>
                <td colSpan={columns.length} className="px-6 py-12 text-center">
                  <p className="text-slate-500 text-sm">{emptyMessage}</p>
                </td>
              </tr>
            ) : (
              data.map((row, rowIndex) => (
                <tr 
                  key={rowIndex} 
                  className="hover:bg-gradient-to-r hover:from-cyan-50 hover:to-blue-50 transition-all duration-200"
                >
                  {columns.map((column, colIndex) => (
                    <td key={colIndex} className="px-6 py-4 whitespace-nowrap text-sm">
                      {column.render ? column.render(row) : row[column.accessor]}
                    </td>
                  ))}
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
      
      {pagination && data.length > 0 && (
        <div className="border-t border-slate-100 px-4 py-3 bg-slate-50">
          <Pagination 
            currentPage={pagination.currentPage} 
            totalPages={pagination.totalPages} 
            onPageChange={onPageChange} 
          />
        </div>
      )}
    </div>
  );
};

export default Table;
