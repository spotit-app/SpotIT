import { FormikHelpers } from 'formik';
import PropTypes from 'prop-types';
import { ReadInterest, WriteInterest } from 'types/profile';
import { Button, PopUpForm, Input } from 'components';
import validationSchema from './InterestsValidation';
import { useInterests } from 'hooks/useInterests';
import { closeModal } from 'utils';

interface InterestsFormProps {
  interestToEdit?: ReadInterest;
}

function InterestsForm({ interestToEdit }: InterestsFormProps) {
  const { createInterest, updateInterest } = useInterests();

  const initialValues = {
    name: interestToEdit?.name || ''
  };

  const onSubmit = async (
    values: WriteInterest,
    { setSubmitting, resetForm }: FormikHelpers<WriteInterest>
  ) => {
    setSubmitting(true);

    if (interestToEdit) {
      await updateInterest.mutateAsync({ id: interestToEdit.id, ...values });
    } else {
      await createInterest.mutateAsync(values);
    }

    setSubmitting(false);
    resetForm();
    closeModal();
  };

  return (
    <PopUpForm<WriteInterest>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Hobby" name="name" id="interestName" type="text" />
      <Button disabled={createInterest.isPending || updateInterest.isPending} type="submit">
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { InterestsForm };

InterestsForm.propTypes = {
  interestToEdit: PropTypes.object
};
