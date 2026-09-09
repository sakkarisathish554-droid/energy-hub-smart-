import { useState, useEffect, useCallback } from 'react';
import { Device, DeviceCategory } from '../types';
import { api } from '../services/api';

export const useDevices = () => {
  const [devices, setDevices] = useState<Device[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchDevices = useCallback(async () => {
    try {
      const data = await api.getDevices();
      setDevices(data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchDevices();
  }, [fetchDevices]);

  const toggleDevice = async (id: string) => {
    const updated = await api.toggleDevice(id);
    setDevices(updated);
    const target = updated.find(d => d.id === id);
    return target;
  };

  const addDevice = async (
    name: string,
    location: string,
    category: DeviceCategory,
    powerKw: number
  ) => {
    const updated = await api.addDevice(name, location, category, powerKw);
    setDevices(updated);
  };

  const deleteDevice = async (id: string) => {
    const updated = await api.deleteDevice(id);
    setDevices(updated);
  };

  return {
    devices,
    loading,
    toggleDevice,
    addDevice,
    deleteDevice,
    refreshDevices: fetchDevices
  };
};
