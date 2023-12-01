import { PropsWithChildren } from 'react';
import PropTypes from 'prop-types';

interface ButtonProps extends PropsWithChildren {
  type: 'button' | 'reset' | 'submit';
}

function Button({ children, type }: ButtonProps) {
  return (
    <div className="sm:col-span-4">
      <div className="flex items-center justify-end">
        <button
          type={type}
          className="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
        >
          {children}
        </button>
      </div>
    </div>
  );
}

Button.propTypes = {
  type: PropTypes.string.isRequired,
  children: PropTypes.node.isRequired
};

export { Button };
