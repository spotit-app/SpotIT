import { ReadExperience, ReadForeignLanguageName, ReadTechSkillName } from 'types/profile';
import { ReadWorkMode } from 'types/company';
import PropTypes from 'prop-types';

interface FilterButtonProps {
  entity: ReadTechSkillName | ReadForeignLanguageName | ReadExperience | ReadWorkMode;
  onChange: () => void;
  isChecked: boolean;
}

function FilterButton({ entity, onChange, isChecked }: FilterButtonProps) {
  const isReadTechSkillName = (
    entity: ReadTechSkillName | ReadForeignLanguageName
  ): entity is ReadTechSkillName => 'logoUrl' in entity;

  const isReadTechSkillNameOrForeignLanguageName = (
    entity: ReadTechSkillName | ReadForeignLanguageName | ReadExperience | ReadWorkMode
  ): entity is ReadTechSkillName | ReadForeignLanguageName =>
    'logoUrl' in entity || 'flagUrl' in entity;

  const getEntityName = (
    entity: ReadTechSkillName | ReadForeignLanguageName | ReadExperience | ReadWorkMode
  ): string => {
    if ('name' in entity) {
      return entity.name;
    } else {
      return entity.companyName;
    }
  };

  return (
    <li key={entity.id} className="p-0.5">
      <input
        type="checkbox"
        id={getEntityName(entity)}
        value=""
        className="hidden peer"
        onChange={onChange}
        checked={isChecked}
      />
      <label
        htmlFor={getEntityName(entity)}
        className="text-gray-500 bg-base-100 border-2 border-gray-200 rounded-lg cursor-pointer peer-checked:border-primary"
      >
        {isReadTechSkillNameOrForeignLanguageName(entity) && (
          <div className="w-8 h-8 flex justify-center items-center">
            <img
              src={isReadTechSkillName(entity) ? entity.logoUrl : entity.flagUrl}
              alt="Skill Picture"
              className="max-w-full max-h-full"
            />
          </div>
        )}
        <span className="text-lg font-semibold">{getEntityName(entity)}</span>
      </label>
    </li>
  );
}

FilterButton.propTypes = {
  entity: PropTypes.object.isRequired,
  onChange: PropTypes.func.isRequired,
  isChecked: PropTypes.bool.isRequired
};

export { FilterButton };
