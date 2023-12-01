import { FormikHelpers } from 'formik';
import validationSchema from './InterestsValidation';
import { Button, PopUpForm, Input } from '../../../components';

interface InterestsFormType {
  interestName: string;
}

function InterestsForm() {
  const initialValues = {
    interestName: ''
  };

  const onSubmit = (
    values: InterestsFormType,
    { setSubmitting }: FormikHelpers<InterestsFormType>
  ) => {
    setTimeout(() => {
      window.alert(JSON.stringify(values));
      setSubmitting(false);
    }, 400);
  };

  return (
    <PopUpForm<InterestsFormType>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Hobby" name="interestName" id="interestName" type="text" />
      <Button type="submit">Zapisz</Button>
    </PopUpForm>
  );
}

export { InterestsForm };
