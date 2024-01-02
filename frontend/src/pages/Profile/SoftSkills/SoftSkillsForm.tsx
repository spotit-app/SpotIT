import { ChangeEvent, useState } from 'react';
import { FormikHelpers } from 'formik';
import { ReadSoftSkillName, SoftSkillsFormType } from 'types/profile';
import { Button, PopUpForm, Input, Rating, Select } from 'components';
import validationSchema from './SoftSkillsValidation';
import { useSoftSkills } from 'hooks/useSoftSkills';
import { SelectOption } from 'types/shared';
import { closeModal } from 'utils';

function SoftSkillsForm() {
  const [selectedSoftSkillName, setSelectedSoftSkillName] = useState('');

  const { softSkillNames, createSoftSkill } = useSoftSkills();

  const optionsToSelect: SelectOption[] | undefined = softSkillNames?.map(
    (softSkillName: ReadSoftSkillName) => ({
      value: softSkillName.name,
      label: softSkillName.name
    })
  );

  const initialValues = {
    softSkillName: '',
    customSoftSkillName: '',
    softSkillLevel: 0
  };

  const onSubmit = async (
    values: SoftSkillsFormType,
    { setSubmitting, resetForm }: FormikHelpers<SoftSkillsFormType>
  ) => {
    setSubmitting(true);
    await createSoftSkill.mutateAsync(values);
    setSubmitting(false);
    resetForm();
    setSelectedSoftSkillName('');
    closeModal();
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
        data-testid="softSkillName-select"
        options={[...(optionsToSelect || []), { value: 'Inna', label: 'Inna' }]}
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
      <Button disabled={createSoftSkill.isPending} type="submit">
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { SoftSkillsForm };
