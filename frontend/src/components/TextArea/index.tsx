import { ClassAttributes, TextareaHTMLAttributes } from 'react';
import { FieldHookConfig, useField } from 'formik';
import PropTypes from 'prop-types';

interface TextAreaProps {
  label: string;
}

type TextAreaFieldProps = TextAreaProps & FieldHookConfig<string>;
type TextAreaElementProps = TextareaHTMLAttributes<HTMLTextAreaElement> &
  ClassAttributes<HTMLTextAreaElement>;

function TextArea({ label, ...props }: TextAreaFieldProps & TextAreaElementProps) {
  const [field, meta] = useField(props);

  return (
    <>
      <label htmlFor={props.id} className="block text-sm font-medium leading-6 mb-1">
        {label}
      </label>

      <textarea
        {...field}
        {...props}
        autoComplete="off"
        placeholder=""
        className="textarea textarea-bordered w-full"
      />
      <div className="text-red-500 h-6">
        {meta.touched && meta.error ? <div className="error">{meta.error}</div> : null}
      </div>
    </>
  );
}

TextArea.propTypes = {
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired
};

export { TextArea };
