import { ClassAttributes, InputHTMLAttributes } from 'react';
import PropTypes from 'prop-types';
import { FieldHookConfig, useField } from 'formik';

interface CheckBoxProps {
  label: string;
  checked: boolean;
  customOnChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

type InputFieldProps = CheckBoxProps & FieldHookConfig<string>;
type InputElementProps = InputHTMLAttributes<HTMLInputElement> & ClassAttributes<HTMLInputElement>;

function CheckBox({
  label,
  checked,
  customOnChange,
  ...props
}: InputFieldProps & InputElementProps) {
  const [field] = useField(props);

  return (
    <div className="sm:col-span-4">
      <label htmlFor={props.id} className="text-sm font-medium leading-6 mr-2">
        {label}
      </label>
      <input
        type="checkbox"
        {...field}
        {...props}
        checked={checked}
        onChange={(e) => {
          customOnChange && customOnChange(e);
          field.onChange(e);
        }}
        className="checkbox checkbox-primary"
      />
    </div>
  );
}

CheckBox.propTypes = {
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired,
  checked: PropTypes.bool.isRequired,
  customOnChange: PropTypes.func
};

export { CheckBox };
