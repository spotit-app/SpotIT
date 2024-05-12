import { closeModal } from 'utils';
import { useSoftSkills } from 'hooks';
import { Button, Input, PopUpForm } from 'components';
import adminSoftSkillsValidationSchema from './AdminSoftSkillsValidation';
import { ReadSoftSkillName, SoftSkillNamesFormType, WriteSoftSkillName } from 'types/profile';
import { FormikHelpers } from 'formik';
import PropTypes from 'prop-types';

interface AdminSoftSkillsFormProps {
  softSkillToEdit?: ReadSoftSkillName;
}

function AdminSoftSkillsForm({ softSkillToEdit }: AdminSoftSkillsFormProps) {
  const { createSoftSkillName, updateSoftSkillName } = useSoftSkills();

  const initialValues: WriteSoftSkillName = {
    name: softSkillToEdit?.name || ''
  };

  const onSubmit = async (
    values: SoftSkillNamesFormType,
    { setSubmitting, resetForm }: FormikHelpers<SoftSkillNamesFormType>
  ) => {
    setSubmitting(true);
    if (softSkillToEdit) {
      await updateSoftSkillName.mutateAsync({ id: softSkillToEdit.id, ...values });
    } else {
      await createSoftSkillName.mutateAsync(values);
    }

    setSubmitting(false);
    resetForm();
    closeModal();
  };

  return (
    <PopUpForm<SoftSkillNamesFormType>
      initialValues={initialValues}
      validationSchema={adminSoftSkillsValidationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Nazwa" name="name" id="name" />
      <Button
        type="submit"
        disabled={createSoftSkillName.isPending || updateSoftSkillName.isPending}
      >
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { AdminSoftSkillsForm };

AdminSoftSkillsForm.propTypes = {
  softSkillToEdit: PropTypes.object
};
