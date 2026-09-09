import React from 'react';
import { ArrowUpRight, ArrowDownRight } from 'lucide-react';

interface StatCardProps {
  title: string;
  value: string;
  unit?: string;
  subtitle: string;
  trend: string;
  isPositiveTrendGood?: boolean;
  isTrendingUp?: boolean;
  icon: React.ReactNode;
  accentColor?: string;
}

export const StatCard: React.FC<StatCardProps> = ({
  title,
  value,
  unit,
  subtitle,
  trend,
  isPositiveTrendGood = true,
  isTrendingUp = true,
  icon,
  accentColor = '#57E1DC'
}) => {
  const isGood = isTrendingUp ? isPositiveTrendGood : !isPositiveTrendGood;

  return (
    <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl flex flex-col justify-between card-hover">
      <div className="flex items-center justify-between mb-3">
        <span className="text-xs font-semibold text-[#8DA5BA] tracking-wide uppercase">
          {title}
        </span>
        <div 
          className="flex items-center justify-center w-9 h-9 rounded-xl"
          style={{ 
            backgroundColor: `${accentColor}18`,
            border: `1px solid ${accentColor}40`,
            color: accentColor 
          }}
        >
          {icon}
        </div>
      </div>

      <div>
        <div className="flex items-baseline gap-1.5 mb-1.5">
          <h3 className="text-2xl lg:text-3xl font-bold tracking-tight text-[#EDF7FF]">
            {value}
          </h3>
          {unit && (
            <span className="text-xs font-medium text-[#8DA5BA]">
              {unit}
            </span>
          )}
        </div>

        <div className="flex items-center justify-between pt-1">
          <span className="text-[11px] text-[#8DA5BA] truncate max-w-[140px]">
            {subtitle}
          </span>
          <div
            className={`flex items-center gap-0.5 px-2 py-0.5 rounded-md text-[11px] font-bold ${
              isGood
                ? 'bg-[#B8ED68]/15 text-[#B8ED68] border border-[#B8ED68]/30'
                : 'bg-[#FFB866]/15 text-[#FFB866] border border-[#FFB866]/30'
            }`}
          >
            {isTrendingUp ? (
              <ArrowUpRight className="w-3 h-3" />
            ) : (
              <ArrowDownRight className="w-3 h-3" />
            )}
            <span>{trend}</span>
          </div>
        </div>
      </div>
    </div>
  );
};
