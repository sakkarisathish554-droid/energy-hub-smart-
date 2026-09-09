import React, { useState } from 'react';
import { Recommendation, RecommendationPriority } from '../types';
import { RecommendationCard } from '../components/recommendations/RecommendationCard';
import { EmptyState } from '../components/common/EmptyState';
import { Lightbulb, CheckCircle2 } from 'lucide-react';

interface RecommendationsProps {
  recommendations: Recommendation[];
  onApplyRecommendation: (rec: Recommendation) => void;
  onCompleteRecommendation: (id: string) => void;
  onDismissRecommendation: (id: string) => void;
  onResetRecommendations: () => void;
}

export const Recommendations: React.FC<RecommendationsProps> = ({
  recommendations,
  onApplyRecommendation,
  onCompleteRecommendation,
  onDismissRecommendation,
  onResetRecommendations
}) => {
  const [priorityFilter, setPriorityFilter] = useState<'ALL' | RecommendationPriority>('ALL');

  const visibleRecommendations = recommendations
    .filter((r) => !r.isDismissed)
    .filter((r) => (priorityFilter === 'ALL' ? true : r.priority === priorityFilter));

  const totalPotentialSavingsKwh = 2.6; // 1.8 + 0.4 + 0.4

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Banner / Header */}
      <div className="p-6 bg-gradient-to-r from-[#0E1D30] to-[#132942] border border-[#1D3850] rounded-2xl flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2 mb-1">
            <Lightbulb className="w-5 h-5 text-[#57E1DC]" />
            <h2 className="text-xl font-bold text-[#EDF7FF]">Energy Optimization Insights</h2>
          </div>
          <p className="text-xs text-[#8DA5BA] max-w-xl leading-relaxed">
            AI-driven anomaly detection and tariff-aware scheduling to reduce your electricity footprint without compromising comfort.
          </p>
        </div>

        <div className="flex items-center gap-3 bg-[#07111F]/80 p-3.5 rounded-xl border border-[#1D3850]">
          <div>
            <span className="block text-[10px] text-[#8DA5BA] uppercase font-bold">Estimated Savings</span>
            <span className="text-base font-extrabold text-[#B8ED68]">Up to {totalPotentialSavingsKwh} kWh/day</span>
          </div>
          <div className="w-px h-8 bg-[#1D3850]" />
          <div>
            <span className="block text-[10px] text-[#8DA5BA] uppercase font-bold">Cost Impact</span>
            <span className="text-base font-extrabold text-[#57E1DC]">~₹21.00/day</span>
          </div>
        </div>
      </div>

      {/* Priority Filter Tabs */}
      <div className="flex items-center gap-2 overflow-x-auto pb-1">
        {(['ALL', 'HIGH', 'MEDIUM', 'LOW'] as const).map((p) => {
          const count = recommendations.filter(
            (r) => !r.isDismissed && (p === 'ALL' || r.priority === p)
          ).length;

          return (
            <button
              key={p}
              onClick={() => setPriorityFilter(p)}
              className={`flex items-center gap-2 px-3.5 py-1.5 rounded-xl text-xs font-semibold transition-all ${
                priorityFilter === p
                  ? 'bg-[#57E1DC] text-[#07111F]'
                  : 'bg-[#132942] text-[#8DA5BA] hover:text-[#EDF7FF] border border-[#1D3850]'
              }`}
            >
              <span>{p === 'ALL' ? 'All Priorities' : `${p} Priority`}</span>
              <span
                className={`px-1.5 py-0.2 rounded-full text-[10px] font-bold ${
                  priorityFilter === p ? 'bg-[#07111F]/20 text-[#07111F]' : 'bg-[#07111F] text-[#8DA5BA]'
                }`}
              >
                {count}
              </span>
            </button>
          );
        })}
      </div>

      {/* Cards or Empty State */}
      {visibleRecommendations.length === 0 ? (
        <EmptyState
          icon={<CheckCircle2 className="w-8 h-8 text-[#B8ED68]" />}
          title="All recommendations addressed!"
          description="Your household has applied or reviewed all active energy suggestions. Great job maintaining peak efficiency!"
          actionLabel="Reload Recommendations"
          onAction={onResetRecommendations}
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {visibleRecommendations.map((rec) => (
            <RecommendationCard
              key={rec.id}
              recommendation={rec}
              onApply={onApplyRecommendation}
              onComplete={onCompleteRecommendation}
              onDismiss={onDismissRecommendation}
            />
          ))}
        </div>
      )}
    </div>
  );
};
