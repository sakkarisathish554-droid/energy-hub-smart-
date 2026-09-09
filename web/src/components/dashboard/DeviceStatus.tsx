import React from 'react';
import { Device } from '../../types';
import { DeviceCard } from '../devices/DeviceCard';
import { ArrowRight } from 'lucide-react';

interface DeviceStatusProps {
  devices: Device[];
  onToggle: (id: string) => void;
  onViewAll: () => void;
}

export const DeviceStatus: React.FC<DeviceStatusProps> = ({
  devices,
  onToggle,
  onViewAll
}) => {
  const activeCount = devices.filter(d => d.isOn).length;

  return (
    <div className="space-y-3">
      <div className="flex items-center justify-between">
        <div>
          <h3 className="text-base font-bold text-[#EDF7FF]">Smart Devices</h3>
          <p className="text-xs text-[#8DA5BA]">
            {activeCount} of {devices.length} appliances actively drawing power
          </p>
        </div>

        <button
          onClick={onViewAll}
          className="flex items-center gap-1 text-xs font-bold text-[#57E1DC] hover:underline"
        >
          View All
          <ArrowRight className="w-3.5 h-3.5" />
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3.5">
        {devices.slice(0, 6).map((device) => (
          <DeviceCard
            key={device.id}
            device={device}
            onToggle={() => onToggle(device.id)}
          />
        ))}
      </div>
    </div>
  );
};
