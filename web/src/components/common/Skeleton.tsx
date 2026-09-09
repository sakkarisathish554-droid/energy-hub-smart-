import React from 'react';

export const Skeleton: React.FC<{ className?: string }> = ({ className = '' }) => {
  return (
    <div className={`animate-pulse bg-[#132942] rounded-lg ${className}`} />
  );
};

export const StatCardSkeleton: React.FC = () => {
  return (
    <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl flex flex-col gap-3">
      <div className="flex justify-between items-center">
        <Skeleton className="w-24 h-4" />
        <Skeleton className="w-8 h-8 rounded-lg" />
      </div>
      <Skeleton className="w-32 h-7" />
      <Skeleton className="w-20 h-3" />
    </div>
  );
};
