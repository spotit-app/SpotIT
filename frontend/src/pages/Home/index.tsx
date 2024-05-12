import HomeLightDesktopImg from 'assets/images/home-light-desktop.svg';
import { Button } from 'flowbite-react';
import { useNavigate } from 'react-router-dom';

function Home() {
  const navigate = useNavigate();

  return (
    <div className="w-full h-[90vh] relative">
      <div className="w-full center relative flex justify-center">
        <img
          src={HomeLightDesktopImg}
          alt="Home banner"
          className="z-0 home max-h-[70vh] sm:max-h-none"
        />
        <div className="absolute top-2/3 left-1/2 -translate-x-1/2 -translate-y-2/3 sm:top-1/2 sm:left-1/3 transform sm:-translate-x-1/3 sm:-translate-y-1/2 z-10 uppercase">
          <div className="flex flex-wrap justify-center text-3xl lg:text-5xl text-home">
            <span className="font-bold mr-2">Twoja</span>wyszukiwarka
          </div>
          <div className="text-center my-2 text-xl lg:text-3xl text-white">
            Ofert pracy lub pracowników
          </div>
          <div className="flex mt-5 justify-center">
            <Button
              type="button"
              className="mr-2 bg-home"
              onClick={() => navigate('/oferty-pracy')}
            >
              Oferty
            </Button>
            <Button type="button" className="mr-2 bg-home" onClick={() => navigate('/pracownicy')}>
              Pracownicy
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Home;
