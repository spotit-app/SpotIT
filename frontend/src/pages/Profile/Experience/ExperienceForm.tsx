import { ChangeEvent, useEffect, useState } from 'react';
import { FormikHelpers } from 'formik';
import PropTypes from 'prop-types';
import { Button, CheckBox, PopUpForm, Input } from 'components';
import { ReadExperience, WriteExperience } from 'types/profile';
import { useExperiences } from 'hooks';
import validationSchema from './ExperienceValidation';
import { closeModal, successToast, errorToast } from 'utils';

interface ExperienceFormProps {
  experienceToEdit?: ReadExperience;
}

function ExperienceForm({ experienceToEdit }: ExperienceFormProps) {
  const { createExperience, updateExperience } = useExperiences();

  const [isChecked, setIsChecked] = useState(false);

  useEffect(() => {
    if (experienceToEdit) {
      setIsChecked(experienceToEdit.endDate !== null);
    }
  }, [experienceToEdit]);

  const initialValues = {
    companyName: experienceToEdit?.companyName || '',
    position: experienceToEdit?.position || '',
    startDate: experienceToEdit?.startDate || '',
    isChecked: experienceToEdit ? experienceToEdit?.endDate !== null : false,
    endDate: experienceToEdit
      ? experienceToEdit.endDate !== null
        ? experienceToEdit.endDate
        : ''
      : ''
  };

  const onSubmit = async (
    values: WriteExperience,
    { setSubmitting, resetForm }: FormikHelpers<WriteExperience>
  ) => {
    setSubmitting(true);
    try {
      if (experienceToEdit) {
        await updateExperience.mutateAsync({ id: experienceToEdit.id, ...values });
      } else {
        await createExperience.mutateAsync(values);
      }
      successToast();
    } catch (error) {
      errorToast();
    }
    setSubmitting(false);
    resetForm();
    closeModal();
  };

  return (
    <PopUpForm<WriteExperience>
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
      <Button type="submit" disabled={createExperience.isPending || updateExperience.isPending}>
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { ExperienceForm };

ExperienceForm.propTypes = {
  experienceToEdit: PropTypes.object
};
