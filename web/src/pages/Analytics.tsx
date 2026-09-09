import React, { useState } from 'react';
import { Device } from '../types';
import { EnergyChart } from '../components/dashboard/EnergyChart';
import { api } from '../services/api';
import { Button } from '../components/common/Button';
import { Download, Clock, IndianRupee, Award } from 'lucide-react';

interface AnalyticsProps {
  devices: Device[];
  onShowToast: (msg: string) => void;
}

export const Analytics: React.FC<AnalyticsProps> = ({ devices, onShowToast }) => {
  const [timeRange, setTimeRange] = useState('7 Days');
  const chartData = api.getEnergyReadings(timeRange);

  const handleExportCSV = () => {
    const header = "Device Name,Location,Category,Power (kW),Today Energy (kWh),Cost (INR),Status,Is On\n";
    const rows = devices.map(d => 
      `"${d.name}","${d.location}","${d.category}",${d.powerKw},${d.todayKwh},${d.estimatedCost},"${d.isOnline ? 'Online' : 'Offline'}","${d.isOn ? 'ON' : 'OFF'}"`
    ).join("\n");
    const csvContent = "data:text/csv;charset=utf-8," + encodeURI(header + rows);
    const link = document.createElement("a");
    link.setAttribute("href", csvContent);
    link.setAttribute("download", `EnergyHub_Analytics_${timeRange.replace(" ", "_")}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    onShowToast("Analytics CSV exported successfully");
  };

  const sortedDevices = [...devices].sort((a, b) => b.todayKwh - a.todayKwh);
  const maxDeviceKwh = Math.max(...devices.map(d => d.todayKwh), 1);

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Page Header & Export Action */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-bold text-[#EDF7FF]">Analytics & Deep Metrics</h2>
          <p className="text-xs text-[#8DA5BA]">Detailed historical trends, device comparisons, and efficiency rating</p>
        </div>

        <Button
          variant="secondary"
          size="sm"
          icon={<Download className="w-4 h-4 text-[#57E1DC]" />}
          onClick={handleExportCSV}
        >
          Export CSV Report
        </Button>
      </div>

      {/* Main Consumption Chart */}
      <EnergyChart
        data={chartData}
        timeRange={timeRange}
        onTimeRangeChange={setTimeRange}
      />

      {/* 2 Middle Cards: Cost Analysis & Peak Window */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {/* Cost Analysis Card */}
        <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl flex flex-col justify-between">
          <div>
            <div className="flex items-center justify-between mb-3">
              <span className="text-xs font-semibold text-[#8DA5BA] uppercase">Cost Projection</span>
              <div className="p-2 rounded-xl bg-[#FFB866]/15 border border-[#FFB866]/30 text-[#FFB866]">
                <IndianRupee className="w-4 h-4" />
              </div>
            </div>
            <h3 className="text-2xl font-bold text-[#EDF7FF] mb-1">₹196.80</h3>
            <p className="text-xs text-[#8DA5BA]">Estimated cost for current period at ₹8.00/kWh standard tariff</p>
          </div>

          <div className="mt-4 pt-3 border-t border-[#1D3850] flex items-center justify-between text-xs">
            <span className="text-[#8DA5BA]">Comparison vs Last Period:</span>
            <span className="font-bold text-[#B8ED68]">-6.2% (-₹13.00)</span>
          </div>
        </div>

        {/* Peak Usage Window Card */}
        <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl flex flex-col justify-between">
          <div>
            <div className="flex items-center justify-between mb-3">
              <span className="text-xs font-semibold text-[#8DA5BA] uppercase">Peak Usage Window</span>
              <div className="p-2 rounded-xl bg-[#57E1DC]/15 border border-[#57E1DC]/30 text-[#57E1DC]">
                <Clock className="w-4 h-4" />
              </div>
            </div>
            <h3 className="text-2xl font-bold text-[#FFB866] mb-1">2:00 PM – 5:30 PM</h3>
            <p className="text-xs text-[#8DA5BA]">Peak load reaches 3.40 kW due to simultaneous cooling & refrigeration</p>
          </div>

          <div className="mt-4 pt-3 border-t border-[#1D3850] flex items-center justify-between text-xs">
            <span className="text-[#8DA5BA]">Peak Surcharge:</span>
            <span className="font-bold text-[#FFB866]">₹9.20/kWh applied</span>
          </div>
        </div>
      </div>

      {/* Device Comparison Bar Chart */}
      <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl">
        <h3 className="text-base font-bold text-[#EDF7FF] mb-1">Appliance Consumption Comparison</h3>
        <p className="text-xs text-[#8DA5BA] mb-5">Ranked by total kilowatt-hours consumed today</p>

        <div className="space-y-3.5">
          {sortedDevices.map((dev) => {
            const pct = Math.min((dev.todayKwh / maxDeviceKwh) * 100, 100);
            return (
              <div key={dev.id} className="space-y-1.5">
                <div className="flex items-center justify-between text-xs">
                  <div className="flex items-center gap-2">
                    <span className="font-semibold text-[#EDF7FF]">{dev.name}</span>
                    <span className="text-[11px] text-[#8DA5BA]">({dev.location})</span>
                  </div>
                  <div className="flex items-center gap-3">
                    <span className="text-[#8DA5BA]">₹{dev.estimatedCost.toFixed(2)}</span>
                    <span className="font-bold text-[#57E1DC]">{dev.todayKwh} kWh</span>
                  </div>
                </div>

                <div className="h-2 w-full bg-[#132942] rounded-full overflow-hidden">
                  <div
                    className={`h-full rounded-full transition-all duration-500 ${
                      dev.isOn ? 'bg-[#57E1DC]' : 'bg-[#8DA5BA]/40'
                    }`}
                    style={{ width: `${pct}%` }}
                  />
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* Energy Efficiency Rating */}
      <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl flex flex-col sm:flex-row items-center gap-6">
        <div className="relative flex items-center justify-center w-28 h-28 rounded-full border-4 border-[#B8ED68] bg-[#132942] shadow-lg">
          <div className="text-center">
            <span className="text-3xl font-extrabold text-[#EDF7FF]">88</span>
            <span className="block text-[10px] text-[#8DA5BA] uppercase font-bold">Grade A</span>
          </div>
        </div>

        <div className="flex-1 text-center sm:text-left">
          <div className="flex items-center justify-center sm:justify-start gap-2 mb-1">
            <Award className="w-5 h-5 text-[#B8ED68]" />
            <h4 className="text-base font-bold text-[#EDF7FF]">Household Efficiency Rating</h4>
          </div>
          <p className="text-xs text-[#8DA5BA] leading-relaxed mb-3">
            Your home is performing in the top 12% of energy-optimized residential systems. Peak clipping and midday solar utilization contributed +4 efficiency points this week.
          </p>
          <span className="inline-block px-3 py-1 rounded-lg bg-[#B8ED68]/15 border border-[#B8ED68]/30 text-[#B8ED68] text-xs font-bold">
            +12.5% energy saved vs community average
          </span>
        </div>
      </div>
    </div>
  );
};
