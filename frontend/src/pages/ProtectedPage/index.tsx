import { ReactNode, useEffect } from 'react';
import PropTypes from 'prop-types';
import { Loading } from 'components';
import { useNavigate } from 'react-router-dom';
import { useUser } from 'hooks';

interface ProtectedPageProps {
  children?: ReactNode;
  adminProtected?: boolean;
}

function ProtectedPage({ children, adminProtected }: ProtectedPageProps) {
  const { isAuthenticated, isLoading, loginWithRedirect, isAdmin, roles } = useUser();
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated && adminProtected && roles) {
      !isAdmin && navigate('/');
    } else if (!isLoading && !isAuthenticated) {
      loginWithRedirect();
    }
  }, [isAuthenticated, adminProtected, isAdmin, roles, navigate, loginWithRedirect]);

  if (isLoading) {
    return <Loading />;
  }

  return children;
}

export default ProtectedPage;

ProtectedPage.propTypes = {
  children: PropTypes.node.isRequired,
  adminProtected: PropTypes.bool
};
