import { Outlet } from 'react-router-dom';
import { Sidebar } from 'components';
import icons from 'assets/icons';

function Profile() {
  return (
    <div className="flex">
      <div className="drawer lg:drawer-open">
        <input id="my-drawer-2" type="checkbox" className="drawer-toggle" />
        <div className="drawer-content flex">
          <Outlet />
          <label
            htmlFor="my-drawer-2"
            className="btn btn-primary drawer-button lg:hidden sidebar-toggle fixed"
          >
            <icons.HiMenuAlt2 size="30" />
          </label>
        </div>
        <div className="drawer-side">
          <label htmlFor="my-drawer-2" aria-label="close sidebar" className="drawer-overlay" />
          <ul className="menu p-4 w-80 min-h-full bg-base-200 text-base-content">
            <Sidebar />
          </ul>
        </div>
      </div>
    </div>
  );
}

export default Profile;
