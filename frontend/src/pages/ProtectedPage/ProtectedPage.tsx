import { useAuth0 } from '@auth0/auth0-react';
import { Loading } from '../../components';
import { PropsWithChildren } from 'react';
import PropTypes from 'prop-types';

function ProtectedPage({ children }: PropsWithChildren) {
  const { isAuthenticated, isLoading, loginWithRedirect } = useAuth0();

  return <>{isLoading ? <Loading /> : isAuthenticated ? children : loginWithRedirect()}</>;
}

export { ProtectedPage };

ProtectedPage.propTypes = {
  children: PropTypes.node.isRequired
};
