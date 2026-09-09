import React from 'react';
import { Search as SearchIcon, X } from 'lucide-react';

interface SearchProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
  className?: string;
}

export const Search: React.FC<SearchProps> = ({
  value,
  onChange,
  placeholder = 'Search devices, recommendations...',
  className = ''
}) => {
  return (
    <div className={`relative flex items-center ${className}`}>
      <SearchIcon className="absolute left-3 w-4 h-4 text-[#8DA5BA] pointer-events-none" />
      <input
        type="text"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
        className="w-full pl-9 pr-9 py-2 bg-[#0E1D30] border border-[#1D3850] rounded-lg text-xs md:text-sm text-[#EDF7FF] placeholder-[#8DA5BA] focus:outline-none focus:border-[#57E1DC] transition-colors"
      />
      {value && (
        <button
          onClick={() => onChange('')}
          className="absolute right-3 p-0.5 text-[#8DA5BA] hover:text-[#EDF7FF] rounded transition-colors"
        >
          <X className="w-3.5 h-3.5" />
        </button>
      )}
    </div>
  );
};
