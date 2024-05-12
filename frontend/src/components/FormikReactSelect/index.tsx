import { useField, useFormikContext } from 'formik';
import Select, { ClassNamesConfig, MultiValue, SingleValue, StylesConfig } from 'react-select';
import { StateManagerProps } from 'node_modules/react-select/dist/declarations/src/useStateManager';
import { ChangeEvent } from 'react';

type MyOption = {
  label: string;
  value: string | number;
};

type GroupedOption = {
  label: string;
  options: MyOption[];
};

type Props = {
  name: string;
  label: string;
  customOnChange?: (value: (string | number)[] | string | number) => void;
} & Omit<StateManagerProps<MyOption, false | true, GroupedOption>, 'value' | 'onChange'>;

function FormikReactSelect(props: Props) {
  const { name, label, customOnChange, ...restProps } = props;
  const [field, meta] = useField(name);
  const { setFieldValue } = useFormikContext();

  const flattenedOptions = props.options?.flatMap((o) => {
    const isNotGrouped = 'value' in o;
    if (isNotGrouped) {
      return o;
    } else {
      return o.options;
    }
  });

  const value = flattenedOptions?.filter((o) => {
    const isArrayValue = Array.isArray(field.value);

    if (isArrayValue) {
      const values = field.value as Array<string | number>;
      return values.includes(o.value);
    } else {
      return field.value === o.value;
    }
  });

  const selectOnChange = (
    val: MultiValue<MyOption> | SingleValue<MyOption> | ChangeEvent<HTMLSelectElement>
  ) => {
    const _val = val as MyOption[] | MyOption;
    const isArray = Array.isArray(_val);
    if (isArray) {
      const values = _val.map((o) => o.value);
      setFieldValue(name, values);
      customOnChange && customOnChange(values);
    } else {
      setFieldValue(name, _val.value);
      customOnChange && customOnChange(_val.value);
    }
  };

  const selectClassNames: ClassNamesConfig<MyOption, boolean, GroupedOption> = {
    control: (state) =>
      `select select-bordered w-full pl-0 h-auto !bg-transparent${
        state.isFocused ? ' select-focused' : ''
      }`,
    multiValue: () => '!bg-primary font-medium items-center rounded px-1 mx-1',
    multiValueRemove: () =>
      '!text-base-100 !mx-1 !rounded-full w-5 h-5 hover:!bg-base-100 hover:!text-primary',
    menu: () => '!bg-base-200 overflow-y-auto h-40 flex flex-col',
    option: () => 'hover:bg-base-100 px-4 py-2',
    multiValueLabel: () => '!text-base-100 !mx-1',
    clearIndicator: () => '!text-gray-300 hover:!text-primary !mx-2',
    indicatorSeparator: () => '!bg-gray-300',
    dropdownIndicator: () => '!hidden',
    input: () => 'react-select-input'
  };

  const selectStyles: StylesConfig<MyOption, boolean, GroupedOption> = {
    singleValue: (styles) => ({ ...styles, color: 'default' }),
    control: () => ({}),
    option: () => ({
      cursor: 'pointer'
    }),
    menu: () => ({})
  };

  return (
    <div className="flex flex-col w-full">
      <label htmlFor={name} className="block text-sm font-medium leading-6 mb-1">
        {label}
      </label>

      <Select
        {...restProps}
        name={name}
        inputId={name}
        value={value}
        onChange={selectOnChange}
        classNames={selectClassNames}
        styles={selectStyles}
        noOptionsMessage={() => 'Brak wyników'}
        placeholder="Wybierz..."
      />

      <div className="text-red-500 h-6">
        {meta.touched && meta.error ? <div className="error">{meta.error}</div> : null}
      </div>
    </div>
  );
}

export { FormikReactSelect };
