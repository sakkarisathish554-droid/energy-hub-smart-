import React, { useState } from 'react';
import { Device, SolarMetrics, NavTab } from '../types';
import { StatCard } from '../components/dashboard/StatCard';
import { EnergyChart } from '../components/dashboard/EnergyChart';
import { EnergyBreakdown } from '../components/dashboard/EnergyBreakdown';
import { DeviceStatus } from '../components/dashboard/DeviceStatus';
import { api } from '../services/api';
import { Zap, IndianRupee, Leaf, Sun, ArrowRight } from 'lucide-react';

interface OverviewProps {
  devices: Device[];
  solarMetrics: SolarMetrics;
  onToggleDevice: (id: string) => void;
  onNavigate: (tab: NavTab) => void;
}

export const Overview: React.FC<OverviewProps> = ({
  devices,
  solarMetrics,
  onToggleDevice,
  onNavigate
}) => {
  const [timeRange, setTimeRange] = useState('Today');
  const chartData = api.getEnergyReadings(timeRange);
  const breakdownItems = api.getEnergyBreakdown();

  return (
    <div className="space-y-6 animate-fade-in">
      {/* 4 KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard
          title="Total Energy"
          value="24.6"
          unit="kWh"
          subtitle="Today's consumption"
          trend="-8.4%"
          isPositiveTrendGood={false}
          isTrendingUp={false}
          icon={<Zap className="w-5 h-5" />}
          accentColor="#57E1DC"
        />
        <StatCard
          title="Estimated Cost"
          value="₹196.80"
          subtitle="Today's estimated cost"
          trend="-6.2%"
          isPositiveTrendGood={false}
          isTrendingUp={false}
          icon={<IndianRupee className="w-5 h-5" />}
          accentColor="#FFB866"
        />
        <StatCard
          title="Energy Saved"
          value="7.8"
          unit="kWh"
          subtitle="Saved this week"
          trend="+12.5%"
          isPositiveTrendGood={true}
          isTrendingUp={true}
          icon={<Leaf className="w-5 h-5" />}
          accentColor="#B8ED68"
        />
        <StatCard
          title="Solar Generated"
          value="18.4"
          unit="kWh"
          subtitle="Today's solar generation"
          trend="+9.3%"
          isPositiveTrendGood={true}
          isTrendingUp={true}
          icon={<Sun className="w-5 h-5" />}
          accentColor="#57E1DC"
        />
      </div>

      {/* Main Chart */}
      <EnergyChart
        data={chartData}
        timeRange={timeRange}
        onTimeRangeChange={setTimeRange}
      />

      {/* Energy Breakdown & Quick Solar Banner */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2">
          <EnergyBreakdown items={breakdownItems} totalKwh={24.6} />
        </div>

        {/* Solar Highlight Card */}
        <div 
          onClick={() => onNavigate('solar')}
          className="p-5 bg-gradient-to-br from-[#0E1D30] to-[#132942] border border-[#B8ED68]/30 rounded-2xl flex flex-col justify-between cursor-pointer card-hover"
        >
          <div>
            <div className="flex items-center justify-between mb-4">
              <div className="p-3 bg-[#B8ED68]/15 border border-[#B8ED68]/30 rounded-xl text-[#B8ED68]">
                <Sun className="w-6 h-6" />
              </div>
              <span className="px-2.5 py-1 rounded-full text-xs font-bold bg-[#B8ED68]/20 text-[#B8ED68]">
                Self-Sufficient
              </span>
            </div>

            <h3 className="text-base font-bold text-[#EDF7FF] mb-1">
              Solar is covering {solarMetrics.solarCoveragePct}% of usage
            </h3>
            <p className="text-xs text-[#8DA5BA] leading-relaxed mb-4">
              {solarMetrics.generationKwh} kWh generated today, keeping grid import low and saving ₹{solarMetrics.estimatedSavingsRupees}.
            </p>
          </div>

          <div className="flex items-center justify-between pt-3 border-t border-[#1D3850] text-xs font-semibold text-[#B8ED68]">
            <span>View Solar Intelligence</span>
            <ArrowRight className="w-4 h-4" />
          </div>
        </div>
      </div>

      {/* Live Device Status Grid */}
      <DeviceStatus
        devices={devices}
        onToggle={onToggleDevice}
        onViewAll={() => onNavigate('devices')}
      />
    </div>
  );
};
