import { FormikHelpers } from 'formik';
import validationSchema from './ExperienceValidation';
import { Button, CheckBox, PopUpForm, Input } from '../../../components';
import { ChangeEvent, useState } from 'react';

interface ExperienceFormType {
  companyName: string;
  position: string;
  startDate: string;
  isChecked: boolean;
  endDate: string;
}

function ExperienceForm() {
  const [isChecked, setIsChecked] = useState(false);

  const initialValues = {
    companyName: '',
    position: '',
    startDate: '',
    isChecked: false,
    endDate: ''
  };

  const onSubmit = (
    values: ExperienceFormType,
    { setSubmitting }: FormikHelpers<ExperienceFormType>
  ) => {
    setTimeout(() => {
      window.alert(JSON.stringify(values));
      setSubmitting(false);
    }, 400);
  };

  return (
    <PopUpForm<ExperienceFormType>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Nazwa firmy" name="companyName" id="companyName" type="text" />
      <Input label="Pozycja" name="position" id="position" type="text" />
      <Input label="Data rozpoczęcia" name="startDate" id="startDate" type="date" />
      <CheckBox
        checked={isChecked}
        label="Ukończono?"
        id="isChecked"
        name="isChecked"
        customOnChange={(e: ChangeEvent<HTMLInputElement>) => {
          setIsChecked(e.target.checked);
        }}
      />
      {isChecked && <Input label="Data ukończenia" name="endDate" id="endDate" type="date" />}
      <Button type="submit">Zapisz</Button>
    </PopUpForm>
  );
}

export { ExperienceForm };
