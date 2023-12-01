import icons from '../../../assets/icons/iconImports';
import { TechSkillsForm } from './TechSkillsForm';
import { PopUp } from '../../../components';
import { showModal } from '../../../utils/showModal';

function TechSkills() {
  return (
    <div className="w-9/12 flex flex-row text-base justify-center mt-3 space-y-12">
      <div className="w-9/12 border-b border-gray-900/10 pb-12">
        <h1 className="flex justify-center text-lg font-bold leading-7">Umiejętności techniczne</h1>
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
          <li>Umiejętność techniczna1</li>
          <li>Umiejętność techniczna2</li>
        </ul>
      </div>
      <PopUp title="Dodaj umiejętność">
        <TechSkillsForm />
      </PopUp>
    </div>
  );
}

export { TechSkills };
