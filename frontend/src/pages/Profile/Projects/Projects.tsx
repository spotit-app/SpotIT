import icons from '../../../assets/icons/iconImports';
import { ProjectsForm } from './ProjectsForm';
import { PopUp } from '../../../components';
import { showModal } from '../../../utils/showModal';

function Projects() {
  return (
    <div className="w-9/12 flex flex-row text-base justify-center mt-3 space-y-12">
      <div className="w-9/12 border-b border-gray-900/10 pb-12">
        <h1 className="flex justify-center text-lg font-bold leading-7">Projekty</h1>
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
          <li>Projekt1</li>
          <li>Projekt2</li>
        </ul>
      </div>
      <PopUp title="Dodaj projekt">
        <ProjectsForm />
      </PopUp>
    </div>
  );
}

export { Projects };
