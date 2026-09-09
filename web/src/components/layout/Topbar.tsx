import React, { useState } from 'react';
import { NavTab, NotificationItem } from '../../types';
import { Bell, Search as SearchIcon, CheckCheck, Menu, X } from 'lucide-react';

interface TopbarProps {
  currentTab: NavTab;
  notifications: NotificationItem[];
  onMarkNotificationRead: (id: string) => void;
  onMarkAllRead: () => void;
  onOpenSearch: () => void;
  onToggleMobileMenu: () => void;
  isMobileMenuOpen: boolean;
}

export const Topbar: React.FC<TopbarProps> = ({
  currentTab,
  notifications,
  onMarkNotificationRead,
  onMarkAllRead,
  onOpenSearch,
  onToggleMobileMenu,
  isMobileMenuOpen
}) => {
  const [showNotifications, setShowNotifications] = useState(false);

  const titles: Record<NavTab, { title: string; subtitle: string }> = {
    overview: {
      title: 'Energy Overview',
      subtitle: "Monitor your home's energy performance in real time."
    },
    analytics: {
      title: 'Analytics & Consumption',
      subtitle: 'Historical trends, cost projections, and appliance comparisons.'
    },
    devices: {
      title: 'Smart Devices',
      subtitle: 'Control connected appliances and review live wattage.'
    },
    recommendations: {
      title: 'Energy Recommendations',
      subtitle: 'Simple actions that can improve your energy efficiency.'
    },
    solar: {
      title: 'Solar Intelligence',
      subtitle: 'Track solar generation, self-sufficiency, and real-time flow.'
    },
    settings: {
      title: 'Preferences & System',
      subtitle: 'Manage electricity tariff, profile, and notification targets.'
    }
  };

  const currentDate = new Date().toLocaleDateString('en-US', {
    weekday: 'long',
    month: 'short',
    day: 'numeric',
    year: 'numeric'
  });

  const unreadCount = notifications.filter((n) => !n.isRead).length;

  return (
    <header className="sticky top-0 z-30 flex items-center justify-between px-4 lg:px-8 py-4 bg-[#0E1D30]/90 backdrop-blur-md border-b border-[#1D3850]">
      <div className="flex items-center gap-3">
        <button
          onClick={onToggleMobileMenu}
          className="p-2 lg:hidden rounded-lg text-[#8DA5BA] hover:text-[#EDF7FF] hover:bg-[#132942]"
        >
          {isMobileMenuOpen ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
        </button>

        <div>
          <h2 className="text-lg lg:text-xl font-bold text-[#EDF7FF]">
            {titles[currentTab].title}
          </h2>
          <p className="text-xs text-[#8DA5BA] hidden sm:block">
            {titles[currentTab].subtitle}
          </p>
        </div>
      </div>

      <div className="flex items-center gap-3">
        <div className="hidden md:block text-right pr-2">
          <p className="text-xs font-semibold text-[#EDF7FF]">{currentDate}</p>
          <p className="text-[11px] text-[#8DA5BA]">Status: Optimal</p>
        </div>

        {/* Search button */}
        <button
          onClick={onOpenSearch}
          className="p-2 rounded-xl bg-[#132942] border border-[#1D3850] text-[#8DA5BA] hover:text-[#57E1DC] hover:border-[#57E1DC]/40 transition-colors"
          title="Quick Search"
        >
          <SearchIcon className="w-4 h-4" />
        </button>

        {/* Notifications */}
        <div className="relative">
          <button
            onClick={() => setShowNotifications(!showNotifications)}
            className="relative p-2 rounded-xl bg-[#132942] border border-[#1D3850] text-[#8DA5BA] hover:text-[#EDF7FF] hover:border-[#57E1DC]/40 transition-colors"
          >
            <Bell className="w-4 h-4" />
            {unreadCount > 0 && (
              <span className="absolute top-1.5 right-1.5 w-2 h-2 rounded-full bg-[#FFB866] ring-2 ring-[#0E1D30]" />
            )}
          </button>

          {showNotifications && (
            <div className="absolute right-0 mt-2 w-80 sm:w-96 bg-[#0E1D30] border border-[#1D3850] rounded-2xl shadow-2xl p-4 z-50 animate-scale-up">
              <div className="flex items-center justify-between pb-3 border-b border-[#1D3850]">
                <div className="flex items-center gap-2">
                  <span className="text-sm font-bold text-[#EDF7FF]">Notifications</span>
                  {unreadCount > 0 && (
                    <span className="px-2 py-0.5 text-[10px] font-bold rounded-full bg-[#FFB866]/20 text-[#FFB866]">
                      {unreadCount} new
                    </span>
                  )}
                </div>
                {unreadCount > 0 && (
                  <button
                    onClick={onMarkAllRead}
                    className="flex items-center gap-1 text-xs text-[#57E1DC] hover:underline"
                  >
                    <CheckCheck className="w-3.5 h-3.5" />
                    Mark all read
                  </button>
                )}
              </div>

              <div className="mt-3 space-y-2 max-h-72 overflow-y-auto">
                {notifications.length === 0 ? (
                  <p className="text-xs text-center py-6 text-[#8DA5BA]">No notifications</p>
                ) : (
                  notifications.map((n) => (
                    <div
                      key={n.id}
                      onClick={() => onMarkNotificationRead(n.id)}
                      className={`p-3 rounded-xl border text-xs cursor-pointer transition-colors ${
                        n.isRead
                          ? 'bg-[#132942]/30 border-[#1D3850] text-[#8DA5BA]'
                          : 'bg-[#132942] border-[#57E1DC]/30 text-[#EDF7FF]'
                      }`}
                    >
                      <div className="flex justify-between items-start mb-1">
                        <span className="font-semibold text-[13px]">{n.title}</span>
                        <span className="text-[10px] text-[#8DA5BA]">{n.timeAgo}</span>
                      </div>
                      <p className="text-[11px] text-[#8DA5BA] leading-relaxed">{n.message}</p>
                    </div>
                  ))
                )}
              </div>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};
