import React, { useState, useEffect, useCallback } from 'react';
import { NavTab, Device, Recommendation, NotificationItem, SolarMetrics, AppSettings, DeviceCategory } from './types';
import { api } from './services/api';
import { useToast } from './hooks/useToast';
import { Sidebar } from './components/layout/Sidebar';
import { Topbar } from './components/layout/Topbar';
import { MobileNavigation } from './components/layout/MobileNavigation';
import { ToastContainer } from './components/common/Toast';
import { Modal } from './components/common/Modal';
import { Search } from './components/common/Search';
import { Overview } from './pages/Overview';
import { Analytics } from './pages/Analytics';
import { Devices } from './pages/Devices';
import { Recommendations } from './pages/Recommendations';
import { Solar } from './pages/Solar';
import { Settings } from './pages/Settings';
import { Cpu, Lightbulb, ArrowRight } from 'lucide-react';

export const App: React.FC = () => {
  const [currentTab, setCurrentTab] = useState<NavTab>('overview');
  const [devices, setDevices] = useState<Device[]>([]);
  const [recommendations, setRecommendations] = useState<Recommendation[]>([]);
  const [notifications, setNotifications] = useState<NotificationItem[]>([]);
  const [solarMetrics, setSolarMetrics] = useState<SolarMetrics | null>(null);
  const [settings, setSettings] = useState<AppSettings | null>(null);
  const [isSearchOpen, setIsSearchOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const { toasts, showToast, removeToast } = useToast();

  const loadInitialData = useCallback(async () => {
    try {
      const [devs, recs, notifs, solar, sett] = await Promise.all([
        api.getDevices(),
        api.getRecommendations(),
        api.getNotifications(),
        api.getSolarMetrics(),
        api.getSettings()
      ]);
      setDevices(devs);
      setRecommendations(recs);
      setNotifications(notifs);
      setSolarMetrics(solar);
      setSettings(sett);
    } catch (e) {
      console.error('Error loading initial energy data', e);
    }
  }, []);

  useEffect(() => {
    loadInitialData();
  }, [loadInitialData]);

  // Global Keyboard Shortcuts (Cmd+K / Ctrl+K for search)
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
        e.preventDefault();
        setIsSearchOpen((prev) => !prev);
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, []);

  const handleToggleDevice = async (id: string) => {
    const updated = await api.toggleDevice(id);
    setDevices(updated);
    const changed = updated.find((d) => d.id === id);
    if (changed) {
      showToast(`${changed.name} turned ${changed.isOn ? 'ON' : 'OFF'}`, changed.isOn ? 'success' : 'info');
    }
  };

  const handleAddDevice = async (
    name: string,
    location: string,
    category: DeviceCategory,
    powerKw: number
  ) => {
    const updated = await api.addDevice(name, location, category, powerKw);
    setDevices(updated);
    showToast(`Added ${name} (${powerKw} kW)`, 'success');
  };

  const handleDeleteDevice = async (id: string) => {
    const target = devices.find((d) => d.id === id);
    const updated = await api.deleteDevice(id);
    setDevices(updated);
    showToast(`Deleted ${target?.name || 'device'}`, 'warning');
  };

  const handleApplyRecommendation = (rec: Recommendation) => {
    if (rec.deviceId) {
      handleToggleDevice(rec.deviceId);
    }
    handleCompleteRecommendation(rec.id);
    showToast(`Applied: ${rec.actionLabel}`, 'success');
  };

  const handleCompleteRecommendation = async (id: string) => {
    const updated = recommendations.map((r) =>
      r.id === id ? { ...r, isCompleted: true } : r
    );
    setRecommendations(updated);
    await api.updateRecommendations(updated);
    showToast('Recommendation marked as completed', 'success');
  };

  const handleDismissRecommendation = async (id: string) => {
    const updated = recommendations.map((r) =>
      r.id === id ? { ...r, isDismissed: true } : r
    );
    setRecommendations(updated);
    await api.updateRecommendations(updated);
    showToast('Recommendation dismissed', 'info');
  };

  const handleResetRecommendations = async () => {
    const fresh = await api.getRecommendations();
    const reset = fresh.map((r) => ({ ...r, isCompleted: false, isDismissed: false }));
    setRecommendations(reset);
    await api.updateRecommendations(reset);
    showToast('Recommendations reloaded', 'info');
  };

  const handleMarkNotificationRead = async (id: string) => {
    const updated = notifications.map((n) => (n.id === id ? { ...n, isRead: true } : n));
    setNotifications(updated);
    await api.updateNotifications(updated);
  };

  const handleMarkAllNotificationsRead = async () => {
    const updated = notifications.map((n) => ({ ...n, isRead: true }));
    setNotifications(updated);
    await api.updateNotifications(updated);
    showToast('All notifications marked as read', 'info');
  };

  const handleSaveSettings = async (newSettings: AppSettings) => {
    setSettings(newSettings);
    await api.saveSettings(newSettings);
  };

  const handleResetDemoData = async () => {
    await api.resetAllData();
    await loadInitialData();
    showToast('System data reset to factory demo values', 'warning');
  };

  // Search Results
  const searchResultsDevices = searchQuery.trim()
    ? devices.filter(
        (d) =>
          d.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
          d.category.toLowerCase().includes(searchQuery.toLowerCase()) ||
          d.location.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : [];

  const searchResultsRecs = searchQuery.trim()
    ? recommendations.filter(
        (r) =>
          !r.isDismissed &&
          (r.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
            r.explanation.toLowerCase().includes(searchQuery.toLowerCase()))
      )
    : [];

  return (
    <div className="flex min-h-screen bg-[#07111F] text-[#EDF7FF]">
      {/* Sidebar for Desktop */}
      <Sidebar
        currentTab={currentTab}
        onSelectTab={(tab) => {
          setCurrentTab(tab);
          setIsMobileMenuOpen(false);
        }}
        unreadCount={recommendations.filter((r) => !r.isCompleted && !r.isDismissed).length}
      />

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col min-w-0 pb-20 lg:pb-8">
        <Topbar
          currentTab={currentTab}
          notifications={notifications}
          onMarkNotificationRead={handleMarkNotificationRead}
          onMarkAllRead={handleMarkAllNotificationsRead}
          onOpenSearch={() => setIsSearchOpen(true)}
          onToggleMobileMenu={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
          isMobileMenuOpen={isMobileMenuOpen}
        />

        {/* Mobile Slide-over Menu */}
        {isMobileMenuOpen && (
          <div className="lg:hidden p-4 bg-[#0E1D30] border-b border-[#1D3850] space-y-2 animate-slide-down">
            {[
              { id: 'overview' as NavTab, label: 'Overview' },
              { id: 'analytics' as NavTab, label: 'Analytics' },
              { id: 'devices' as NavTab, label: 'Smart Devices' },
              { id: 'recommendations' as NavTab, label: 'Recommendations' },
              { id: 'solar' as NavTab, label: 'Solar Energy' },
              { id: 'settings' as NavTab, label: 'Settings' },
            ].map((tab) => (
              <button
                key={tab.id}
                onClick={() => {
                  setCurrentTab(tab.id);
                  setIsMobileMenuOpen(false);
                }}
                className={`w-full text-left px-4 py-2.5 rounded-xl text-sm font-medium ${
                  currentTab === tab.id
                    ? 'bg-[#132942] text-[#57E1DC] border border-[#57E1DC]/30'
                    : 'text-[#8DA5BA] hover:text-[#EDF7FF]'
                }`}
              >
                {tab.label}
              </button>
            ))}
          </div>
        )}

        <main className="flex-1 px-4 lg:px-8 py-6 max-w-7xl mx-auto w-full">
          {currentTab === 'overview' && solarMetrics && (
            <Overview
              devices={devices}
              solarMetrics={solarMetrics}
              onToggleDevice={handleToggleDevice}
              onNavigate={setCurrentTab}
            />
          )}

          {currentTab === 'analytics' && (
            <Analytics
              devices={devices}
              onShowToast={(msg) => showToast(msg, 'success')}
            />
          )}

          {currentTab === 'devices' && (
            <Devices
              devices={devices}
              onToggleDevice={handleToggleDevice}
              onAddDevice={handleAddDevice}
              onDeleteDevice={handleDeleteDevice}
            />
          )}

          {currentTab === 'recommendations' && (
            <Recommendations
              recommendations={recommendations}
              onApplyRecommendation={handleApplyRecommendation}
              onCompleteRecommendation={handleCompleteRecommendation}
              onDismissRecommendation={handleDismissRecommendation}
              onResetRecommendations={handleResetRecommendations}
            />
          )}

          {currentTab === 'solar' && solarMetrics && (
            <Solar metrics={solarMetrics} />
          )}

          {currentTab === 'settings' && settings && (
            <Settings
              settings={settings}
              devices={devices}
              onSaveSettings={handleSaveSettings}
              onResetDemoData={handleResetDemoData}
              onShowToast={(msg) => showToast(msg, 'success')}
            />
          )}
        </main>
      </div>

      {/* Mobile Bottom Navigation */}
      <MobileNavigation
        currentTab={currentTab}
        onSelectTab={setCurrentTab}
      />

      {/* Global Search Modal */}
      <Modal
        isOpen={isSearchOpen}
        onClose={() => {
          setIsSearchOpen(false);
          setSearchQuery('');
        }}
        title="EnergyHub Quick Search"
        subtitle="Press Cmd+K or Ctrl+K to open anytime"
      >
        <div className="space-y-4">
          <Search
            value={searchQuery}
            onChange={setSearchQuery}
            placeholder="Search devices, recommendations, locations..."
          />

          {searchQuery && (
            <div className="space-y-4 max-h-72 overflow-y-auto">
              {/* Devices matching */}
              {searchResultsDevices.length > 0 && (
                <div>
                  <h4 className="text-[11px] font-bold text-[#57E1DC] uppercase mb-2">
                    Devices ({searchResultsDevices.length})
                  </h4>
                  <div className="space-y-1.5">
                    {searchResultsDevices.map((d) => (
                      <div
                        key={d.id}
                        onClick={() => {
                          setCurrentTab('devices');
                          setIsSearchOpen(false);
                        }}
                        className="flex items-center justify-between p-2.5 bg-[#132942]/60 hover:bg-[#132942] rounded-xl cursor-pointer transition-colors"
                      >
                        <div className="flex items-center gap-2.5">
                          <Cpu className="w-4 h-4 text-[#57E1DC]" />
                          <div>
                            <p className="text-xs font-semibold text-[#EDF7FF]">{d.name}</p>
                            <p className="text-[10px] text-[#8DA5BA]">{d.location} • {d.powerKw} kW</p>
                          </div>
                        </div>
                        <ArrowRight className="w-3.5 h-3.5 text-[#8DA5BA]" />
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* Recommendations matching */}
              {searchResultsRecs.length > 0 && (
                <div>
                  <h4 className="text-[11px] font-bold text-[#FFB866] uppercase mb-2">
                    Recommendations ({searchResultsRecs.length})
                  </h4>
                  <div className="space-y-1.5">
                    {searchResultsRecs.map((r) => (
                      <div
                        key={r.id}
                        onClick={() => {
                          setCurrentTab('recommendations');
                          setIsSearchOpen(false);
                        }}
                        className="flex items-center justify-between p-2.5 bg-[#132942]/60 hover:bg-[#132942] rounded-xl cursor-pointer transition-colors"
                      >
                        <div className="flex items-center gap-2.5">
                          <Lightbulb className="w-4 h-4 text-[#FFB866]" />
                          <div>
                            <p className="text-xs font-semibold text-[#EDF7FF]">{r.title}</p>
                            <p className="text-[10px] text-[#B8ED68]">{r.potentialSavings}</p>
                          </div>
                        </div>
                        <ArrowRight className="w-3.5 h-3.5 text-[#8DA5BA]" />
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {searchResultsDevices.length === 0 && searchResultsRecs.length === 0 && (
                <p className="text-xs text-center py-6 text-[#8DA5BA]">
                  No items match "{searchQuery}"
                </p>
              )}
            </div>
          )}

          {!searchQuery && (
            <div className="text-center py-6 text-xs text-[#8DA5BA]">
              Type a keyword like "AC", "Kitchen", or "Solar" to jump directly to any asset.
            </div>
          )}
        </div>
      </Modal>

      {/* Floating Toast Notification Container */}
      <ToastContainer toasts={toasts} onRemove={removeToast} />
    </div>
  );
};
