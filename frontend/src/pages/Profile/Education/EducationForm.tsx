import { FormikHelpers } from 'formik';
import validationSchema from './EducationValidation';
import { Input, Select, Button, CheckBox } from '../../../components';
import { ChangeEvent, useState } from 'react';
import { PopUpForm } from '../../../components';

interface EducationFormType {
  educationLevel: string;
  customEducationLevel: string;
  schoolName: string;
  faculty: string;
  startDate: string;
  isChecked: boolean;
  endDate: string;
}

function EducationForm() {
  const [selectedEducationLevel, setSelectedEducationLevel] = useState('');
  const [isChecked, setIsChecked] = useState(false);

  const optionsToSelect = ['Stopień1', 'Stopień2', 'Inny'];

  const initialValues = {
    educationLevel: '',
    customEducationLevel: '',
    schoolName: '',
    faculty: '',
    startDate: '',
    isChecked: false,
    endDate: ''
  };

  const onSubmit = (
    values: EducationFormType,
    { setSubmitting }: FormikHelpers<EducationFormType>
  ) => {
    setTimeout(() => {
      window.alert(JSON.stringify(values));
      setSubmitting(false);
    }, 400);
  };

  return (
    <PopUpForm<EducationFormType>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Select
        label="Stopień edukacji"
        name="educationLevel"
        id="educationLevel"
        placeholder="Wybierz stopień edukacji"
        options={optionsToSelect}
        customOnChange={(e: ChangeEvent<HTMLSelectElement>) => {
          setSelectedEducationLevel(e.target.value);
        }}
      />
      {selectedEducationLevel === 'Inny' && (
        <Input
          label="Podaj swoją wartość"
          name="customEducationLevel"
          id="customEducationLevel"
          type="text"
          placeholder="Wprowadź inny stopień edukacji"
        />
      )}
      <Input label="Nazwa szkoły" name="schoolName" id="schoolName" type="text" />
      <Input label="Kierunek" name="faculty" id="faculty" type="text" />
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

export { EducationForm };
