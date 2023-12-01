import { FieldHookConfig, useField } from 'formik';
import PropTypes from 'prop-types';
import { ClassAttributes, InputHTMLAttributes, useState } from 'react';

interface RatingProps {
  label: string;
  initialValue: number;
}

type InputFieldProps = RatingProps & FieldHookConfig<string>;
type InputElementProps = InputHTMLAttributes<HTMLInputElement> & ClassAttributes<HTMLInputElement>;

function Rating({ label, initialValue, ...props }: InputFieldProps & InputElementProps) {
  const [field, meta] = useField(props);
  const [rating, setRating] = useState(initialValue);

  const ratingInputs = [1, 2, 3, 4, 5].map((value, index) => (
    <input
      key={index}
      {...field}
      data-testid={`${props.id}-${index}`}
      id={`${props.id}-${index}`}
      name={props.name}
      type="radio"
      className="mask mask-star-2 bg-green-500"
      value={value}
      checked={rating === value}
      onClick={(e) => {
        setRating(value);
        field.onChange(e);
      }}
    />
  ));

  const hiddenInput = (
    <input
      key={0}
      id="stars"
      {...field}
      type="radio"
      name={props.name}
      value={0}
      className="hidden"
      checked={rating === 0}
    />
  );

  return (
    <div className="sm:col-span-4">
      <label htmlFor="stars" className="block text-sm font-medium leading-6">
        {label}
      </label>
      <div className="mt-2">
        <div className="rating rating-lg">
          {hiddenInput}
          {ratingInputs}
        </div>
        <div className="text-red-500 h-6">
          {meta.touched && meta.error ? <div className="error">{meta.error}</div> : null}
        </div>
      </div>
    </div>
  );
}

Rating.propTypes = {
  label: PropTypes.string.isRequired,
  initialValue: PropTypes.number.isRequired,
  id: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired
};

export { Rating };
