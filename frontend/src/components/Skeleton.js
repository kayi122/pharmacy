import React from 'react';

const Skeleton = ({ 
  variant = 'text',
  width = '100%',
  height,
  className = '',
  count = 1
}) => {
  const variants = {
    text: 'h-4 rounded',
    title: 'h-8 rounded',
    circle: 'rounded-full',
    rect: 'rounded-xl',
    card: 'h-48 rounded-2xl',
  };
  
  const skeletons = Array.from({ length: count }, (_, i) => (
    <div
      key={i}
      className={`bg-gradient-to-r from-slate-200 via-slate-300 to-slate-200 animate-pulse ${variants[variant]} ${className}`}
      style={{
        width: width,
        height: height || (variant === 'circle' ? width : undefined),
        backgroundSize: '200% 100%',
        animation: 'shimmer 1.5s infinite',
      }}
    />
  ));
  
  return count > 1 ? <div className="space-y-3">{skeletons}</div> : skeletons[0];
};

export default Skeleton;
