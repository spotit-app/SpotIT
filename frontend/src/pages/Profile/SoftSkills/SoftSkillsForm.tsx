import { useState } from 'react';
import { FormikHelpers } from 'formik';
import { ReadSoftSkillName, SoftSkillsFormType } from 'types/profile';
import { Button, PopUpForm, Input, Rating, FormikReactSelect } from 'components';
import validationSchema from './SoftSkillsValidation';
import { useSoftSkills } from 'hooks';
import { SelectOption } from 'types/shared';
import { closeModal, errorToast, successToast } from 'utils';

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
    try {
      await createSoftSkill.mutateAsync(values);
      successToast();
    } catch (error) {
      errorToast();
    }
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
      <FormikReactSelect
        label="Nazwa"
        name="softSkillName"
        options={[...(optionsToSelect || []), { value: 'Inna', label: 'Inna' }]}
        customOnChange={(value) => {
          setSelectedSoftSkillName(value as string);
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
