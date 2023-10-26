import { RouterProvider } from './RouterProvider';
import { AuthProvider } from './AuthProvider';
import PropTypes from 'prop-types';
import { PropsWithChildren } from 'react';

function Providers({ children }: PropsWithChildren) {
  return (
    <AuthProvider>
      <RouterProvider>{children}</RouterProvider>
    </AuthProvider>
  );
}

export { Providers };

Providers.propTypes = {
  children: PropTypes.node.isRequired
};
