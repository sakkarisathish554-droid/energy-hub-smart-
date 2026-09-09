import {
  initialDevices,
  initialRecommendations,
  initialNotifications,
  initialSolarMetrics,
  initialSettings,
  energyChartData,
  initialBreakdown
} from '../data/mockData';
import {
  Device,
  Recommendation,
  NotificationItem,
  SolarMetrics,
  AppSettings,
  EnergyDataPoint,
  EnergyBreakdownItem,
  DeviceCategory
} from '../types';

const STORAGE_KEYS = {
  DEVICES: 'energyhub_devices',
  SETTINGS: 'energyhub_settings',
  NOTIFICATIONS: 'energyhub_notifications',
  RECOMMENDATIONS: 'energyhub_recommendations'
};

export const api = {
  getDevices: async (): Promise<Device[]> => {
    const saved = localStorage.getItem(STORAGE_KEYS.DEVICES);
    if (saved) {
      try {
        return JSON.parse(saved);
      } catch (e) {
        console.error('Failed to parse saved devices', e);
      }
    }
    return initialDevices;
  },

  saveDevices: async (devices: Device[]): Promise<void> => {
    localStorage.setItem(STORAGE_KEYS.DEVICES, JSON.stringify(devices));
  },

  toggleDevice: async (id: string): Promise<Device[]> => {
    const devices = await api.getDevices();
    const updated = devices.map(d => {
      if (d.id === id) {
        const nextOn = !d.isOn;
        return {
          ...d,
          isOn: nextOn,
          powerKw: nextOn ? d.ratedPowerKw : 0.0,
          isOnline: nextOn ? true : d.isOnline,
          lastActive: 'Active now'
        };
      }
      return d;
    });
    await api.saveDevices(updated);
    return updated;
  },

  addDevice: async (
    name: string,
    location: string,
    category: DeviceCategory,
    powerKw: number
  ): Promise<Device[]> => {
    const devices = await api.getDevices();
    const newDevice: Device = {
      id: `dev_${Date.now()}`,
      name,
      location,
      category,
      powerKw,
      ratedPowerKw: powerKw,
      todayKwh: 0.0,
      estimatedCost: 0.0,
      lastActive: 'Just added',
      isOnline: true,
      isOn: true
    };
    const updated = [...devices, newDevice];
    await api.saveDevices(updated);
    return updated;
  },

  deleteDevice: async (id: string): Promise<Device[]> => {
    const devices = await api.getDevices();
    const updated = devices.filter(d => d.id !== id);
    await api.saveDevices(updated);
    return updated;
  },

  getRecommendations: async (): Promise<Recommendation[]> => {
    const saved = localStorage.getItem(STORAGE_KEYS.RECOMMENDATIONS);
    if (saved) {
      try {
        return JSON.parse(saved);
      } catch (e) {
        console.error('Failed to parse recommendations', e);
      }
    }
    return initialRecommendations;
  },

  updateRecommendations: async (recs: Recommendation[]): Promise<void> => {
    localStorage.setItem(STORAGE_KEYS.RECOMMENDATIONS, JSON.stringify(recs));
  },

  getNotifications: async (): Promise<NotificationItem[]> => {
    const saved = localStorage.getItem(STORAGE_KEYS.NOTIFICATIONS);
    if (saved) {
      try {
        return JSON.parse(saved);
      } catch (e) {
        console.error('Failed to parse notifications', e);
      }
    }
    return initialNotifications;
  },

  updateNotifications: async (notifs: NotificationItem[]): Promise<void> => {
    localStorage.setItem(STORAGE_KEYS.NOTIFICATIONS, JSON.stringify(notifs));
  },

  getSolarMetrics: async (): Promise<SolarMetrics> => {
    return initialSolarMetrics;
  },

  getSettings: async (): Promise<AppSettings> => {
    const saved = localStorage.getItem(STORAGE_KEYS.SETTINGS);
    if (saved) {
      try {
        return JSON.parse(saved);
      } catch (e) {
        console.error('Failed to parse settings', e);
      }
    }
    return initialSettings;
  },

  saveSettings: async (settings: AppSettings): Promise<void> => {
    localStorage.setItem(STORAGE_KEYS.SETTINGS, JSON.stringify(settings));
  },

  getEnergyReadings: (timeRange: string): EnergyDataPoint[] => {
    return energyChartData[timeRange] || energyChartData['Today'];
  },

  getEnergyBreakdown: (): EnergyBreakdownItem[] => {
    return initialBreakdown;
  },

  resetAllData: async (): Promise<void> => {
    localStorage.removeItem(STORAGE_KEYS.DEVICES);
    localStorage.removeItem(STORAGE_KEYS.SETTINGS);
    localStorage.removeItem(STORAGE_KEYS.NOTIFICATIONS);
    localStorage.removeItem(STORAGE_KEYS.RECOMMENDATIONS);
  }
};
