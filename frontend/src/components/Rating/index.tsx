import { ClassAttributes, InputHTMLAttributes } from 'react';
import { FieldHookConfig, useField } from 'formik';
import PropTypes from 'prop-types';

interface RatingProps {
  label: string;
  initialValue: number;
}

type InputFieldProps = RatingProps & FieldHookConfig<string>;
type InputElementProps = InputHTMLAttributes<HTMLInputElement> & ClassAttributes<HTMLInputElement>;

function Rating({ label, ...props }: InputFieldProps & InputElementProps) {
  const [field, meta, helpers] = useField(props);
  const { value } = meta;
  const { setValue } = helpers;

  const ratingInputs = [1, 2, 3, 4, 5].map((rate, index) => (
    <input
      key={`${props.id}-${rate}`}
      {...field}
      data-testid={`${props.id}-${index}`}
      id={`${props.id}-${index}`}
      name={props.name}
      type="radio"
      className="mask mask-star-2 bg-primary checked:bg-primary star"
      value={rate}
      checked={+value === rate}
      onClick={(e) => {
        setValue(rate.toString());
        field.onChange(e);
      }}
    />
  ));

  const hiddenInput = (
    <input
      key={`${props.id}-0`}
      id="stars"
      {...field}
      type="radio"
      name={props.name}
      value={0}
      className="hidden"
      checked={+value === 0}
    />
  );

  return (
    <>
      <label htmlFor="stars" className="block text-sm font-medium leading-6 mb-1 mt-2">
        {label}
      </label>
      <div className="rating rating-lg">
        {hiddenInput}
        {ratingInputs}
      </div>
      <div className="text-red-500 h-6">
        {meta.touched && meta.error ? <div className="error">{meta.error}</div> : null}
      </div>
    </>
  );
}

Rating.propTypes = {
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired
};

export { Rating };
