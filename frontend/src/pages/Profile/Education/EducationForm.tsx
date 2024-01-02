import { ChangeEvent, useState } from 'react';
import { FormikHelpers } from 'formik';
import { Input, Select, Button, CheckBox, PopUpForm } from 'components';
import { EducationFormType, ReadEducationLevel } from 'types/profile';
import validationSchema from './EducationValidation';
import { useEducations } from 'hooks/useEducations';
import { SelectOption } from 'types/shared';
import { closeModal } from 'utils';

function EducationForm() {
  const [selectedEducationLevel, setSelectedEducationLevel] = useState('');
  const [isChecked, setIsChecked] = useState(false);

  const { educationLevels, createEducation } = useEducations();
  const educationLevelsToSelect: string[] | undefined = educationLevels?.map(
    (educationLevel: ReadEducationLevel) => educationLevel.name
  );

  const optionsToSelect: SelectOption[] = [...(educationLevelsToSelect || []), 'Inny'].map(
    (option: string) => {
      return {
        value: option,
        label: option
      };
    }
  );

  const initialValues = {
    educationLevel: '',
    customEducationLevel: '',
    schoolName: '',
    faculty: '',
    startDate: '',
    isChecked: false,
    endDate: ''
  };

  const onSubmit = async (
    values: EducationFormType,
    { setSubmitting, resetForm }: FormikHelpers<EducationFormType>
  ) => {
    setSubmitting(true);
    await createEducation.mutateAsync(values);
    setSubmitting(false);
    resetForm();
    setSelectedEducationLevel('');
    setIsChecked(false);
    closeModal();
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
