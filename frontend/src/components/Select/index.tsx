import { ClassAttributes, SelectHTMLAttributes } from 'react';
import { FieldHookConfig, useField } from 'formik';
import PropTypes from 'prop-types';
import { SelectOption } from 'types/shared';

interface SelectProps {
  label: string;
  options: SelectOption[];
  placeholder: string;
  customOnChange?: (event: React.ChangeEvent<HTMLSelectElement>) => void;
}

type SelectFieldProps = SelectProps & FieldHookConfig<string>;
type SelectElementProps = SelectHTMLAttributes<HTMLSelectElement> &
  ClassAttributes<HTMLSelectElement>;

function Select({
  label,
  options,
  placeholder,
  customOnChange,
  ...props
}: SelectFieldProps & SelectElementProps) {
  const [field, meta] = useField(props);

  const selectOptions = options.map((option: SelectOption, index) => (
    <option key={index} value={option.value} className="bg-base-100">
      {option.label}
    </option>
  ));

  return (
    <div>
      <label htmlFor={props.id} className="block text-sm font-medium leading-6 mb-1">
        {label}
      </label>

      <select
        {...field}
        {...props}
        onChange={(e) => {
          customOnChange && customOnChange(e);
          field.onChange(e);
        }}
        className="select select-bordered w-full"
      >
        <option disabled hidden value="">
          {placeholder}
        </option>
        {selectOptions}
      </select>

      <div className="text-red-500 h-6">
        {meta.touched && meta.error ? <div className="error">{meta.error}</div> : null}
      </div>
    </div>
  );
}

Select.propTypes = {
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired,
  options: PropTypes.arrayOf(PropTypes.object.isRequired).isRequired,
  placeholder: PropTypes.string,
  customOnChange: PropTypes.func
};

export { Select };
