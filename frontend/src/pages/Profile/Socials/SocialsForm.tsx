import { FormikHelpers } from 'formik';
import validationSchema from './SocialsValidation';
import { Button, PopUpForm, Input } from '../../../components';

interface SocialsFormType {
  socialsName: string;
  socialsLink: string;
}

function SocialsForm() {
  const initialValues = {
    socialsName: '',
    socialsLink: ''
  };

  const onSubmit = (values: SocialsFormType, { setSubmitting }: FormikHelpers<SocialsFormType>) => {
    setTimeout(() => {
      window.alert(JSON.stringify(values));
      setSubmitting(false);
    }, 400);
  };

  return (
    <PopUpForm<SocialsFormType>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Input
        label="Nazwa konta społecznościowego"
        name="socialsName"
        id="socialsName"
        type="text"
      />
      <Input label="Link do konta" name="socialsLink" id="socialsLink" type="text" />
      <Button type="submit">Zapisz</Button>
    </PopUpForm>
  );
}

export { SocialsForm };
