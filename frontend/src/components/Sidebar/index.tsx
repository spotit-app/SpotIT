import { Link } from 'react-router-dom';
import { useUser } from 'hooks/useUser';
import icons from 'assets/icons';
import { toSlug } from 'utils';

function Sidebar() {
  const { userName, userPicture } = useUser();

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
        to={`/profil/${toSlug(item.name)}`}
        className="flex items-center justify-start p-1 rounded-lg hover:bg-gray-200 hover:text-black active:bg-gray-300 duration-150"
      >
        <div className="flex items-center w-7 h-7 justify-center text-base-100 rounded-full bg-primary">
          {item.icon}
        </div>
        <span className="block ml-2">{item.name}</span>
      </Link>
    </li>
  ));

  const navigationFooterList = navsFooter.map((item, idx) => (
    <li key={idx}>
      <Link
        to={`/profil/${toSlug(item.name)}`}
        className="flex items-center justify-start p-1 rounded-lg hover:bg-gray-200 hover:text-black active:bg-gray-300 duration-150"
      >
        <div className="flex items-center w-7 h-7 justify-center text-base-100 rounded-full bg-primary">
          {item.icon}
        </div>
        <span className="ml-2">{item.name}</span>
      </Link>
    </li>
  ));

  return (
    <nav className="w-fit h-full">
      <div className="flex flex-col h-full pl-4 pr-4 pt-4 items-center sm:items-start">
        <div className="flex flex-col items-end pl-2">
          <label htmlFor="my-drawer-2" className="text-primary drawer-button lg:hidden">
            <icons.AiOutlineClose size="30" />
          </label>
          <Link to="/profil">
            <div className="w-full flex items-center gap-x-4">
              <img
                src={userPicture}
                referrerPolicy="no-referrer"
                alt="Profile Picture"
                className="w-20 h-20 rounded-full"
              />
              <div>
                <span className="text-base font-semibold block">{userName}</span>
              </div>
            </div>
          </Link>
        </div>
        <div className="w-full overflow-auto pt-2 mt-4 border-t-2 border-primary">
          <ul className="text-sm font-medium flex-1">{navigationList}</ul>
          <ul className="text-sm flex flex-col font-medium">{navigationFooterList}</ul>
        </div>
      </div>
    </nav>
  );
}

export { Sidebar };
