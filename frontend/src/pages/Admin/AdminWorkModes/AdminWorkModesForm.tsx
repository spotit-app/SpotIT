import { closeModal, successToast, errorToast } from 'utils';
import { useWorkModes } from 'hooks';
import { Button, Input, PopUpForm } from 'components';
import adminWorkModesValidationSchema from './AdminWorkModesValidation';
import { ReadWorkMode, WriteWorkMode } from 'types/company';
import { FormikHelpers } from 'formik';
import PropTypes from 'prop-types';

interface AdminWorkModesFormProps {
  workModeToEdit?: ReadWorkMode;
}

function AdminWorkModesForm({ workModeToEdit }: AdminWorkModesFormProps) {
  const { createWorkMode, updateWorkMode } = useWorkModes();

  const initialValues: WriteWorkMode = {
    name: workModeToEdit?.name || ''
  };

  const onSubmit = async (
    values: WriteWorkMode,
    { setSubmitting, resetForm }: FormikHelpers<WriteWorkMode>
  ) => {
    setSubmitting(true);
    try {
      if (workModeToEdit) {
        await updateWorkMode.mutateAsync({ id: workModeToEdit.id, ...values });
      } else {
        await createWorkMode.mutateAsync(values);
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
    <PopUpForm<WriteWorkMode>
      initialValues={initialValues}
      validationSchema={adminWorkModesValidationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Nazwa" name="name" id="name" />
      <Button type="submit" disabled={createWorkMode.isPending || updateWorkMode.isPending}>
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { AdminWorkModesForm };

AdminWorkModesForm.propTypes = {
  workModeToEdit: PropTypes.object
};
