import { useAuth0 } from '@auth0/auth0-react';
import { PropsWithChildren } from 'react';
import PropTypes from 'prop-types';
import { Loading } from 'components';

function ProtectedPage({ children }: PropsWithChildren) {
  const { isAuthenticated, isLoading, loginWithRedirect } = useAuth0();

  return <>{isLoading ? <Loading /> : isAuthenticated ? children : loginWithRedirect()}</>;
}

export default ProtectedPage;

ProtectedPage.propTypes = {
  children: PropTypes.node.isRequired
};
