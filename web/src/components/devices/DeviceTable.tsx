import React from 'react';
import { Device } from '../../types';
import { getCategoryIcon } from './DeviceCard';
import { Trash2 } from 'lucide-react';

interface DeviceTableProps {
  devices: Device[];
  onToggle: (id: string) => void;
  onDelete: (id: string) => void;
}

export const DeviceTable: React.FC<DeviceTableProps> = ({
  devices,
  onToggle,
  onDelete
}) => {
  return (
    <div className="overflow-x-auto bg-[#0E1D30] border border-[#1D3850] rounded-2xl">
      <table className="w-full text-left text-xs md:text-sm">
        <thead className="bg-[#132942]/60 text-[#8DA5BA] uppercase text-[11px] font-semibold border-b border-[#1D3850]">
          <tr>
            <th className="px-5 py-3.5">Device</th>
            <th className="px-4 py-3.5">Location</th>
            <th className="px-4 py-3.5">Status</th>
            <th className="px-4 py-3.5">Current Power</th>
            <th className="px-4 py-3.5">Today's Energy</th>
            <th className="px-4 py-3.5">Est. Cost</th>
            <th className="px-4 py-3.5 text-right">Actions</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-[#1D3850]">
          {devices.map((device) => (
            <tr key={device.id} className="hover:bg-[#132942]/30 transition-colors">
              <td className="px-5 py-3.5">
                <div className="flex items-center gap-2.5">
                  <div className={`p-1.5 rounded-lg ${device.isOn ? 'bg-[#57E1DC]/15 text-[#57E1DC]' : 'bg-[#132942] text-[#8DA5BA]'}`}>
                    {getCategoryIcon(device.category)}
                  </div>
                  <div>
                    <p className="font-bold text-[#EDF7FF]">{device.name}</p>
                    <p className="text-[11px] text-[#8DA5BA]">{device.category}</p>
                  </div>
                </div>
              </td>
              <td className="px-4 py-3.5 text-[#8DA5BA]">{device.location}</td>
              <td className="px-4 py-3.5">
                <span className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-semibold ${
                  device.isOnline
                    ? 'bg-[#B8ED68]/15 text-[#B8ED68] border border-[#B8ED68]/30'
                    : 'bg-[#FFB866]/15 text-[#FFB866] border border-[#FFB866]/30'
                }`}>
                  <span className={`w-1.5 h-1.5 rounded-full ${device.isOnline ? 'bg-[#B8ED68]' : 'bg-[#FFB866]'}`} />
                  {device.isOnline ? 'Online' : 'Offline'}
                </span>
              </td>
              <td className="px-4 py-3.5 font-bold text-[#57E1DC]">
                {device.powerKw.toFixed(2)} kW
              </td>
              <td className="px-4 py-3.5 text-[#EDF7FF]">
                {device.todayKwh.toFixed(1)} kWh
              </td>
              <td className="px-4 py-3.5 font-semibold text-[#B8ED68]">
                ₹{device.estimatedCost.toFixed(2)}
              </td>
              <td className="px-4 py-3.5 text-right">
                <div className="flex items-center justify-end gap-3">
                  <button
                    type="button"
                    onClick={() => onToggle(device.id)}
                    className={`relative inline-flex h-5 w-9 flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent transition-colors duration-200 ease-in-out focus:outline-none ${
                      device.isOn ? 'bg-[#57E1DC]' : 'bg-[#132942]'
                    }`}
                  >
                    <span
                      className={`pointer-events-none inline-block h-4 w-4 transform rounded-full bg-[#07111F] shadow ring-0 transition duration-200 ease-in-out ${
                        device.isOn ? 'translate-x-4' : 'translate-x-0'
                      }`}
                    />
                  </button>
                  <button
                    onClick={() => onDelete(device.id)}
                    className="p-1.5 text-[#8DA5BA] hover:text-red-400 rounded-lg hover:bg-[#132942] transition-colors"
                    title="Remove device"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
