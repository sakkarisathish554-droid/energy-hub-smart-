import React, { useState } from 'react';
import { AppSettings, Device } from '../types';
import { Button } from '../components/common/Button';
import { User, Bell, Sliders, Database, Download, RotateCcw, Check } from 'lucide-react';

interface SettingsProps {
  settings: AppSettings;
  devices: Device[];
  onSaveSettings: (settings: AppSettings) => void;
  onResetDemoData: () => void;
  onShowToast: (msg: string) => void;
}

export const Settings: React.FC<SettingsProps> = ({
  settings,
  devices,
  onSaveSettings,
  onResetDemoData,
  onShowToast
}) => {
  const [form, setForm] = useState<AppSettings>(settings);

  const handleChange = <K extends keyof AppSettings>(key: K, val: AppSettings[K]) => {
    setForm((prev) => ({ ...prev, [key]: val }));
  };

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    onSaveSettings(form);
    onShowToast('Settings saved successfully');
  };

  const handleExportCSV = () => {
    const header = "Device Name,Location,Category,Power (kW),Today Energy (kWh),Cost (INR)\n";
    const rows = devices.map(d => `"${d.name}","${d.location}","${d.category}",${d.powerKw},${d.todayKwh},${d.estimatedCost}`).join("\n");
    const blob = new Blob([header + rows], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", "EnergyHub_Full_Export.csv");
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    onShowToast("Full system data exported");
  };

  return (
    <form onSubmit={handleSave} className="space-y-6 animate-fade-in max-w-4xl">
      {/* 1. Profile Section */}
      <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl space-y-4">
        <div className="flex items-center gap-2 pb-3 border-b border-[#1D3850]">
          <User className="w-5 h-5 text-[#57E1DC]" />
          <div>
            <h3 className="text-base font-bold text-[#EDF7FF]">User Profile</h3>
            <p className="text-xs text-[#8DA5BA]">Manage identity and primary account credentials</p>
          </div>
        </div>

        <div className="flex items-center gap-4">
          <div className="w-14 h-14 rounded-2xl bg-[#57E1DC]/20 border border-[#57E1DC] flex items-center justify-center font-bold text-xl text-[#57E1DC]">
            SS
          </div>
          <div>
            <h4 className="text-sm font-bold text-[#EDF7FF]">{form.userName}</h4>
            <p className="text-xs text-[#8DA5BA]">{form.userEmail}</p>
            <span className="inline-block mt-1 px-2 py-0.5 rounded text-[10px] font-bold bg-[#B8ED68]/20 text-[#B8ED68]">
              Pro Energy Subscriber
            </span>
          </div>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 pt-2">
          <div>
            <label className="block text-xs font-semibold text-[#8DA5BA] mb-1.5">
              Full Name
            </label>
            <input
              type="text"
              value={form.userName}
              onChange={(e) => handleChange('userName', e.target.value)}
              className="w-full px-3.5 py-2.5 bg-[#132942] border border-[#1D3850] rounded-xl text-sm text-[#EDF7FF] focus:outline-none focus:border-[#57E1DC]"
            />
          </div>
          <div>
            <label className="block text-xs font-semibold text-[#8DA5BA] mb-1.5">
              Email Address
            </label>
            <input
              type="email"
              value={form.userEmail}
              onChange={(e) => handleChange('userEmail', e.target.value)}
              className="w-full px-3.5 py-2.5 bg-[#132942] border border-[#1D3850] rounded-xl text-sm text-[#EDF7FF] focus:outline-none focus:border-[#57E1DC]"
            />
          </div>
        </div>
      </div>

      {/* 2. Tariffs and Targets */}
      <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl space-y-4">
        <div className="flex items-center gap-2 pb-3 border-b border-[#1D3850]">
          <Sliders className="w-5 h-5 text-[#FFB866]" />
          <div>
            <h3 className="text-base font-bold text-[#EDF7FF]">Tariff & Consumption Targets</h3>
            <p className="text-xs text-[#8DA5BA]">Used to compute real-time cost and budget alert notifications</p>
          </div>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <div>
            <label className="block text-xs font-semibold text-[#8DA5BA] mb-1.5">
              Electricity Tariff (₹ / kWh)
            </label>
            <input
              type="number"
              step="0.1"
              value={form.electricityRate}
              onChange={(e) => handleChange('electricityRate', parseFloat(e.target.value) || 0)}
              className="w-full px-3.5 py-2.5 bg-[#132942] border border-[#1D3850] rounded-xl text-sm text-[#EDF7FF] focus:outline-none focus:border-[#57E1DC]"
            />
          </div>
          <div>
            <label className="block text-xs font-semibold text-[#8DA5BA] mb-1.5">
              Daily Target (kWh)
            </label>
            <input
              type="number"
              step="1"
              value={form.dailyTargetKwh}
              onChange={(e) => handleChange('dailyTargetKwh', parseFloat(e.target.value) || 0)}
              className="w-full px-3.5 py-2.5 bg-[#132942] border border-[#1D3850] rounded-xl text-sm text-[#EDF7FF] focus:outline-none focus:border-[#57E1DC]"
            />
          </div>
          <div>
            <label className="block text-xs font-semibold text-[#8DA5BA] mb-1.5">
              Monthly Budget (kWh)
            </label>
            <input
              type="number"
              step="10"
              value={form.monthlyTargetKwh}
              onChange={(e) => handleChange('monthlyTargetKwh', parseFloat(e.target.value) || 0)}
              className="w-full px-3.5 py-2.5 bg-[#132942] border border-[#1D3850] rounded-xl text-sm text-[#EDF7FF] focus:outline-none focus:border-[#57E1DC]"
            />
          </div>
        </div>
      </div>

      {/* 3. Notifications Preferences */}
      <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl space-y-4">
        <div className="flex items-center gap-2 pb-3 border-b border-[#1D3850]">
          <Bell className="w-5 h-5 text-[#B8ED68]" />
          <div>
            <h3 className="text-base font-bold text-[#EDF7FF]">Notification Preferences</h3>
            <p className="text-xs text-[#8DA5BA]">Select which real-time event triggers deliver desktop & push alerts</p>
          </div>
        </div>

        <div className="space-y-3">
          {[
            {
              key: 'alertHighConsumption' as const,
              label: 'High Consumption Alerts',
              desc: 'Warn when instantaneous whole-home load exceeds 3.0 kW'
            },
            {
              key: 'alertDeviceOffline' as const,
              label: 'Device Offline Alerts',
              desc: 'Notify when monitored smart plugs drop Wi-Fi connectivity'
            },
            {
              key: 'alertDailySummary' as const,
              label: 'Daily Summary Digest',
              desc: 'Receive end-of-day cost breakdown and solar coverage metrics'
            },
            {
              key: 'alertRecommendations' as const,
              label: 'AI Recommendation Alerts',
              desc: 'Receive immediate alerts when high-impact energy savings are detected'
            }
          ].map((item) => (
            <div
              key={item.key}
              className="flex items-center justify-between p-3 bg-[#132942]/40 border border-[#1D3850] rounded-xl"
            >
              <div>
                <h4 className="text-xs font-bold text-[#EDF7FF]">{item.label}</h4>
                <p className="text-[11px] text-[#8DA5BA]">{item.desc}</p>
              </div>

              <button
                type="button"
                onClick={() => handleChange(item.key, !form[item.key])}
                className={`relative inline-flex h-5 w-10 flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent transition-colors duration-200 ease-in-out ${
                  form[item.key] ? 'bg-[#57E1DC]' : 'bg-[#132942]'
                }`}
              >
                <span
                  className={`pointer-events-none inline-block h-4 w-4 transform rounded-full bg-[#07111F] shadow ring-0 transition duration-200 ease-in-out ${
                    form[item.key] ? 'translate-x-5' : 'translate-x-0'
                  }`}
                />
              </button>
            </div>
          ))}
        </div>
      </div>

      {/* 4. Data Management */}
      <div className="p-5 bg-[#0E1D30] border border-[#1D3850] rounded-2xl space-y-4">
        <div className="flex items-center gap-2 pb-3 border-b border-[#1D3850]">
          <Database className="w-5 h-5 text-[#8DA5BA]" />
          <div>
            <h3 className="text-base font-bold text-[#EDF7FF]">Data Management</h3>
            <p className="text-xs text-[#8DA5BA]">Export records or reset sample demonstration state</p>
          </div>
        </div>

        <div className="flex flex-wrap items-center gap-3">
          <Button
            type="button"
            variant="outline"
            size="sm"
            icon={<Download className="w-4 h-4" />}
            onClick={handleExportCSV}
          >
            Export All Data (CSV)
          </Button>

          <Button
            type="button"
            variant="danger"
            size="sm"
            icon={<RotateCcw className="w-4 h-4" />}
            onClick={() => {
              if (window.confirm("Reset all settings and devices to default demo state?")) {
                onResetDemoData();
              }
            }}
          >
            Reset Demo Data
          </Button>
        </div>
      </div>

      {/* Submit Button */}
      <div className="flex items-center justify-end gap-3 pt-2">
        <Button
          type="submit"
          variant="primary"
          size="md"
          icon={<Check className="w-4 h-4" />}
        >
          Save Changes
        </Button>
      </div>
    </form>
  );
};
