import React from 'react';
import { ToastState } from '../../hooks/useToast';
import { CheckCircle2, AlertTriangle, Info, AlertCircle, X } from 'lucide-react';

interface ToastProps {
  toasts: ToastState[];
  onRemove: (id: string) => void;
}

export const ToastContainer: React.FC<ToastProps> = ({ toasts, onRemove }) => {
  if (toasts.length === 0) return null;

  return (
    <div className="fixed bottom-5 right-5 z-50 flex flex-col gap-2 pointer-events-none max-w-sm w-full">
      {toasts.map((toast) => {
        const icons = {
          success: <CheckCircle2 className="w-5 h-5 text-[#B8ED68]" />,
          warning: <AlertTriangle className="w-5 h-5 text-[#FFB866]" />,
          info: <Info className="w-5 h-5 text-[#57E1DC]" />,
          error: <AlertCircle className="w-5 h-5 text-red-400" />
        };

        const borderColors = {
          success: 'border-[#B8ED68]/40',
          warning: 'border-[#FFB866]/40',
          info: 'border-[#57E1DC]/40',
          error: 'border-red-400/40'
        };

        return (
          <div
            key={toast.id}
            className={`pointer-events-auto flex items-center justify-between p-3.5 bg-[#0E1D30] border ${borderColors[toast.type || 'success']} rounded-xl shadow-xl transition-all animate-slide-up`}
          >
            <div className="flex items-center gap-3">
              {icons[toast.type || 'success']}
              <p className="text-xs md:text-sm font-medium text-[#EDF7FF]">{toast.message}</p>
            </div>
            <button
              onClick={() => onRemove(toast.id)}
              className="p-1 text-[#8DA5BA] hover:text-[#EDF7FF] rounded-md transition-colors"
            >
              <X className="w-4 h-4" />
            </button>
          </div>
        );
      })}
    </div>
  );
};
