import { PropsWithChildren } from 'react';
import PropTypes from 'prop-types';
import { RouterProvider, AuthProvider, QueryProvider, UserProvider } from '.';

function Providers({ children }: PropsWithChildren) {
  return (
    <AuthProvider>
      <RouterProvider>
        <QueryProvider>
          <UserProvider>{children}</UserProvider>
        </QueryProvider>
      </RouterProvider>
    </AuthProvider>
  );
}

export { Providers };

Providers.propTypes = {
  children: PropTypes.node.isRequired
};
