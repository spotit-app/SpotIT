import { useAuth0 } from '@auth0/auth0-react';
import { Loading } from '../../components';

function Profile() {
  const { user, isLoading } = useAuth0();

  return isLoading ? (
    <Loading />
  ) : (
    <div>
      <h1>Profile</h1>
      <img src={user?.picture} alt={user?.name} />
      <h3>Username: {user?.name}</h3>
      <h3>Email: {user?.email}</h3>
    </div>
  );
}

export { Profile };
