import { Link } from 'react-router-dom';
import icons from 'assets/icons';
import { toSlug } from 'utils';

function AdminSidebar() {
  const navigation = [
    {
      name: 'Edukacja',
      icon: <icons.IoIosSchool className="w-5 h-5" />
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
      name: 'Poziom doświadczenia',
      icon: <icons.BsBarChartFill className="w-5 h-5" />
    },
    {
      name: 'Tryby pracy',
      icon: <icons.MdHomeWork className="w-5 h-5" />
    }
  ];

  const navigationList = navigation.map((item, idx) => (
    <li key={idx}>
      <Link
        to={`/admin/${toSlug(item.name)}`}
        className="flex items-center justify-start p-1 rounded-lg hover:bg-gray-200 hover:text-black active:bg-gray-300 duration-150"
      >
        <div className="flex items-center w-7 h-7 justify-center text-base-100 rounded-full bg-primary">
          {item.icon}
        </div>
        <span className="block ml-2">{item.name}</span>
      </Link>
    </li>
  ));

  return (
    <nav className="w-fit h-full">
      <div className="flex flex-col h-full pl-4 pr-4 pt-4 items-center sm:items-start">
        <div className="flex flex-col items-end">
          <label htmlFor="my-drawer-2" className="text-primary drawer-button lg:hidden">
            <icons.AiOutlineClose size="30" />
          </label>
          <Link to="/admin">
            <div className="w-full flex items-center">
              <div className="text-base text-xl font-semibold block">Panel administracyjny</div>
            </div>
          </Link>
        </div>
        <div className="w-full overflow-auto pt-2 mt-4 border-t-2 border-primary">
          <ul className="text-sm font-medium flex-1">{navigationList}</ul>
        </div>
      </div>
    </nav>
  );
}

export { AdminSidebar };
