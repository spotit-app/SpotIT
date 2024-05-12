import { useState, useRef, useEffect, Ref } from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { LogoutArguments } from 'types/auth';
import { useUser } from 'hooks';

interface AvatarMenuProps {
  picture: string;
  logout: (x: LogoutArguments) => void;
}

interface NavigationItem {
  title: string;
  path: string;
}

function AvatarMenu({ picture, logout }: AvatarMenuProps) {
  const { isAdmin } = useUser();
  const [state, setState] = useState<boolean>(false);
  const profileRef: Ref<HTMLButtonElement> = useRef<HTMLButtonElement>(null);

  const navigation: NavigationItem[] = [
    { title: 'Profil', path: 'profil' },
    { title: 'Moje firmy', path: 'moje-firmy' },
    { title: 'Moje aplikacje', path: 'moje-aplikacje' }
  ];

  const navigationElements = navigation.map((item, idx) => (
    <li key={idx}>
      <Link className="block font-bold md:hover:bg-base-200 md:p-3" to={item.path}>
        {item.title}
      </Link>
    </li>
  ));

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
    <div className="relative border-t md:border-none">
      <div>
        <button
          data-testid="profile"
          ref={profileRef}
          className="hidden w-10 h-10 outline-none rounded-full ring-primary md:focus:ring-2 md:block"
          onClick={() => setState(!state)}
        >
          <img src={picture} referrerPolicy="no-referrer" className="w-full h-full rounded-full" />
        </button>
      </div>
      <ul
        className={`bg-transparent md:bg-base-100 text-primary top-14 right-0 mt-6 space-y-6 md:absolute md:border md:rounded-md md:w-52 md:shadow-md md:space-y-0 md:mt-0 md:p-1 ${
          state ? '' : 'md:hidden'
        }`}
      >
        {navigationElements}
        {isAdmin && (
          <Link className="block font-bold md:hover:bg-base-200 md:p-3" to="/admin">
            Admin
          </Link>
        )}
        <button
          data-testid="logout"
          onClick={() => logout({ logoutParams: { returnTo: window.location.origin } })}
          className="block w-full text-justify font-bold border-t py-3 md:hover:bg-base-200 md:p-3"
        >
          Wyloguj
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
