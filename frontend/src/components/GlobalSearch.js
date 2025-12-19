import React, { useState } from 'react';
import { Search, X } from 'lucide-react';

const API_BASE_URL = 'http://localhost:8080/api';

const GlobalSearch = ({ onClose }) => {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSearch = async () => {
    if (!query.trim()) return;
    
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/search?query=${encodeURIComponent(query)}`);
      const data = await response.json();
      setResults(data);
    } catch (error) {
      console.error('Search error:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-start justify-center pt-20">
      <div className="bg-white rounded-lg shadow-2xl w-full max-w-3xl max-h-[80vh] overflow-hidden">
        <div className="p-4 border-b flex items-center gap-3">
          <Search className="w-6 h-6 text-gray-400" />
          <input
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            placeholder="Search medicines, companies, sales, users..."
            className="flex-1 outline-none text-lg"
            autoFocus
          />
          <button onClick={onClose} className="p-2 hover:bg-gray-100 rounded">
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="p-4 overflow-y-auto max-h-[calc(80vh-80px)]">
          {loading && <p className="text-center text-gray-500">Searching...</p>}
          
          {results && (
            <div className="space-y-6">
              {results.medicines?.length > 0 && (
                <div>
                  <h3 className="font-bold text-lg mb-2">Medicines ({results.medicines.length})</h3>
                  <div className="space-y-2">
                    {results.medicines.map(m => (
                      <div key={m.id} className="p-3 bg-gray-50 rounded hover:bg-gray-100">
                        <p className="font-semibold">{m.name}</p>
                        <p className="text-sm text-gray-600">{m.category} - RWF {m.sellingPrice}</p>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {results.companies?.length > 0 && (
                <div>
                  <h3 className="font-bold text-lg mb-2">Companies ({results.companies.length})</h3>
                  <div className="space-y-2">
                    {results.companies.map(c => (
                      <div key={c.id} className="p-3 bg-gray-50 rounded hover:bg-gray-100">
                        <p className="font-semibold">{c.name}</p>
                        <p className="text-sm text-gray-600">{c.country}</p>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {results.sales?.length > 0 && (
                <div>
                  <h3 className="font-bold text-lg mb-2">Sales ({results.sales.length})</h3>
                  <div className="space-y-2">
                    {results.sales.map(s => (
                      <div key={s.id} className="p-3 bg-gray-50 rounded hover:bg-gray-100">
                        <p className="font-semibold">{s.customerName}</p>
                        <p className="text-sm text-gray-600">RWF {s.totalPrice}</p>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {results.users?.length > 0 && (
                <div>
                  <h3 className="font-bold text-lg mb-2">Users ({results.users.length})</h3>
                  <div className="space-y-2">
                    {results.users.map(u => (
                      <div key={u.id} className="p-3 bg-gray-50 rounded hover:bg-gray-100">
                        <p className="font-semibold">{u.firstName} {u.lastName}</p>
                        <p className="text-sm text-gray-600">{u.role} - {u.email}</p>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {results && !results.medicines?.length && !results.companies?.length && 
               !results.sales?.length && !results.users?.length && (
                <p className="text-center text-gray-500">No results found</p>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default GlobalSearch;
