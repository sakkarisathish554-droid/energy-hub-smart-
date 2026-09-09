import {
  Device,
  Recommendation,
  NotificationItem,
  SolarMetrics,
  EnergyBreakdownItem,
  EnergyDataPoint,
  AppSettings
} from '../types';

export const initialDevices: Device[] = [
  {
    id: 'dev_1',
    name: 'Living Room AC',
    location: 'Living Room',
    category: 'Air Conditioning',
    powerKw: 1.24,
    ratedPowerKw: 1.24,
    todayKwh: 6.2,
    estimatedCost: 49.60,
    lastActive: 'Active now',
    isOnline: true,
    isOn: true,
  },
  {
    id: 'dev_2',
    name: 'Smart Refrigerator',
    location: 'Kitchen',
    category: 'Refrigeration',
    powerKw: 0.18,
    ratedPowerKw: 0.18,
    todayKwh: 2.7,
    estimatedCost: 21.60,
    lastActive: 'Active now',
    isOnline: true,
    isOn: true,
  },
  {
    id: 'dev_3',
    name: 'Smart Lights',
    location: 'Whole Home',
    category: 'Lighting',
    powerKw: 0.09,
    ratedPowerKw: 0.09,
    todayKwh: 1.8,
    estimatedCost: 14.40,
    lastActive: 'Active now',
    isOnline: true,
    isOn: true,
  },
  {
    id: 'dev_4',
    name: 'Television',
    location: 'Living Room',
    category: 'Entertainment',
    powerKw: 0.00,
    ratedPowerKw: 0.15,
    todayKwh: 0.9,
    estimatedCost: 7.20,
    lastActive: '2 hours ago',
    isOnline: false,
    isOn: false,
  },
  {
    id: 'dev_5',
    name: 'Ceiling Fan',
    location: 'Bedroom',
    category: 'Other',
    powerKw: 0.07,
    ratedPowerKw: 0.07,
    todayKwh: 0.6,
    estimatedCost: 4.80,
    lastActive: 'Active now',
    isOnline: true,
    isOn: true,
  },
  {
    id: 'dev_6',
    name: 'Solar Inverter',
    location: 'Utility Roof',
    category: 'Other',
    powerKw: 0.04,
    ratedPowerKw: 0.04,
    todayKwh: 0.4,
    estimatedCost: 3.20,
    lastActive: 'Active now',
    isOnline: true,
    isOn: true,
  }
];

export const initialBreakdown: EnergyBreakdownItem[] = [
  { category: 'Air Conditioning', percentage: 42, kwh: 10.3, color: '#57E1DC' },
  { category: 'Refrigeration', percentage: 18, kwh: 4.4, color: '#B8ED68' },
  { category: 'Lighting', percentage: 12, kwh: 3.0, color: '#FFB866' },
  { category: 'Entertainment', percentage: 10, kwh: 2.5, color: '#9D84FD' },
  { category: 'Kitchen', percentage: 11, kwh: 2.7, color: '#4DA6FF' },
  { category: 'Other', percentage: 7, kwh: 1.7, color: '#8DA5BA' },
];

export const initialRecommendations: Recommendation[] = [
  {
    id: 'rec_1',
    priority: 'HIGH',
    title: 'High Air Conditioner Consumption',
    explanation: 'Your air conditioner is responsible for a large portion of today\'s consumption (42%). Increasing thermostat to 24°C saves up to 18% cooling energy.',
    potentialSavings: 'Save up to 1.8 kWh/day (~₹14.40/day)',
    actionLabel: 'Adjust AC Eco Mode',
    deviceId: 'dev_1',
    isCompleted: false,
    isDismissed: false
  },
  {
    id: 'rec_2',
    priority: 'MEDIUM',
    title: 'Extended Lighting Activity Detected',
    explanation: 'Several smart lights have remained active for extended periods in unoccupied rooms during daylight hours.',
    potentialSavings: 'Save up to 0.4 kWh/day (~₹3.20/day)',
    actionLabel: 'Turn Off Idle Lights',
    deviceId: 'dev_3',
    isCompleted: false,
    isDismissed: false
  },
  {
    id: 'rec_3',
    priority: 'LOW',
    title: 'Optimal Solar Generation Utilization',
    explanation: 'Your current solar generation is covering 74.8% of daytime usage. Schedule heavy loads like water heaters or EV charging between 11 AM - 3 PM.',
    potentialSavings: 'Maximize 100% self-consumption',
    actionLabel: 'Schedule Heavy Appliances',
    isCompleted: false,
    isDismissed: false
  }
];

