import React from 'react';
import { Recommendation } from '../../types';
import { Button } from '../common/Button';
import { PiggyBank, Check, X } from 'lucide-react';

interface RecommendationCardProps {
  recommendation: Recommendation;
  onApply: (rec: Recommendation) => void;
  onComplete: (id: string) => void;
  onDismiss: (id: string) => void;
}

export const RecommendationCard: React.FC<RecommendationCardProps> = ({
  recommendation,
  onApply,
  onComplete,
  onDismiss
}) => {
  const priorityConfig = {
    HIGH: {
      label: 'HIGH PRIORITY',
      border: 'border-[#FFB866]/40',
      badge: 'bg-[#FFB866]/15 text-[#FFB866] border-[#FFB866]/30'
    },
    MEDIUM: {
      label: 'MEDIUM PRIORITY',
      border: 'border-[#57E1DC]/40',
      badge: 'bg-[#57E1DC]/15 text-[#57E1DC] border-[#57E1DC]/30'
    },
    LOW: {
      label: 'LOW PRIORITY',
      border: 'border-[#B8ED68]/40',
      badge: 'bg-[#B8ED68]/15 text-[#B8ED68] border-[#B8ED68]/30'
    }
  };

  const currentPriority = priorityConfig[recommendation.priority];

  return (
    <div
      className={`p-5 bg-[#0E1D30] border ${
        recommendation.isCompleted
          ? 'border-[#B8ED68]/40 opacity-75'
          : 'border-[#1D3850]'
      } rounded-2xl flex flex-col justify-between card-hover`}
    >
      <div>
        <div className="flex items-center justify-between mb-3">
          <span
            className={`px-2.5 py-0.5 rounded-md text-[10px] font-bold border ${currentPriority.badge}`}
          >
            {currentPriority.label}
          </span>

          <div className="flex items-center gap-1">
            {recommendation.isCompleted ? (
              <span className="flex items-center gap-1 text-xs font-bold text-[#B8ED68]">
                <Check className="w-3.5 h-3.5" />
                Completed
              </span>
            ) : (
              <button
                onClick={() => onDismiss(recommendation.id)}
                className="p-1 text-[#8DA5BA] hover:text-[#EDF7FF] rounded-lg hover:bg-[#132942] transition-colors"
                title="Dismiss"
              >
                <X className="w-4 h-4" />
              </button>
            )}
          </div>
        </div>

        <h4 className="text-base font-bold text-[#EDF7FF] mb-1.5">
          {recommendation.title}
        </h4>

        <p className="text-xs text-[#8DA5BA] leading-relaxed mb-4">
          {recommendation.explanation}
        </p>

        {/* Savings Pill */}
        <div className="flex items-center gap-2 p-2.5 bg-[#132942] rounded-xl border border-[#1D3850] text-xs mb-4">
          <PiggyBank className="w-4 h-4 text-[#B8ED68]" />
          <span className="text-[#8DA5BA]">Potential Savings:</span>
          <span className="font-bold text-[#B8ED68]">{recommendation.potentialSavings}</span>
        </div>
      </div>

      {!recommendation.isCompleted && (
        <div className="flex items-center justify-end gap-2 pt-2 border-t border-[#1D3850]/50">
          <Button
            variant="secondary"
            size="sm"
            onClick={() => onComplete(recommendation.id)}
          >
            Mark Done
          </Button>
          <Button
            variant="primary"
            size="sm"
            onClick={() => onApply(recommendation)}
          >
            {recommendation.actionLabel}
          </Button>
        </div>
      )}
    </div>
  );
};
