import { FormikHelpers } from 'formik';
import validationSchema from './SoftSkillsValidation';
import { Button, PopUpForm, Input, Rating, Select } from '../../../components';
import { ChangeEvent, useState } from 'react';

interface SoftSkillsFormType {
  softSkillName: string;
  customSoftSkillName: string;
  softSkillLevel: number;
}

function SoftSkillsForm() {
  const [selectedSoftSkillName, setSelectedSoftSkillName] = useState('');

  const optionsToSelect = ['Umiejętność1', 'Umiejętność2', 'Inna'];

  const initialValues = {
    softSkillName: '',
    customSoftSkillName: '',
    softSkillLevel: 0
  };

  const onSubmit = (
    values: SoftSkillsFormType,
    { setSubmitting }: FormikHelpers<SoftSkillsFormType>
  ) => {
    setTimeout(() => {
      window.alert(JSON.stringify(values));
      setSubmitting(false);
    }, 400);
  };

  return (
    <PopUpForm<SoftSkillsFormType>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Select
        label="Nazwa"
        name="softSkillName"
        id="softSkillName"
        placeholder="Wybierz umiejętność"
        options={optionsToSelect}
        customOnChange={(e: ChangeEvent<HTMLSelectElement>) => {
          setSelectedSoftSkillName(e.target.value);
        }}
      />
      {selectedSoftSkillName === 'Inna' && (
        <Input
          label="Podaj swoją wartość"
          name="customSoftSkillName"
          id="customSoftSkillName"
          type="text"
          placeholder="Wprowadź inną nazwę"
        />
      )}
      <Rating
        label="Poziom opanowania"
        initialValue={initialValues.softSkillLevel}
        name="softSkillLevel"
        id="softSkillLevel"
      />
      <Button type="submit">Zapisz</Button>
    </PopUpForm>
  );
}

export { SoftSkillsForm };
