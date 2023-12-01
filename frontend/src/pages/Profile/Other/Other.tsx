import { FormikHelpers } from 'formik';
import validationSchema from './OtherValidation';
import { TextArea, Button, MainForm } from '../../../components';

interface OtherFormTypes {
  description: string;
  cvClause: string;
}

function Other() {
  const initialValues = {
    description: '',
    cvClause: ''
  };

  const onSubmit = (values: OtherFormTypes, { setSubmitting }: FormikHelpers<OtherFormTypes>) => {
    setTimeout(() => {
      window.alert(JSON.stringify(values));
      setSubmitting(false);
    }, 400);
  };

  return (
    <MainForm<OtherFormTypes>
      title="Inne"
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <TextArea label="Opis" name="otherDescription" id="otherDescription" rows={4} />
      <TextArea label="Klauzula informacyjna" name="cvClause" id="cvClause" rows={4} />
      <Button type="submit">Zapisz</Button>
    </MainForm>
  );
}

export { Other };
