import icons from '../../../assets/icons/iconImports';
import { ExperienceForm } from './ExperienceForm';
import { PopUp } from '../../../components';
import { showModal } from '../../../utils/showModal';

function Experience() {
  return (
    <div className="w-9/12 flex flex-row text-base justify-center mt-3 space-y-12">
      <div className="w-9/12 border-b border-gray-900/10 pb-12">
        <h1 className="flex justify-center text-lg font-bold leading-7">Doświadczenie</h1>
        <div className="flex justify-center mt-5">
          <button
            type="button"
            onClick={showModal}
            className="text-white bg-primary hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-lg p-2.5"
          >
            <icons.IoAdd />
          </button>
        </div>
        <ul>
          <li>Doświadczenie1</li>
          <li>Doświadczenie2</li>
        </ul>
      </div>
      <PopUp title="Dodaj stanowisko">
        <ExperienceForm />
      </PopUp>
    </div>
  );
}

export { Experience };
