import React from 'react';
import { Button } from './Button';

interface EmptyStateProps {
  icon: React.ReactNode;
  title: string;
  description: string;
  actionLabel?: string;
  onAction?: () => void;
}

export const EmptyState: React.FC<EmptyStateProps> = ({
  icon,
  title,
  description,
  actionLabel,
  onAction
}) => {
  return (
    <div className="flex flex-col items-center justify-center p-8 md:p-12 text-center bg-[#0E1D30] border border-[#1D3850] rounded-2xl">
      <div className="p-4 bg-[#132942] border border-[#1D3850] rounded-2xl text-[#8DA5BA] mb-4">
        {icon}
      </div>
      <h3 className="text-base font-bold text-[#EDF7FF] mb-1">{title}</h3>
      <p className="text-xs md:text-sm text-[#8DA5BA] max-w-sm mb-5 leading-relaxed">
        {description}
      </p>
      {actionLabel && onAction && (
        <Button variant="primary" size="sm" onClick={onAction}>
          {actionLabel}
        </Button>
      )}
    </div>
  );
};
