import { FormikHelpers } from 'formik';
import validationSchema from './TechSkillsValidation';
import { Button, Input, Select, Rating, PopUpForm } from '../../../components';
import { ChangeEvent, useState } from 'react';

interface TechSkillsForm {
  techSkillName: string;
  customTechSkillName: string;
  techSkillLevel: number;
}

function TechSkillsForm() {
  const [selectedTechSkillName, setSelectedTechSkillName] = useState('');

  const optionsToSelect = ['Umiejętność1', 'Umiejętność2', 'Inna'];

  const initialValues = {
    techSkillName: '',
    customTechSkillName: '',
    techSkillLevel: 0
  };

  const onSubmit = (values: TechSkillsForm, { setSubmitting }: FormikHelpers<TechSkillsForm>) => {
    setTimeout(() => {
      window.alert(JSON.stringify(values));
      setSubmitting(false);
    }, 400);
  };

  return (
    <PopUpForm<TechSkillsForm>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Select
        label="Nazwa"
        name="techSkillName"
        id="techSkillName"
        placeholder="Wybierz umiejętność"
        options={optionsToSelect}
        customOnChange={(e: ChangeEvent<HTMLSelectElement>) => {
          setSelectedTechSkillName(e.target.value);
        }}
      />
      {selectedTechSkillName === 'Inna' && (
        <Input
          label="Podaj swoją wartość"
          name="customTechSkillName"
          id="customTechSkillName"
          type="text"
          placeholder="Wprowadź inną nazwę"
        />
      )}
      <Rating
        label="Poziom opanowania"
        initialValue={initialValues.techSkillLevel}
        name="techSkillLevel"
        id="techSkillLevel"
      />
      <Button type="submit">Zapisz</Button>
    </PopUpForm>
  );
}

export { TechSkillsForm };
