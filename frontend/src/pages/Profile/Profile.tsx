import { useAuth0 } from '@auth0/auth0-react';
import { Sidebar } from '../../components';
import { Outlet } from 'react-router-dom';

function Profile() {
  const { user } = useAuth0();

  return (
    <div className="flex">
      <Sidebar name={user!.name!} picture={user!.picture!} />
      <Outlet />
    </div>
  );
}

export { Profile };
