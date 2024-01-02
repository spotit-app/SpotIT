import { ClassAttributes, InputHTMLAttributes } from 'react';
import { FieldHookConfig, useField } from 'formik';
import PropTypes from 'prop-types';

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
    <div className="flex justify-between items-center input input-bordered p-2 my-2">
      <label htmlFor={props.id} className="text-sm font-medium leading-6 w-full cursor-pointer">
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
        className="checkbox checkbox-primary bg-base-100 focus:ring-offset-0"
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
