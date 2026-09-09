import React, { useState } from 'react';
import { DeviceCategory } from '../../types';
import { Modal } from '../common/Modal';
import { Button } from '../common/Button';

interface AddDeviceModalProps {
  isOpen: boolean;
  onClose: () => void;
  onAdd: (name: string, location: string, category: DeviceCategory, powerKw: number) => void;
}

export const AddDeviceModal: React.FC<AddDeviceModalProps> = ({
  isOpen,
  onClose,
  onAdd
}) => {
  const [name, setName] = useState('');
  const [location, setLocation] = useState('Living Room');
  const [category, setCategory] = useState<DeviceCategory>('Air Conditioning');
  const [powerKw, setPowerKw] = useState('1.20');
  const [error, setError] = useState<string | null>(null);

  const categories: DeviceCategory[] = [
    'Air Conditioning',
    'Refrigeration',
    'Lighting',
    'Entertainment',
    'Kitchen',
    'Other'
  ];

  const locations = [
    'Living Room',
    'Kitchen',
    'Master Bedroom',
    'Bedroom 2',
    'Utility Roof',
    'Garage',
    'Whole Home'
  ];

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) {
      setError('Please provide a device name');
      return;
    }
    const kw = parseFloat(powerKw);
    if (isNaN(kw) || kw <= 0) {
      setError('Please specify a positive wattage/power rating in kW');
      return;
    }

    onAdd(name.trim(), location, category, kw);
    setName('');
    setPowerKw('1.20');
    setError(null);
    onClose();
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title="Add Smart Device"
      subtitle="Connect a simulated smart appliance to your EnergyHub"
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        {error && (
          <div className="p-3 bg-red-500/15 border border-red-500/30 rounded-xl text-xs text-red-400 font-medium">
            {error}
          </div>
        )}

        <div>
          <label className="block text-xs font-semibold text-[#8DA5BA] mb-1.5">
            Device Name
          </label>
          <input
            type="text"
            value={name}
            onChange={(e) => {
              setName(e.target.value);
              setError(null);
            }}
            placeholder="e.g. Master Bedroom Inverter AC"
            className="w-full px-3.5 py-2.5 bg-[#132942] border border-[#1D3850] rounded-xl text-sm text-[#EDF7FF] placeholder-[#8DA5BA] focus:outline-none focus:border-[#57E1DC]"
          />
        </div>

        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className="block text-xs font-semibold text-[#8DA5BA] mb-1.5">
              Category
            </label>
            <select
              value={category}
              onChange={(e) => setCategory(e.target.value as DeviceCategory)}
              className="w-full px-3 py-2.5 bg-[#132942] border border-[#1D3850] rounded-xl text-sm text-[#EDF7FF] focus:outline-none focus:border-[#57E1DC]"
            >
              {categories.map((c) => (
                <option key={c} value={c} className="bg-[#0E1D30]">
                  {c}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#8DA5BA] mb-1.5">
              Location
            </label>
            <select
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              className="w-full px-3 py-2.5 bg-[#132942] border border-[#1D3850] rounded-xl text-sm text-[#EDF7FF] focus:outline-none focus:border-[#57E1DC]"
            >
              {locations.map((loc) => (
                <option key={loc} value={loc} className="bg-[#0E1D30]">
                  {loc}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div>
          <label className="block text-xs font-semibold text-[#8DA5BA] mb-1.5">
            Rated Power (kW)
          </label>
          <input
            type="number"
            step="0.01"
            min="0.01"
            value={powerKw}
            onChange={(e) => {
              setPowerKw(e.target.value);
              setError(null);
            }}
            placeholder="1.20"
            className="w-full px-3.5 py-2.5 bg-[#132942] border border-[#1D3850] rounded-xl text-sm text-[#EDF7FF] placeholder-[#8DA5BA] focus:outline-none focus:border-[#57E1DC]"
          />
        </div>

        <div className="flex items-center justify-end gap-2.5 pt-4 border-t border-[#1D3850]">
          <Button type="button" variant="secondary" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" variant="primary">
            Add Device
          </Button>
        </div>
      </form>
    </Modal>
  );
};
