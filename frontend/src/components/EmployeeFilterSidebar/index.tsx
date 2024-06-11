import { useSearchParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { ReadForeignLanguageName, ReadTechSkillName } from 'types/profile';
import { useForeignLanguages, useTechSkills } from 'hooks';
import { FilterButton } from '../FilterButton';
import { Loading } from '../Loading';
import { Button } from '../Button';
import icons from 'assets/icons';

function EmployeeFilterSidebar() {
  const { foreignLanguageNames, foreignLanguageNamesIsPending } = useForeignLanguages();
  const { techSkillNames, techSkillNamesIsPending } = useTechSkills();
  const [params, setParams] = useSearchParams();

  const [selectedTechSkills, setSelectedTechSkills] = useState<string[]>([]);
  const [selectedForeignLanguages, setSelectedForeignLanguages] = useState<string[]>([]);

  useEffect(() => {
    setSelectedTechSkills(params.getAll('techSkillNameIds'));
    setSelectedForeignLanguages(params.getAll('foreignLanguageNameIds'));
  }, [params]);

  const toggleButton = (array: string[], selectedId: string) =>
    array.includes(selectedId)
      ? [...array.filter((id) => id !== selectedId)]
      : [...array, selectedId];

  const onFilterClick = () => {
    setParams({
      techSkillNameIds: selectedTechSkills,
      foreignLanguageNameIds: selectedForeignLanguages
    });
  };

  const onResetClick = () => {
    setSelectedForeignLanguages([]);
    setSelectedTechSkills([]);
  };

  const techSkillsToChooseFrom = techSkillNames?.map((techSkill: ReadTechSkillName) => (
    <FilterButton
      key={techSkill.id}
      entity={techSkill}
      onChange={() => setSelectedTechSkills((arr) => toggleButton(arr, techSkill.id.toString()))}
      isChecked={selectedTechSkills.includes(techSkill.id.toString())}
    />
  ));

  const foreignLanguaguesToChooseFrom = foreignLanguageNames?.map(
    (foreignLanguage: ReadForeignLanguageName) => (
      <FilterButton
        key={foreignLanguage.id}
        entity={foreignLanguage}
        onChange={() =>
          setSelectedForeignLanguages((arr) => toggleButton(arr, foreignLanguage.id.toString()))
        }
        isChecked={selectedForeignLanguages.includes(foreignLanguage.id.toString())}
      />
    )
  );

  return (
    <nav className="w-fit h-full">
      <div className="flex flex-col h-full pl-3 pr-3 pt-3 items-center sm:items-start">
        <div className="flex flex-col items-end">
          <label htmlFor="my-drawer-2" className="text-primary drawer-button lg:hidden">
            <icons.AiOutlineClose size="30" />
          </label>
          <div className="w-80 sm:w-96  flex flex-col items-center gap-x-4">
            <div role="alert" className="text-center mb-4 alert border-2 border-primary rounded-lg">
              <icons.IoInformationCircleSharp className="text-primary w-7 h-7" />
              <h1 className="italic text-lg">Jakiego pracownika szukasz?</h1>
            </div>
            {techSkillNamesIsPending || foreignLanguageNamesIsPending ? (
              <Loading />
            ) : (
              <>
                <h3 className="mt-5 mb-3 text-lg self-start font-medium">Technologie:</h3>
                <ul className="flex flex-row justify-center flex-wrap">{techSkillsToChooseFrom}</ul>
                <h3 className="mt-5 mb-3 text-lg self-start font-medium">Języki obce:</h3>
                <ul className="flex flex-row justify-center flex-wrap">
                  {foreignLanguaguesToChooseFrom}
                </ul>
                <div className="flex self-end mt-10 space-x-2">
                  <Button type="button" className="bg-secondary" onClick={onResetClick}>
                    Resetuj
                  </Button>
                  <Button type="button" onClick={onFilterClick}>
                    Filtruj
                  </Button>
                </div>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}

export { EmployeeFilterSidebar };
