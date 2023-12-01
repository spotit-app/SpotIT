import { FieldHookConfig, useField } from 'formik';
import PropTypes from 'prop-types';
import { ClassAttributes, TextareaHTMLAttributes } from 'react';

interface TextAreaProps {
  label: string;
}

type TextAreaFieldProps = TextAreaProps & FieldHookConfig<string>;
type TextAreaElementProps = TextareaHTMLAttributes<HTMLTextAreaElement> &
  ClassAttributes<HTMLTextAreaElement>;

function TextArea({ label, ...props }: TextAreaFieldProps & TextAreaElementProps) {
  const [field, meta] = useField(props);

  return (
    <div className="sm:col-span-4">
      <label htmlFor={props.id} className="block text-sm font-medium leading-6">
        {label}
      </label>
      <div className="mt-2">
        <div className="flex rounded-md shadow-sm ring-1 ring-inset ring-gray-300 focus-within:ring-2 focus-within:ring-inset focus-within:ring-indigo-600 sm:max-w-md">
          <textarea
            {...field}
            {...props}
            className="block flex-1 border-0 bg-transparent py-1.5 pl-1 w-full placeholder:text-gray-400 focus:ring-0 sm:text-sm sm:leading-6"
            placeholder=""
          />
        </div>
        <div className="text-red-500 h-6">
          {meta.touched && meta.error ? <div className="error">{meta.error}</div> : null}
        </div>
      </div>
    </div>
  );
}

TextArea.propTypes = {
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired
};

export { TextArea };
