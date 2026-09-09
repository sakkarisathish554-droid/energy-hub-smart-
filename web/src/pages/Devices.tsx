import React, { useState } from 'react';
import { Device, DeviceCategory } from '../types';
import { DeviceCard } from '../components/devices/DeviceCard';
import { DeviceTable } from '../components/devices/DeviceTable';
import { AddDeviceModal } from '../components/devices/AddDeviceModal';
import { Search } from '../components/common/Search';
import { Button } from '../components/common/Button';
import { EmptyState } from '../components/common/EmptyState';
import { Plus, LayoutGrid, List, Cpu } from 'lucide-react';

interface DevicesPageProps {
  devices: Device[];
  onToggleDevice: (id: string) => void;
  onAddDevice: (name: string, location: string, category: DeviceCategory, powerKw: number) => void;
  onDeleteDevice: (id: string) => void;
}

export const Devices: React.FC<DevicesPageProps> = ({
  devices,
  onToggleDevice,
  onAddDevice,
  onDeleteDevice
}) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedLocation, setSelectedLocation] = useState('All');
  const [viewMode, setViewMode] = useState<'grid' | 'table'>('grid');
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);

  const locations = ['All', 'Living Room', 'Kitchen', 'Bedroom', 'Utility Roof', 'Whole Home'];

  const filteredDevices = devices.filter((d) => {
    const matchesSearch =
      d.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      d.category.toLowerCase().includes(searchQuery.toLowerCase()) ||
      d.location.toLowerCase().includes(searchQuery.toLowerCase());

    const matchesLocation =
      selectedLocation === 'All' || d.location.toLowerCase().includes(selectedLocation.toLowerCase());

    return matchesSearch && matchesLocation;
  });

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Header and Add Button */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-bold text-[#EDF7FF]">Smart Devices Management</h2>
          <p className="text-xs text-[#8DA5BA]">
            {devices.length} appliances configured • {devices.filter(d => d.isOn).length} actively drawing wattage
          </p>
        </div>

        <div className="flex items-center gap-2">
          {/* View Toggle */}
          <div className="flex items-center p-1 bg-[#132942] border border-[#1D3850] rounded-xl">
            <button
              onClick={() => setViewMode('grid')}
              className={`p-1.5 rounded-lg transition-colors ${
                viewMode === 'grid' ? 'bg-[#57E1DC] text-[#07111F]' : 'text-[#8DA5BA] hover:text-[#EDF7FF]'
              }`}
              title="Grid View"
            >
              <LayoutGrid className="w-4 h-4" />
            </button>
            <button
              onClick={() => setViewMode('table')}
              className={`p-1.5 rounded-lg transition-colors ${
                viewMode === 'table' ? 'bg-[#57E1DC] text-[#07111F]' : 'text-[#8DA5BA] hover:text-[#EDF7FF]'
              }`}
              title="Table View"
            >
              <List className="w-4 h-4" />
            </button>
          </div>

          <Button
            variant="primary"
            size="sm"
            icon={<Plus className="w-4 h-4" />}
            onClick={() => setIsAddModalOpen(true)}
          >
            Add Device
          </Button>
        </div>
      </div>

      {/* Filter and Search Bar */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <Search
          value={searchQuery}
          onChange={setSearchQuery}
          placeholder="Search devices by name, category, location..."
          className="w-full md:w-80"
        />

        {/* Location Filter Pills */}
        <div className="flex items-center gap-2 overflow-x-auto pb-1 md:pb-0 scrollbar-none">
          {locations.map((loc) => (
            <button
              key={loc}
              onClick={() => setSelectedLocation(loc)}
              className={`px-3 py-1.5 rounded-xl text-xs font-semibold whitespace-nowrap transition-all ${
                selectedLocation === loc
                  ? 'bg-[#57E1DC]/20 text-[#57E1DC] border border-[#57E1DC]/40'
                  : 'bg-[#132942] text-[#8DA5BA] hover:text-[#EDF7FF] border border-[#1D3850]'
              }`}
            >
              {loc}
            </button>
          ))}
        </div>
      </div>

      {/* Device List or Empty State */}
      {filteredDevices.length === 0 ? (
        <EmptyState
          icon={<Cpu className="w-8 h-8" />}
          title="No appliances found"
          description="Try adjusting your search criteria or location filters to see matching smart devices."
          actionLabel="Clear Filters"
          onAction={() => {
            setSearchQuery('');
            setSelectedLocation('All');
          }}
        />
      ) : viewMode === 'grid' ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {filteredDevices.map((device) => (
            <DeviceCard
              key={device.id}
              device={device}
              onToggle={() => onToggleDevice(device.id)}
              onDelete={() => onDeleteDevice(device.id)}
            />
          ))}
        </div>
      ) : (
        <DeviceTable
          devices={filteredDevices}
          onToggle={onToggleDevice}
          onDelete={onDeleteDevice}
        />
      )}

      {/* Add Device Modal */}
      <AddDeviceModal
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        onAdd={onAddDevice}
      />
    </div>
  );
};
