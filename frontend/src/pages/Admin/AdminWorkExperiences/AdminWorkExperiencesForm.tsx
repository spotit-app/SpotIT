import { closeModal, successToast, errorToast } from 'utils';
import { useWorkExperiences } from 'hooks';
import { Button, Input, PopUpForm } from 'components';
import adminWorkExperiencesValidationSchema from './AdminWorkExperiencesValidation';
import { ReadWorkExperience, WriteWorkExperience } from 'types/company';
import { FormikHelpers } from 'formik';
import PropTypes from 'prop-types';

interface AdminWorkExperiencesFormProps {
  workExperienceToEdit?: ReadWorkExperience;
}

function AdminWorkExperiencesForm({ workExperienceToEdit }: AdminWorkExperiencesFormProps) {
  const { createWorkExperience, updateWorkExperience } = useWorkExperiences();

  const initialValues: WriteWorkExperience = {
    name: workExperienceToEdit?.name || ''
  };

  const onSubmit = async (
    values: WriteWorkExperience,
    { setSubmitting, resetForm }: FormikHelpers<WriteWorkExperience>
  ) => {
    setSubmitting(true);
    try {
      if (workExperienceToEdit) {
        await updateWorkExperience.mutateAsync({ id: workExperienceToEdit.id, ...values });
      } else {
        await createWorkExperience.mutateAsync(values);
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
    <PopUpForm<WriteWorkExperience>
      initialValues={initialValues}
      validationSchema={adminWorkExperiencesValidationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Nazwa" name="name" id="name" />
      <Button
        type="submit"
        disabled={createWorkExperience.isPending || updateWorkExperience.isPending}
      >
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { AdminWorkExperiencesForm };

AdminWorkExperiencesForm.propTypes = {
  workExperienceToEdit: PropTypes.object
};
