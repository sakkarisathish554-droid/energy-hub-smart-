import React from 'react';
import { EnergyBreakdownItem } from '../../types';

interface EnergyBreakdownProps {
  items: EnergyBreakdownItem[];
  totalKwh?: number;
}

export const EnergyBreakdown: React.FC<EnergyBreakdownProps> = ({
  items,
  totalKwh = 24.6
}) => {
  return (
    <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl">
      <div className="flex items-center justify-between mb-4">
        <div>
          <h3 className="text-base font-bold text-[#EDF7FF]">Energy Breakdown</h3>
          <p className="text-xs text-[#8DA5BA]">Consumption distributed by appliance category</p>
        </div>
        <span className="text-xs font-bold text-[#57E1DC] bg-[#57E1DC]/10 px-2.5 py-1 rounded-lg border border-[#57E1DC]/20">
          {totalKwh} kWh Total
        </span>
      </div>

      {/* Segmented multi-color bar */}
      <div className="flex h-3 rounded-full overflow-hidden mb-5 bg-[#132942]">
        {items.map((item) => (
          <div
            key={item.category}
            style={{
              width: `${item.percentage}%`,
              backgroundColor: item.color
            }}
            title={`${item.category}: ${item.percentage}% (${item.kwh} kWh)`}
            className="transition-all hover:opacity-80"
          />
        ))}
      </div>

      {/* List with detail bars */}
      <div className="space-y-3">
        {items.map((item) => (
          <div key={item.category} className="space-y-1.5">
            <div className="flex items-center justify-between text-xs">
              <div className="flex items-center gap-2">
                <span
                  className="w-2.5 h-2.5 rounded-full"
                  style={{ backgroundColor: item.color }}
                />
                <span className="font-medium text-[#EDF7FF]">{item.category}</span>
              </div>
              <div className="flex items-center gap-3">
                <span className="text-[#8DA5BA]">{item.kwh} kWh</span>
                <span className="font-bold text-[#EDF7FF] w-8 text-right">
                  {item.percentage}%
                </span>
              </div>
            </div>
            <div className="h-1.5 w-full bg-[#132942] rounded-full overflow-hidden">
              <div
                className="h-full rounded-full transition-all duration-500"
                style={{
                  width: `${item.percentage}%`,
                  backgroundColor: item.color
                }}
              />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
