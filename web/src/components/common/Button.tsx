import React from 'react';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'outline' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  icon?: React.ReactNode;
}

export const Button: React.FC<ButtonProps> = ({
  children,
  variant = 'primary',
  size = 'md',
  icon,
  className = '',
  ...props
}) => {
  const baseStyles = "inline-flex items-center justify-center font-semibold rounded-lg transition-all focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed";

  const sizeStyles = {
    sm: "px-3 py-1.5 text-xs gap-1.5",
    md: "px-4 py-2 text-sm gap-2",
    lg: "px-5 py-2.5 text-base gap-2.5"
  };

  const variantStyles = {
    primary: "bg-[#57E1DC] text-[#07111F] hover:bg-[#48cac6] active:bg-[#3bbab6] focus:ring-[#57E1DC]",
    secondary: "bg-[#132942] text-[#EDF7FF] hover:bg-[#1c3858] active:bg-[#0E1D30] border border-[#1D3850]",
    outline: "bg-transparent text-[#57E1DC] border border-[#57E1DC]/40 hover:bg-[#57E1DC]/10",
    danger: "bg-red-500/20 text-red-300 border border-red-500/40 hover:bg-red-500/30"
  };

  return (
    <button
      className={`${baseStyles} ${sizeStyles[size]} ${variantStyles[variant]} ${className}`}
      {...props}
    >
      {icon && <span className="flex-shrink-0">{icon}</span>}
      {children}
    </button>
  );
};
