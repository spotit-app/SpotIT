import { ComponentPropsWithoutRef } from 'react';
import { twMerge } from 'tailwind-merge';

function Button({ className, ...props }: ComponentPropsWithoutRef<'button'>) {
  return (
    <button {...props} className={twMerge('btn btn-sm btn-primary font-bold my-2', className)} />
  );
}

export { Button };
