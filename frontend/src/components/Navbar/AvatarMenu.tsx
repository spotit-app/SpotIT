import { useState, useRef, useEffect, Ref } from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { LogoutArguments } from '../../types/auth/types';

interface AvatarMenuProps {
  picture: string;
  logout: (x: LogoutArguments) => void;
}

function AvatarMenu({ picture, logout }: AvatarMenuProps) {
  const [state, setState] = useState<boolean>(false);
  const profileRef: Ref<HTMLButtonElement> = useRef<HTMLButtonElement>(null);

  const navigation = [{ title: 'Profile', path: 'profile' }];

  useEffect(() => {
    const handleDropDown = (e: MouseEvent) => {
      if (!profileRef.current?.contains(e.target as Node)) setState(false);
    };
    document.addEventListener('click', handleDropDown);
    return () => {
      document.removeEventListener('click', handleDropDown);
    };
  }, [profileRef]);

  return (
    <div className="relative border-t lg:border-none">
      <div>
        <button
          data-testid="profile"
          ref={profileRef}
          className="hidden w-10 h-10 outline-none rounded-full ring-offset-2 ring-gray-200 lg:focus:ring-2 lg:block"
          onClick={() => setState(!state)}
        >
          <img src={picture} className="w-full h-full rounded-full" />
        </button>
      </div>
      <ul
        className={`bg-white top-14 right-0 mt-6 space-y-6 lg:absolute lg:border lg:rounded-md lg:w-52 lg:shadow-md lg:space-y-0 lg:mt-0 ${
          state ? '' : 'lg:hidden'
        }`}
      >
        {navigation.map((item, idx) => (
          <li key={idx}>
            <Link
              className="block text-gray-600 hover:text-gray-900 lg:hover:bg-gray-50 lg:p-3"
              to={item.path}
            >
              {item.title}
            </Link>
          </li>
        ))}
        <button
          data-testid="logout"
          onClick={() => logout({ logoutParams: { returnTo: window.location.origin } })}
          className="block w-full text-justify text-gray-600 hover:text-gray-900 border-t py-3 lg:hover:bg-gray-50 lg:p-3"
        >
          Logout
        </button>
      </ul>
    </div>
  );
}

export { AvatarMenu };

AvatarMenu.propTypes = {
  picture: PropTypes.string.isRequired,
  logout: PropTypes.func.isRequired
};
