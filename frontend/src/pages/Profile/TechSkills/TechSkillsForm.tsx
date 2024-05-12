import { useState } from 'react';
import { FormikHelpers } from 'formik';
import { ReadTechSkillName, TechSkillsFormType } from 'types/profile';
import { Button, Input, Rating, PopUpForm, FormikReactSelect } from 'components';
import validationSchema from './TechSkillsValidation';
import { useTechSkills } from 'hooks';
import { SelectOption } from 'types/shared';
import { closeModal } from 'utils';

function TechSkillsForm() {
  const [selectedTechSkillName, setSelectedTechSkillName] = useState('');

  const { techSkillNames, createTechSkill } = useTechSkills();

  const optionsToSelect: SelectOption[] | undefined = techSkillNames?.map(
    (techSkillName: ReadTechSkillName) => ({
      value: techSkillName.name,
      label: techSkillName.name
    })
  );

  const initialValues = {
    techSkillName: '',
    customTechSkillName: '',
    techSkillLevel: 0
  };

  const onSubmit = async (
    values: TechSkillsFormType,
    { setSubmitting, resetForm }: FormikHelpers<TechSkillsFormType>
  ) => {
    setSubmitting(true);
    await createTechSkill.mutateAsync(values);
    setSubmitting(false);
    resetForm();
    setSelectedTechSkillName('');
    closeModal();
  };

  return (
    <PopUpForm<TechSkillsFormType>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <FormikReactSelect
        label="Nazwa"
        name="techSkillName"
        options={[...(optionsToSelect || []), { value: 'Inna', label: 'Inna' }]}
        customOnChange={(value) => {
          setSelectedTechSkillName(value as string);
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
      <Button disabled={createTechSkill.isPending} type="submit">
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { TechSkillsForm };
