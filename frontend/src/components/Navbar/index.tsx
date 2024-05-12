import { useAuth0 } from '@auth0/auth0-react';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { AvatarMenu } from '../AvatarMenu';
import { ThemeToggle } from '../ThemeToggle';
import Logo from 'assets/images/logo.svg';
import { useUser } from 'hooks';

function Navbar() {
  const [menuOpened, setMenuOpened] = useState(false);
  const { isAuthenticated, loginWithRedirect, logout } = useAuth0();
  const { userPicture } = useUser();

  const navigation = [
    { title: 'Dla pracownika', path: '/oferty-pracy' },
    { title: 'Dla pracodawcy', path: '/pracownicy' }
  ];

  const navigationElements = navigation.map((item, idx) => {
    return (
      <li key={idx} className="text-primary font-bold">
        <Link to={item.path} className="block">
          {item.title}
        </Link>
      </li>
    );
  });

  useEffect(() => {
    document.onclick = (e) => {
      const target = e.target as HTMLElement;
      if (!target.closest('.menu-btn')) setMenuOpened(false);
    };
  }, []);

  return (
    <nav
      className={`sticky border-b-2 border-primary w-full md:text-sm lg:z-10 ${
        menuOpened ? 'shadow-lg rounded-xl mt-2 md:shadow-none md:mx-2 md:mt-0' : ''
      }`}
    >
      <div className="gap-x-14items-center max-w-screen-xl mx-auto px-4 md:flex md:px-8">
        <div className="flex items-center justify-between py-5 md:block px-3">
          <Link to="/">
            <img src={Logo} width={120} height={50} className="logo" alt="SpotIT logo" />
          </Link>
          <div className="md:hidden">
            <button
              data-testid="menu-btn"
              className="menu-btn text-primary focus:outline-none"
              onClick={() => setMenuOpened(!menuOpened)}
            >
              {menuOpened ? (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-6 w-6"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    fillRule="evenodd"
                    d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                    clipRule="evenodd"
                  />
                </svg>
              ) : (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"
                  />
                </svg>
              )}
            </button>
          </div>
        </div>
        <div
          className={`flex-1 items-center mx-2 mt-8 md:mt-0 md:flex ${
            menuOpened ? 'block' : 'hidden'
          } `}
        >
          <ul className="justify-center items-center space-y-6 md:flex md:space-x-6 md:space-y-0">
            {navigationElements}
          </ul>
          <div className="flex-1 gap-x-6 items-center justify-end my-6 space-y-6 md:flex md:space-y-0 md:my-0">
            {!isAuthenticated && (
              <>
                <button
                  data-testid="login"
                  onClick={() => loginWithRedirect()}
                  className="btn btn-primary flex items-center"
                >
                  Zaloguj
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 20 20"
                    fill="currentColor"
                    className="w-5 h-5"
                  >
                    <path
                      fillRule="evenodd"
                      d="M7.21 14.77a.75.75 0 01.02-1.06L11.168 10 7.23 6.29a.75.75 0 111.04-1.08l4.5 4.25a.75.75 0 010 1.08l-4.5 4.25a.75.75 0 01-1.06-.02z"
                      clipRule="evenodd"
                    />
                  </svg>
                </button>
              </>
            )}
            {isAuthenticated && <AvatarMenu picture={userPicture} logout={logout} />}
            <ThemeToggle />
          </div>
        </div>
      </div>
    </nav>
  );
}

export { Navbar };
