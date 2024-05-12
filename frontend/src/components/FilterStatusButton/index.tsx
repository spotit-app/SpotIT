import PropTypes from 'prop-types';

interface FilterButtonProps {
  entity: string;
  onChange: () => void;
  isChecked: boolean;
}

function FilterStatusButton({ entity, onChange, isChecked }: FilterButtonProps) {
  return (
    <li key={entity} className="p-0.5 list-none">
      <input
        type="checkbox"
        id={entity}
        value=""
        className="hidden peer"
        onChange={onChange}
        checked={isChecked}
      />
      <label
        htmlFor={entity}
        className="text-gray-500 bg-base-300 border-2 p-2 border-gray-200 rounded-lg cursor-pointer peer-checked:border-primary"
      >
        <span className="text-lg font-semibold">{entity}</span>
      </label>
    </li>
  );
}

FilterStatusButton.propTypes = {
  entity: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  isChecked: PropTypes.bool.isRequired
};

export { FilterStatusButton };
