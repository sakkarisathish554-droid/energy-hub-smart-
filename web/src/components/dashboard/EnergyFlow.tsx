import React from 'react';
import { SolarMetrics } from '../../types';
import { Sun, Zap, Home, Grid } from 'lucide-react';

interface EnergyFlowProps {
  metrics: SolarMetrics;
}

export const EnergyFlow: React.FC<EnergyFlowProps> = ({ metrics }) => {
  return (
    <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl">
      <div className="flex items-center justify-between mb-4">
        <div>
          <h3 className="text-base font-bold text-[#EDF7FF]">Live Energy Flow</h3>
          <p className="text-xs text-[#8DA5BA]">Real-time power routing across generation & home nodes</p>
        </div>
        <div className="flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-[#B8ED68]/15 border border-[#B8ED68]/30 text-[#B8ED68] text-xs font-bold">
          <span className="w-2 h-2 rounded-full bg-[#B8ED68] animate-ping" />
          Active Flow
        </div>
      </div>

      {/* Nodes and Flow Connectors */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 my-2">
        {/* Node 1: Solar */}
        <div className="p-3.5 bg-[#132942] border border-[#57E1DC]/30 rounded-xl text-center flex flex-col items-center">
          <div className="p-2 rounded-lg bg-[#57E1DC]/20 text-[#57E1DC] mb-2">
            <Sun className="w-5 h-5" />
          </div>
          <span className="text-[11px] text-[#8DA5BA]">Solar Panels</span>
          <span className="text-sm font-bold text-[#57E1DC]">{metrics.currentSolarKw} kW</span>
        </div>

        {/* Node 2: Inverter */}
        <div className="p-3.5 bg-[#132942] border border-[#B8ED68]/30 rounded-xl text-center flex flex-col items-center">
          <div className="p-2 rounded-lg bg-[#B8ED68]/20 text-[#B8ED68] mb-2">
            <Zap className="w-5 h-5" />
          </div>
          <span className="text-[11px] text-[#8DA5BA]">Solar Inverter</span>
          <span className="text-sm font-bold text-[#B8ED68]">{metrics.currentInverterKw} kW</span>
        </div>

        {/* Node 3: Home */}
        <div className="p-3.5 bg-[#132942] border border-[#FFB866]/30 rounded-xl text-center flex flex-col items-center">
          <div className="p-2 rounded-lg bg-[#FFB866]/20 text-[#FFB866] mb-2">
            <Home className="w-5 h-5" />
          </div>
          <span className="text-[11px] text-[#8DA5BA]">Home Load</span>
          <span className="text-sm font-bold text-[#FFB866]">{metrics.currentHomeKw} kW</span>
        </div>

        {/* Node 4: Grid */}
        <div className="p-3.5 bg-[#132942] border border-[#8DA5BA]/30 rounded-xl text-center flex flex-col items-center">
          <div className="p-2 rounded-lg bg-[#8DA5BA]/20 text-[#8DA5BA] mb-2">
            <Grid className="w-5 h-5" />
          </div>
          <span className="text-[11px] text-[#8DA5BA]">Grid Power</span>
          <span className="text-sm font-bold text-[#EDF7FF]">{metrics.currentGridKw} kW</span>
        </div>
      </div>

      <div className="mt-4 pt-3 border-t border-[#1D3850] flex flex-col sm:flex-row sm:items-center justify-between gap-2 text-xs">
        <div className="flex items-center gap-2">
          <span className="text-[#8DA5BA]">Inverter Efficiency:</span>
          <span className="font-bold text-[#B8ED68]">{metrics.solarEfficiencyPct}%</span>
        </div>
        <div className="flex items-center gap-2">
          <span className="text-[#8DA5BA]">Today's Solar Coverage:</span>
          <span className="font-bold text-[#57E1DC]">{metrics.solarCoveragePct}%</span>
        </div>
        <div className="flex items-center gap-2">
          <span className="text-[#8DA5BA]">Estimated Savings:</span>
          <span className="font-bold text-[#B8ED68]">₹{metrics.estimatedSavingsRupees}</span>
        </div>
      </div>
    </div>
  );
};
