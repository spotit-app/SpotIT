import { closeModal, successToast, errorToast } from 'utils';
import { useEducations } from 'hooks';
import { Button, Input, PopUpForm } from 'components';
import adminEducationValidationSchema from './AdminEducationValidation';
import { ReadEducationLevel, EducationLevelFormType, WriteEducationLevel } from 'types/profile';
import { FormikHelpers } from 'formik';
import PropTypes from 'prop-types';

interface AdminEducationFormProps {
  educationLevelToEdit?: ReadEducationLevel;
}

function AdminEducationForm({ educationLevelToEdit }: AdminEducationFormProps) {
  const { createEducationLevel, updateEducationLevel } = useEducations();

  const initialValues: WriteEducationLevel = {
    name: educationLevelToEdit?.name || ''
  };

  const onSubmit = async (
    values: EducationLevelFormType,
    { setSubmitting, resetForm }: FormikHelpers<EducationLevelFormType>
  ) => {
    setSubmitting(true);
    try {
      if (educationLevelToEdit) {
        await updateEducationLevel.mutateAsync({ id: educationLevelToEdit.id, ...values });
      } else {
        await createEducationLevel.mutateAsync(values);
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
    <PopUpForm<EducationLevelFormType>
      initialValues={initialValues}
      validationSchema={adminEducationValidationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Nazwa" name="name" id="name" />
      <Button
        type="submit"
        disabled={createEducationLevel.isPending || updateEducationLevel.isPending}
      >
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { AdminEducationForm };

AdminEducationForm.propTypes = {
  educationLevelToEdit: PropTypes.object
};
