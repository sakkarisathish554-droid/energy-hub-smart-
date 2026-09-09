import React from 'react';
import { SolarMetrics } from '../types';
import { StatCard } from '../components/dashboard/StatCard';
import { EnergyFlow } from '../components/dashboard/EnergyFlow';
import { Sun, Home, Power, Gauge, Zap } from 'lucide-react';

interface SolarProps {
  metrics: SolarMetrics;
}

export const Solar: React.FC<SolarProps> = ({ metrics }) => {
  // Solar hourly generation curve
  const curve = [
    { time: '06:00', kw: 0.1 },
    { time: '08:00', kw: 1.1 },
    { time: '10:00', kw: 2.8 },
    { time: '12:00', kw: 3.82 },
    { time: '14:00', kw: 3.4 },
    { time: '16:00', kw: 2.0 },
    { time: '18:00', kw: 0.5 },
    { time: '20:00', kw: 0.0 },
  ];

  const maxKw = 4.5;

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Simulation Banner */}
      <div className="flex items-center justify-between p-4 bg-[#132942]/60 border border-[#57E1DC]/30 rounded-2xl">
        <div className="flex items-center gap-3">
          <div className="p-2 bg-[#57E1DC]/15 rounded-xl text-[#57E1DC]">
            <Zap className="w-5 h-5" />
          </div>
          <div>
            <h3 className="text-sm font-bold text-[#EDF7FF]">Solar Simulation Environment</h3>
            <p className="text-xs text-[#8DA5BA]">
              Streaming synthetic telemetry for a 5.0 kW rooftop PV array coupled to a hybrid inverter.
            </p>
          </div>
        </div>
        <span className="hidden sm:inline-block px-3 py-1 rounded-full text-xs font-bold bg-[#57E1DC]/20 text-[#57E1DC]">
          Simulation Active
        </span>
      </div>

      {/* 4 Solar KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard
          title="Solar Generation"
          value={`${metrics.generationKwh}`}
          unit="kWh"
          subtitle="Today's total PV harvest"
          trend="+9.3%"
          isPositiveTrendGood={true}
          isTrendingUp={true}
          icon={<Sun className="w-5 h-5" />}
          accentColor="#57E1DC"
        />
        <StatCard
          title="Home Consumption"
          value={`${metrics.consumptionKwh}`}
          unit="kWh"
          subtitle="Total home power draw"
          trend="-8.4%"
          isPositiveTrendGood={false}
          isTrendingUp={false}
          icon={<Home className="w-5 h-5" />}
          accentColor="#FFB866"
        />
        <StatCard
          title="Grid Import"
          value={`${metrics.gridImportKwh}`}
          unit="kWh"
          subtitle="Night / shortfall draw"
          trend="-14.2%"
          isPositiveTrendGood={false}
          isTrendingUp={false}
          icon={<Power className="w-5 h-5" />}
          accentColor="#8DA5BA"
        />
        <StatCard
          title="Solar Coverage"
          value={`${metrics.solarCoveragePct}`}
          unit="%"
          subtitle="Self-sufficiency index"
          trend="+5.8%"
          isPositiveTrendGood={true}
          isTrendingUp={true}
          icon={<Gauge className="w-5 h-5" />}
          accentColor="#B8ED68"
        />
      </div>

      {/* Live Energy Flow Component */}
      <EnergyFlow metrics={metrics} />

      {/* Solar Generation Curve (Today) */}
      <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2 mb-4">
          <div>
            <h3 className="text-base font-bold text-[#EDF7FF]">Solar Generation Curve (Today)</h3>
            <p className="text-xs text-[#8DA5BA]">Peak generation reached 3.82 kW at 12:30 PM</p>
          </div>
          <div className="flex items-center gap-3 text-xs">
            <span className="text-[#8DA5BA]">Array Rated: <strong>5.0 kWp</strong></span>
            <span className="text-[#B8ED68]">Efficiency: <strong>{metrics.solarEfficiencyPct}%</strong></span>
          </div>
        </div>

        {/* SVG Curve */}
        <div className="w-full">
          <svg viewBox="0 0 700 180" className="w-full h-44">
            <defs>
              <linearGradient id="solarGrad" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stopColor="#B8ED68" stopOpacity="0.4" />
                <stop offset="100%" stopColor="#B8ED68" stopOpacity="0.0" />
              </linearGradient>
            </defs>

            {/* Grid lines */}
            {[0, 0.33, 0.66, 1].map((ratio) => {
              const y = 20 + 130 * ratio;
              return (
                <line
                  key={ratio}
                  x1="30"
                  y1={y}
                  x2="670"
                  y2={y}
                  stroke="#132942"
                  strokeWidth="1"
                />
              );
            })}

            {/* Area path */}
            <path
              d={`M 50,${150 - (curve[0].kw / maxKw) * 120} 
                 C 150,${150 - (curve[1].kw / maxKw) * 120} 250,${150 - (curve[2].kw / maxKw) * 120} 350,${150 - (curve[3].kw / maxKw) * 120}
                 C 450,${150 - (curve[4].kw / maxKw) * 120} 550,${150 - (curve[5].kw / maxKw) * 120} 650,${150 - (curve[7].kw / maxKw) * 120}
                 L 650,150 L 50,150 Z`}
              fill="url(#solarGrad)"
            />

            {/* Line path */}
            <path
              d={`M 50,${150 - (curve[0].kw / maxKw) * 120} 
                 C 150,${150 - (curve[1].kw / maxKw) * 120} 250,${150 - (curve[2].kw / maxKw) * 120} 350,${150 - (curve[3].kw / maxKw) * 120}
                 C 450,${150 - (curve[4].kw / maxKw) * 120} 550,${150 - (curve[5].kw / maxKw) * 120} 650,${150 - (curve[7].kw / maxKw) * 120}`}
              fill="none"
              stroke="#B8ED68"
              strokeWidth="3"
            />

            {/* Peak indicator */}
            <circle cx="350" cy={150 - (3.82 / maxKw) * 120} r="5" fill="#B8ED68" stroke="#0E1D30" strokeWidth="2" />
            <text x="350" y={150 - (3.82 / maxKw) * 120 - 10} textAnchor="middle" fill="#B8ED68" className="text-[11px] font-bold">
              3.82 kW Peak
            </text>

            {/* X Labels */}
            {curve.map((pt, i) => (
              <text
                key={pt.time}
                x={50 + (i / (curve.length - 1)) * 600}
                y="170"
                textAnchor="middle"
                fill="#8DA5BA"
                className="text-[10px]"
              >
                {pt.time}
              </text>
            ))}
          </svg>
        </div>
      </div>
    </div>
  );
};
