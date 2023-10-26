import PropTypes from 'prop-types';
import { Auth0Provider } from '@auth0/auth0-react';
import { PropsWithChildren } from 'react';

function AuthProvider({ children }: PropsWithChildren) {
  return (
    <Auth0Provider
      domain={process.env.VITE_AUTH0_DOMAIN!}
      clientId={process.env.VITE_AUTH0_CLIENT_ID!}
      authorizationParams={{
        redirect_uri: window.location.origin
      }}
    >
      {children}
    </Auth0Provider>
  );
}
export { AuthProvider };

AuthProvider.propTypes = {
  children: PropTypes.node.isRequired
};
