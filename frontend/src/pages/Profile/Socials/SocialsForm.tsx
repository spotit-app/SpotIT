import { FormikHelpers } from 'formik';
import PropTypes from 'prop-types';
import { ReadSocial, WriteSocial } from 'types/profile';
import { Button, PopUpForm, Input } from 'components';
import validationSchema from './SocialsValidation';
import { useSocials } from 'hooks';
import { closeModal } from 'utils';

interface SocialsFormProps {
  socialToEdit?: ReadSocial;
}

function SocialsForm({ socialToEdit }: SocialsFormProps) {
  const { createSocial, updateSocial } = useSocials();

  const initialValues: WriteSocial = {
    name: socialToEdit?.name || '',
    socialUrl: socialToEdit?.socialUrl || ''
  };

  const onSubmit = async (
    values: WriteSocial,
    { setSubmitting, resetForm }: FormikHelpers<WriteSocial>
  ) => {
    setSubmitting(true);

    if (socialToEdit) {
      await updateSocial.mutateAsync({ id: socialToEdit.id, ...values });
    } else {
      await createSocial.mutateAsync(values);
    }

    setSubmitting(false);
    resetForm();
    closeModal();
  };

  return (
    <PopUpForm<WriteSocial>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Nazwa konta społecznościowego" name="name" id="socialsName" type="text" />
      <Input label="Link do konta" name="socialUrl" id="socialsLink" type="text" />
      <Button disabled={createSocial.isPending || updateSocial.isPending} type="submit">
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { SocialsForm };

SocialsForm.propTypes = {
  socialToEdit: PropTypes.object
};
