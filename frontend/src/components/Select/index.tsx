import { FieldHookConfig, useField } from 'formik';
import PropTypes from 'prop-types';
import { ClassAttributes, SelectHTMLAttributes } from 'react';

interface SelectProps {
  label: string;
  options: string[];
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

  const selectOptions = options.map((option, index) => (
    <option key={index} className="bg-base-100">
      {option}
    </option>
  ));

  return (
    <div className="sm:col-span-4">
      <label htmlFor={props.id} className="block text-sm font-medium leading-6">
        {label}
      </label>
      <div className="mt-2">
        <div className="flex rounded-md shadow-sm ring-1 ring-inset ring-gray-300 focus-within:ring-2 focus-within:ring-inset focus-within:ring-indigo-600 sm:max-w-md">
          <select
            {...field}
            {...props}
            onChange={(e) => {
              customOnChange && customOnChange(e);
              field.onChange(e);
            }}
            className="select select-bordered block flex-1 border-0 bg-transparent py-1.5 pl-1 focus:ring-0 sm:text-sm sm:leading-6"
          >
            <option disabled hidden value="">
              {placeholder}
            </option>
            {selectOptions}
          </select>
        </div>
        <div className="text-red-500 h-6">
          {meta.touched && meta.error ? <div className="error">{meta.error}</div> : null}
        </div>
      </div>
    </div>
  );
}

Select.propTypes = {
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired,
  options: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired,
  placeholder: PropTypes.string,
  customOnChange: PropTypes.func
};

export { Select };
