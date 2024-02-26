import { ReadForeignLanguageName, ReadTechSkillName } from '@/types/profile';
import PropTypes from 'prop-types';

interface FilterButtonProps {
  entity: ReadTechSkillName | ReadForeignLanguageName;
  onChange: () => void;
  isChecked: boolean;
}

function FilterButton({ entity, onChange, isChecked }: FilterButtonProps) {
  const isReadTechSkillName = (
    entity: ReadTechSkillName | ReadForeignLanguageName
  ): entity is ReadTechSkillName => 'logoUrl' in entity;

  return (
    <li key={entity.id} className="p-0.5">
      <input
        type="checkbox"
        id={entity.name}
        value=""
        className="hidden peer"
        onChange={onChange}
        checked={isChecked}
      />
      <label
        htmlFor={entity.name}
        className="text-gray-500 bg-base-100 border-2 border-gray-200 rounded-lg cursor-pointer peer-checked:border-primary"
      >
        <img
          src={isReadTechSkillName(entity) ? entity.logoUrl : entity.flagUrl}
          width="20"
          height="20"
        />
        <span className="text-lg font-semibold">{entity.name}</span>
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
