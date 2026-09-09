import React from 'react';
import { Device, DeviceCategory } from '../../types';
import {
  Wind,
  Refrigerator,
  Lightbulb,
  Tv,
  Utensils,
  Zap
} from 'lucide-react';

interface DeviceCardProps {
  device: Device;
  onToggle: () => void;
  onDelete?: () => void;
}

export const getCategoryIcon = (category: DeviceCategory) => {
  switch (category) {
    case 'Air Conditioning':
      return <Wind className="w-4 h-4" />;
    case 'Refrigeration':
      return <Refrigerator className="w-4 h-4" />;
    case 'Lighting':
      return <Lightbulb className="w-4 h-4" />;
    case 'Entertainment':
      return <Tv className="w-4 h-4" />;
    case 'Kitchen':
      return <Utensils className="w-4 h-4" />;
    default:
      return <Zap className="w-4 h-4" />;
  }
};

export const DeviceCard: React.FC<DeviceCardProps> = ({
  device,
  onToggle
}) => {
  return (
    <div className="p-4 bg-[#0E1D30] border border-[#1D3850] rounded-2xl flex flex-col justify-between card-hover">
      <div>
        <div className="flex items-start justify-between gap-2 mb-3">
          <div className="flex items-center gap-2.5">
            <div
              className={`p-2 rounded-xl transition-colors ${
                device.isOn
                  ? 'bg-[#57E1DC]/15 text-[#57E1DC] border border-[#57E1DC]/30'
                  : 'bg-[#132942] text-[#8DA5BA] border border-[#1D3850]'
              }`}
            >
              {getCategoryIcon(device.category)}
            </div>
            <div>
              <h4 className="text-sm font-bold text-[#EDF7FF] truncate max-w-[130px]">
                {device.name}
              </h4>
              <div className="flex items-center gap-1.5 text-[11px] text-[#8DA5BA]">
                <span>{device.location}</span>
                <span>•</span>
                <span className={`inline-flex items-center gap-1 ${device.isOnline ? 'text-[#B8ED68]' : 'text-[#FFB866]'}`}>
                  <span className={`w-1.5 h-1.5 rounded-full ${device.isOnline ? 'bg-[#B8ED68]' : 'bg-[#FFB866]'}`} />
                  {device.isOnline ? 'Online' : 'Offline'}
                </span>
              </div>
            </div>
          </div>

          {/* Switch */}
          <button
            type="button"
            onClick={onToggle}
            className={`relative inline-flex h-6 w-11 flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent transition-colors duration-200 ease-in-out focus:outline-none ${
              device.isOn ? 'bg-[#57E1DC]' : 'bg-[#132942]'
            }`}
          >
            <span
              className={`pointer-events-none inline-block h-5 w-5 transform rounded-full bg-[#07111F] shadow ring-0 transition duration-200 ease-in-out ${
                device.isOn ? 'translate-x-5' : 'translate-x-0'
              }`}
            />
          </button>
        </div>
      </div>

      {/* Metrics Footer */}
      <div className="pt-3 border-t border-[#1D3850] flex items-center justify-between text-xs bg-[#132942]/40 -mx-4 -mb-4 px-4 py-2.5 rounded-b-2xl">
        <div className="flex items-center gap-1">
          <Zap className={`w-3.5 h-3.5 ${device.isOn ? 'text-[#57E1DC]' : 'text-[#8DA5BA]'}`} />
          <span className={`font-bold ${device.isOn ? 'text-[#57E1DC]' : 'text-[#8DA5BA]'}`}>
            {device.powerKw.toFixed(2)} kW
          </span>
        </div>

        <span className="text-[#8DA5BA]">
          Today: <strong className="text-[#EDF7FF]">{device.todayKwh.toFixed(1)} kWh</strong>
        </span>

        <span className="font-semibold text-[#B8ED68]">
          ₹{device.estimatedCost.toFixed(2)}
        </span>
      </div>
    </div>
  );
};
