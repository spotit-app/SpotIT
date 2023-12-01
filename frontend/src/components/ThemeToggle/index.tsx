import { useEffect, useState } from 'react';
import { FiSun } from 'react-icons/fi';
import { FiMoon } from 'react-icons/fi';
import { LIGHT_THEME, DARK_THEME } from '../../constants';

function ThemeToggle() {
  !localStorage.getItem('theme') && localStorage.setItem('theme', LIGHT_THEME);
  const [theme, setTheme] = useState(localStorage.getItem('theme'));

  useEffect(() => {
    document.querySelector('html')!.setAttribute('data-theme', theme!);
  }, [theme]);

  const changeTheme = () => {
    setTheme(theme === LIGHT_THEME ? DARK_THEME : LIGHT_THEME);
    localStorage.setItem('theme', theme === LIGHT_THEME ? DARK_THEME : LIGHT_THEME);
  };

  return (
    <div
      data-testid="theme-toggle"
      onClick={changeTheme}
      className="text-primary text-2xl cursor-pointer border-2 border-primary p-2 rounded-xl w-fit"
    >
      {theme === DARK_THEME ? <FiSun /> : <FiMoon />}
    </div>
  );
}

export { ThemeToggle };
