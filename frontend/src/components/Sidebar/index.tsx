import { Link } from 'react-router-dom';
import icons from '../../assets/icons/iconImports';
import { toSlug } from '../../utils';
import PropTypes from 'prop-types';

interface ProfileSideBarProps {
  picture: string;
  name: string;
}

function Sidebar({ name, picture }: ProfileSideBarProps) {
  const navigation = [
    {
      name: 'Dane osobowe',
      icon: <icons.BsFillPersonFill className="w-5 h-5" />
    },
    {
      name: 'Konta społecznościowe',
      icon: <icons.IoShareSocialSharp className="w-5 h-5" />
    },
    {
      name: 'Edukacja',
      icon: <icons.IoIosSchool className="w-5 h-5" />
    },
    {
      name: 'Doświadczenie',
      icon: <icons.MdOutlineWork className="w-5 h-5" />
    },
    {
      name: 'Umiejętności techniczne',
      icon: <icons.HiWrenchScrewdriver className="w-5 h-5" />
    },
    {
      name: 'Umiejętności miękkie',
      icon: <icons.GiTalk className="w-5 h-5" />
    },
    {
      name: 'Języki obce',
      icon: <icons.IoLanguageSharp className="w-5 h-5" />
    },
    {
      name: 'Projekty',
      icon: <icons.PiAppWindowFill className="w-5 h-5" />
    },
    {
      name: 'Zainteresowania',
      icon: <icons.MdInterests className="w-5 h-5" />
    },
    {
      name: 'Kursy',
      icon: <icons.PiStudentFill className="w-5 h-5" />
    }
  ];

  const navsFooter = [
    {
      name: 'Inne',
      icon: <icons.BiSolidHelpCircle className="w-5 h-5" />
    }
  ];

  const navigationList = navigation.map((item, idx) => (
    <li key={idx}>
      <Link
        to={`/profile/${toSlug(item.name)}`}
        className="flex items-center justify-center md:justify-start p-1 rounded-lg hover:bg-gray-200 hover:text-black active:bg-gray-300 duration-150"
      >
        <div className="flex items-center justify-center w-24 h-7  md:w-7 text-base-100 rounded-lg md:rounded-full bg-primary">
          {item.icon}
        </div>
        <span className="hidden md:block ml-2">{item.name}</span>
      </Link>
    </li>
  ));

  const navigationFooterList = navsFooter.map((item, idx) => (
    <li key={idx}>
      <Link
        to={`/profile/${toSlug(item.name)}`}
        className="flex items-center justify-center md:justify-start p-1 rounded-lg hover:bg-gray-200 hover:text-black active:bg-gray-300 duration-150"
      >
        <div className="flex items-center justify-center w-24 h-7 md:w-7 text-base-100 rounded-lg md:rounded-full bg-primary">
          {item.icon}
        </div>
        <span className="hidden md:block ml-2">{item.name}</span>
      </Link>
    </li>
  ));

  return (
    <nav className="w-fit h-full border-r-2 border-gray-300 space-y-8">
      <div className="flex flex-col h-full pl-4 pr-4 pt-4 items-center sm:items-start">
        <div className="h-20 flex items-center pl-2">
          <div className="w-full flex items-center gap-x-4">
            <img src={picture} alt="Profile Picture" className="w-20 h-20 rounded-full" />
            <div>
              <span className="text-base font-semibold hidden md:block">{name}</span>
            </div>
          </div>
        </div>
        <div className="w-full overflow-auto pt-2 mt-2 border-t-2 border-gray-300">
          <ul className="text-sm font-medium flex-1">{navigationList}</ul>
          <div className="pt-2 mt-2 border-t-2 border-gray-300">
            <ul className="text-sm flex flex-col font-medium">{navigationFooterList}</ul>
          </div>
        </div>
      </div>
    </nav>
  );
}

Sidebar.propTypes = {
  picture: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired
};

export { Sidebar };
