export type DeviceCategory = 
  | 'Air Conditioning'
  | 'Refrigeration'
  | 'Lighting'
  | 'Entertainment'
  | 'Kitchen'
  | 'Other';

export interface Device {
  id: string;
  name: string;
  location: string;
  category: DeviceCategory;
  powerKw: number;
  ratedPowerKw: number;
  todayKwh: number;
  estimatedCost: number;
  lastActive: string;
  isOnline: boolean;
  isOn: boolean;
}

export type RecommendationPriority = 'HIGH' | 'MEDIUM' | 'LOW';

export interface Recommendation {
  id: string;
  priority: RecommendationPriority;
  title: string;
  explanation: string;
  potentialSavings: string;
  actionLabel: string;
  deviceId?: string;
  isCompleted?: boolean;
  isDismissed?: boolean;
}

export type NotificationType = 'SUCCESS' | 'WARNING' | 'INFO';

export interface NotificationItem {
  id: string;
  title: string;
  message: string;
  timeAgo: string;
  type: NotificationType;
  isRead: boolean;
}

export interface SolarMetrics {
  generationKwh: number;
  consumptionKwh: number;
  gridImportKwh: number;
  gridExportKwh: number;
  solarCoveragePct: number;
  currentSolarKw: number;
  currentInverterKw: number;
  currentHomeKw: number;
  currentGridKw: number;
  solarEfficiencyPct: number;
  estimatedSavingsRupees: number;
}

export interface EnergyDataPoint {
  label: string;
  currentKwh: number;
  previousKwh: number;
  isPeak?: boolean;
}

export interface EnergyBreakdownItem {
  category: DeviceCategory;
  percentage: number;
  kwh: number;
  color: string;
}

export interface AppSettings {
  userName: string;
  userEmail: string;
  electricityRate: number;
  dailyTargetKwh: number;
  monthlyTargetKwh: number;
  alertHighConsumption: boolean;
  alertDeviceOffline: boolean;
  alertDailySummary: boolean;
  alertRecommendations: boolean;
  compactMode: boolean;
}

export type NavTab = 
  | 'overview'
  | 'analytics'
  | 'devices'
  | 'recommendations'
  | 'solar'
  | 'settings';
