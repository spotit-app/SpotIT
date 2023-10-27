import PropTypes from 'prop-types';
import { Auth0Provider } from '@auth0/auth0-react';
import { PropsWithChildren } from 'react';

function AuthProvider({ children }: PropsWithChildren) {
  return (
    <Auth0Provider
      domain="spot-it.eu.auth0.com"
      clientId="X6Kfhqp6UuE93tVOfQeOtdbo92RUF3dh"
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
