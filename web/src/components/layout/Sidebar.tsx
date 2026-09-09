import React from 'react';
import { NavTab } from '../../types';
import {
  Zap,
  LayoutDashboard,
  BarChart3,
  Cpu,
  Lightbulb,
  Sun,
  Settings
} from 'lucide-react';

interface SidebarProps {
  currentTab: NavTab;
  onSelectTab: (tab: NavTab) => void;
  unreadCount?: number;
}

export const Sidebar: React.FC<SidebarProps> = ({
  currentTab,
  onSelectTab,
  unreadCount = 0
}) => {
  const navItems = [
    { id: 'overview' as NavTab, label: 'Overview', icon: <LayoutDashboard className="w-5 h-5" /> },
    { id: 'analytics' as NavTab, label: 'Analytics', icon: <BarChart3 className="w-5 h-5" /> },
    { id: 'devices' as NavTab, label: 'Smart Devices', icon: <Cpu className="w-5 h-5" /> },
    { id: 'recommendations' as NavTab, label: 'Recommendations', icon: <Lightbulb className="w-5 h-5" /> },
    { id: 'solar' as NavTab, label: 'Solar Energy', icon: <Sun className="w-5 h-5" /> },
    { id: 'settings' as NavTab, label: 'Settings', icon: <Settings className="w-5 h-5" /> },
  ];

  return (
    <aside className="hidden lg:flex flex-col w-64 bg-[#0E1D30] border-r border-[#1D3850] min-h-screen p-5 justify-between">
      <div>
        {/* Brand */}
        <div className="flex items-center gap-3 px-2 py-3 mb-6">
          <div className="flex items-center justify-center w-10 h-10 rounded-xl bg-[#57E1DC]/15 border border-[#57E1DC]/40 text-[#57E1DC]">
            <Zap className="w-6 h-6 fill-current" />
          </div>
          <div>
            <h1 className="font-bold text-base tracking-wide text-[#EDF7FF]">EnergyHub</h1>
            <p className="text-[11px] font-medium text-[#57E1DC]">Smart Energy Intelligence</p>
          </div>
        </div>

        {/* Navigation Items */}
        <nav className="space-y-1.5">
          {navItems.map((item) => {
            const isActive = currentTab === item.id;
            return (
              <button
                key={item.id}
                onClick={() => onSelectTab(item.id)}
                className={`w-full flex items-center justify-between px-3.5 py-2.5 rounded-xl font-medium text-sm transition-all ${
                  isActive
                    ? 'bg-[#132942] text-[#57E1DC] border border-[#57E1DC]/30 shadow-sm'
                    : 'text-[#8DA5BA] hover:text-[#EDF7FF] hover:bg-[#132942]/50'
                }`}
              >
                <div className="flex items-center gap-3">
                  <span className={isActive ? 'text-[#57E1DC]' : 'text-[#8DA5BA]'}>
                    {item.icon}
                  </span>
                  <span>{item.label}</span>
                </div>
                {item.id === 'recommendations' && unreadCount > 0 && (
                  <span className="px-2 py-0.5 text-[10px] font-bold rounded-full bg-[#FFB866]/20 text-[#FFB866] border border-[#FFB866]/40">
                    Active
                  </span>
                )}
              </button>
            );
          })}
        </nav>
      </div>

      {/* User info at bottom */}
      <div className="pt-4 border-t border-[#1D3850]">
        <div 
          onClick={() => onSelectTab('settings')}
          className="flex items-center justify-between p-2 rounded-xl bg-[#132942]/60 hover:bg-[#132942] border border-[#1D3850] cursor-pointer transition-colors"
        >
          <div className="flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-[#57E1DC]/20 border border-[#57E1DC] flex items-center justify-center font-bold text-xs text-[#57E1DC]">
              SS
            </div>
            <div>
              <p className="text-xs font-semibold text-[#EDF7FF]">Sathish S.</p>
              <div className="flex items-center gap-1.5">
                <span className="w-1.5 h-1.5 rounded-full bg-[#B8ED68] animate-pulse" />
                <p className="text-[10px] text-[#B8ED68]">Live Simulation</p>
              </div>
            </div>
          </div>
          <Settings className="w-4 h-4 text-[#8DA5BA]" />
        </div>
      </div>
    </aside>
  );
};
