import React from 'react';
import { NavTab } from '../../types';
import {
  LayoutDashboard,
  BarChart3,
  Cpu,
  Lightbulb,
  Sun
} from 'lucide-react';

interface MobileNavProps {
  currentTab: NavTab;
  onSelectTab: (tab: NavTab) => void;
}

export const MobileNavigation: React.FC<MobileNavProps> = ({
  currentTab,
  onSelectTab
}) => {
  const items = [
    { id: 'overview' as NavTab, label: 'Overview', icon: <LayoutDashboard className="w-5 h-5" /> },
    { id: 'analytics' as NavTab, label: 'Analytics', icon: <BarChart3 className="w-5 h-5" /> },
    { id: 'devices' as NavTab, label: 'Devices', icon: <Cpu className="w-5 h-5" /> },
    { id: 'recommendations' as NavTab, label: 'Advice', icon: <Lightbulb className="w-5 h-5" /> },
    { id: 'solar' as NavTab, label: 'Solar', icon: <Sun className="w-5 h-5" /> },
  ];

  return (
    <nav className="lg:hidden fixed bottom-0 left-0 right-0 z-40 bg-[#0E1D30]/95 backdrop-blur-lg border-t border-[#1D3850] px-2 py-2">
      <div className="flex items-center justify-around">
        {items.map((item) => {
          const isActive = currentTab === item.id;
          return (
            <button
              key={item.id}
              onClick={() => onSelectTab(item.id)}
              className={`flex flex-col items-center gap-1 p-2 rounded-xl transition-colors ${
                isActive ? 'text-[#57E1DC]' : 'text-[#8DA5BA] hover:text-[#EDF7FF]'
              }`}
            >
              {item.icon}
              <span className="text-[10px] font-medium">{item.label}</span>
            </button>
          );
        })}
      </div>
    </nav>
  );
};
