import { ClassAttributes, InputHTMLAttributes } from 'react';
import { FieldHookConfig, useField } from 'formik';
import PropTypes from 'prop-types';

interface InputProps {
  label: string;
}

type InputFieldProps = InputProps & FieldHookConfig<string>;
type InputElementProps = InputHTMLAttributes<HTMLInputElement> & ClassAttributes<HTMLInputElement>;

function Input({ label, ...props }: InputFieldProps & InputElementProps) {
  const [field, meta] = useField(props);

  return (
    <div className="flex flex-col w-full">
      <label htmlFor={props.id} className="block text-sm font-medium leading-6 mb-1">
        {label}
      </label>

      <input {...field} {...props} autoComplete="off" className="input input-bordered w-full" />

      <div className="text-red-500 h-6">
        {meta.touched && meta.error ? <div className="error">{meta.error}</div> : null}
      </div>
    </div>
  );
}

Input.propTypes = {
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired
};

export { Input };