export const initialNotifications: NotificationItem[] = [
  {
    id: 'notif_1',
    title: 'Efficiency Achievement',
    message: 'Energy usage is 8.4% lower than yesterday. Great job maintaining peak savings!',
    timeAgo: '15m ago',
    type: 'SUCCESS',
    isRead: false
  },
  {
    id: 'notif_2',
    title: 'Appliance Alert',
    message: 'Living Room AC has been running continuously for 3 hours at 1.24 kW.',
    timeAgo: '1h ago',
    type: 'WARNING',
    isRead: false
  },
  {
    id: 'notif_3',
    title: 'Solar Target Met',
    message: 'Solar generation reached today\'s target of 18.0 kWh at 2:30 PM.',
    timeAgo: '3h ago',
    type: 'SUCCESS',
    isRead: true
  },
  {
    id: 'notif_4',
    title: 'Device Online',
    message: 'Smart Refrigerator is online and operating within optimal defrost range.',
    timeAgo: '5h ago',
    type: 'INFO',
    isRead: true
  }
];

export const initialSolarMetrics: SolarMetrics = {
  generationKwh: 18.4,
  consumptionKwh: 24.6,
  gridImportKwh: 6.2,
  gridExportKwh: 0.0,
  solarCoveragePct: 74.8,
  currentSolarKw: 3.42,
  currentInverterKw: 3.28,
  currentHomeKw: 1.58,
  currentGridKw: 0.00,
  solarEfficiencyPct: 94.2,
  estimatedSavingsRupees: 147.20
};

export const initialSettings: AppSettings = {
  userName: 'Sathish S.',
  userEmail: 'sathish@energyhub.io',
  electricityRate: 8.00,
  dailyTargetKwh: 25.0,
  monthlyTargetKwh: 750.0,
  alertHighConsumption: true,
  alertDeviceOffline: true,
  alertDailySummary: true,
  alertRecommendations: true,
  compactMode: false
};

export const energyChartData: Record<string, EnergyDataPoint[]> = {
  Today: [
    { label: '00:00', currentKwh: 0.8, previousKwh: 0.9 },
    { label: '03:00', currentKwh: 0.6, previousKwh: 0.7 },
    { label: '06:00', currentKwh: 1.2, previousKwh: 1.4 },
    { label: '09:00', currentKwh: 2.1, previousKwh: 2.4 },
    { label: '12:00', currentKwh: 2.9, previousKwh: 3.2 },
    { label: '15:00', currentKwh: 3.4, previousKwh: 3.6, isPeak: true },
    { label: '18:00', currentKwh: 3.1, previousKwh: 3.3, isPeak: true },
    { label: '21:00', currentKwh: 2.2, previousKwh: 2.5 },
  ],
  '7 Days': [
    { label: 'Mon', currentKwh: 23.4, previousKwh: 25.1 },
    { label: 'Tue', currentKwh: 26.2, previousKwh: 28.0 },
    { label: 'Wed', currentKwh: 24.6, previousKwh: 26.8 },
    { label: 'Thu', currentKwh: 22.8, previousKwh: 24.5 },
    { label: 'Fri', currentKwh: 27.5, previousKwh: 29.2, isPeak: true },
    { label: 'Sat', currentKwh: 29.1, previousKwh: 31.4, isPeak: true },
    { label: 'Sun', currentKwh: 25.3, previousKwh: 27.0 },
  ],
  '30 Days': [
    { label: 'Week 1', currentKwh: 175.4, previousKwh: 189.0 },
    { label: 'Week 2', currentKwh: 168.2, previousKwh: 182.5 },
    { label: 'Week 3', currentKwh: 182.6, previousKwh: 194.0, isPeak: true },
    { label: 'Week 4', currentKwh: 162.8, previousKwh: 178.2 },
  ],
  '12 Months': [
    { label: 'Jan', currentKwh: 680, previousKwh: 720 },
    { label: 'Feb', currentKwh: 640, previousKwh: 690 },
    { label: 'Mar', currentKwh: 710, previousKwh: 760 },
    { label: 'Apr', currentKwh: 790, previousKwh: 840, isPeak: true },
    { label: 'May', currentKwh: 890, previousKwh: 950, isPeak: true },
    { label: 'Jun', currentKwh: 850, previousKwh: 910 },
    { label: 'Jul', currentKwh: 820, previousKwh: 870 },
    { label: 'Aug', currentKwh: 800, previousKwh: 850 },
    { label: 'Sep', currentKwh: 730, previousKwh: 780 },
    { label: 'Oct', currentKwh: 690, previousKwh: 740 },
    { label: 'Nov', currentKwh: 660, previousKwh: 710 },
    { label: 'Dec', currentKwh: 670, previousKwh: 715 },
  ]
};
