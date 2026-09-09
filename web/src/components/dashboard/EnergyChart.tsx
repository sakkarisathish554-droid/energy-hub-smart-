import React, { useState } from 'react';
import { EnergyDataPoint } from '../../types';

interface EnergyChartProps {
  data: EnergyDataPoint[];
  timeRange: string;
  onTimeRangeChange: (range: string) => void;
}

export const EnergyChart: React.FC<EnergyChartProps> = ({
  data,
  timeRange,
  onTimeRangeChange
}) => {
  const [hoveredIdx, setHoveredIdx] = useState<number | null>(null);

  const ranges = ['Today', '7 Days', '30 Days', '12 Months'];

  // SVG calculations
  const width = 650;
  const height = 220;
  const paddingX = 35;
  const paddingY = 25;
  const chartW = width - paddingX * 2;
  const chartH = height - paddingY * 2;

  const maxVal = Math.max(...data.map(d => Math.max(d.currentKwh, d.previousKwh)), 10) * 1.15;
  const minVal = 0;

  const getX = (idx: number) => paddingX + (idx / Math.max(data.length - 1, 1)) * chartW;
  const getY = (val: number) => height - paddingY - ((val - minVal) / (maxVal - minVal)) * chartH;

  // Generate smooth line path
  const currentPoints = data.map((d, i) => `${getX(i)},${getY(d.currentKwh)}`);
  const prevPoints = data.map((d, i) => `${getX(i)},${getY(d.previousKwh)}`);

  const currentPath = currentPoints.reduce((acc, pt, idx, arr) => {
    if (idx === 0) return `M ${pt}`;
    const [x, y] = pt.split(',').map(Number);
    const [prevX, prevY] = arr[idx - 1].split(',').map(Number);
    const cx1 = prevX + (x - prevX) / 2;
    const cy1 = prevY;
    const cx2 = prevX + (x - prevX) / 2;
    const cy2 = y;
    return `${acc} C ${cx1} ${cy1}, ${cx2} ${cy2}, ${x} ${y}`;
  }, '');

  const areaPath = `${currentPath} L ${getX(data.length - 1)},${height - paddingY} L ${getX(0)},${height - paddingY} Z`;

  const prevPath = prevPoints.reduce((acc, pt, idx) => {
    return idx === 0 ? `M ${pt}` : `${acc} L ${pt}`;
  }, '');

  const activePoint = hoveredIdx !== null ? data[hoveredIdx] : null;

  return (
    <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 mb-4">
        <div>
          <h3 className="text-base font-bold text-[#EDF7FF]">Energy Consumption</h3>
          <p className="text-xs text-[#8DA5BA]">Real-time load compared with historical baseline</p>
        </div>

        {/* Range Selector */}
        <div className="flex items-center p-1 bg-[#132942] border border-[#1D3850] rounded-xl self-start sm:self-auto">
          {ranges.map((r) => (
            <button
              key={r}
              onClick={() => onTimeRangeChange(r)}
              className={`px-3 py-1 text-xs font-semibold rounded-lg transition-all ${
                timeRange === r
                  ? 'bg-[#57E1DC] text-[#07111F]'
                  : 'text-[#8DA5BA] hover:text-[#EDF7FF]'
              }`}
            >
              {r}
            </button>
          ))}
        </div>
      </div>

      {/* Legend and Active hover value */}
      <div className="flex items-center justify-between min-h-[28px] mb-2 px-1 text-xs">
        {activePoint ? (
          <div className="flex items-center gap-3 bg-[#132942] px-3 py-1 rounded-lg border border-[#57E1DC]/30">
            <span className="font-semibold text-[#EDF7FF]">{activePoint.label}:</span>
            <span className="text-[#57E1DC] font-bold">{activePoint.currentKwh} kWh</span>
            <span className="text-[#8DA5BA]">prev: {activePoint.previousKwh} kWh</span>
            {activePoint.isPeak && (
              <span className="px-1.5 py-0.5 rounded text-[10px] font-bold bg-[#FFB866]/20 text-[#FFB866]">
                PEAK
              </span>
            )}
          </div>
        ) : (
          <span className="text-xs text-[#8DA5BA]">Hover points to inspect load details</span>
        )}

        <div className="flex items-center gap-4 text-xs">
          <div className="flex items-center gap-1.5">
            <span className="w-2.5 h-2.5 rounded-full bg-[#57E1DC]" />
            <span className="text-[#8DA5BA]">Current Period</span>
          </div>
          <div className="flex items-center gap-1.5">
            <span className="w-2.5 h-0.5 border-t border-dashed border-[#8DA5BA]" />
            <span className="text-[#8DA5BA]">Previous Period</span>
          </div>
        </div>
      </div>

      {/* SVG Container */}
      <div className="relative w-full overflow-hidden">
        <svg
          viewBox={`0 0 ${width} ${height}`}
          className="w-full h-48 md:h-56"
        >
          <defs>
            <linearGradient id="cyanGrad" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stopColor="#57E1DC" stopOpacity="0.35" />
              <stop offset="100%" stopColor="#57E1DC" stopOpacity="0.0" />
            </linearGradient>
          </defs>

          {/* Grid lines */}
          {[0, 0.25, 0.5, 0.75, 1].map((ratio) => {
            const y = paddingY + chartH * ratio;
            return (
              <line
                key={ratio}
                x1={paddingX}
                y1={y}
                x2={width - paddingX}
                y2={y}
                stroke="#132942"
                strokeWidth="1"
              />
            );
          })}

          {/* Previous period line */}
          <path
            d={prevPath}
            fill="none"
            stroke="#8DA5BA"
            strokeWidth="1.5"
            strokeDasharray="4 4"
            opacity="0.5"
          />

          {/* Area under current line */}
          <path d={areaPath} fill="url(#cyanGrad)" />

          {/* Current line */}
          <path
            d={currentPath}
            fill="none"
            stroke="#57E1DC"
            strokeWidth="2.5"
          />

          {/* Data Points */}
          {data.map((pt, idx) => {
            const cx = getX(idx);
            const cy = getY(pt.currentKwh);
            const isHovered = hoveredIdx === idx;

            return (
              <g
                key={idx}
                className="cursor-pointer"
                onMouseEnter={() => setHoveredIdx(idx)}
                onMouseLeave={() => setHoveredIdx(null)}
              >
                {pt.isPeak && (
                  <circle
                    cx={cx}
                    cy={cy}
                    r="8"
                    fill="#FFB866"
                    opacity="0.25"
                  />
                )}
                <circle
                  cx={cx}
                  cy={cy}
                  r={isHovered ? 6 : pt.isPeak ? 5 : 3.5}
                  fill={pt.isPeak ? '#FFB866' : '#57E1DC'}
                  stroke="#0E1D30"
                  strokeWidth="2"
                />
              </g>
            );
          })}

          {/* X-axis labels */}
          {data.map((pt, idx) => (
            <text
              key={idx}
              x={getX(idx)}
              y={height - 6}
              textAnchor="middle"
              className={`text-[10px] font-medium transition-colors ${
                hoveredIdx === idx ? 'fill-[#57E1DC] font-bold' : 'fill-[#8DA5BA]'
              }`}
            >
              {pt.label}
            </text>
          ))}
        </svg>
      </div>
    </div>
  );
};
