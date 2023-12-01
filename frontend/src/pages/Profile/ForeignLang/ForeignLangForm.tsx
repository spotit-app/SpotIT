import { FormikHelpers } from 'formik';
import validationSchema from './ForeignLangValidation';
import { Button, PopUpForm, Select } from '../../../components';

interface ForeignLangFormType {
  langName: string;
  langLevel: string;
}

function ForeignLangForm() {
  const langToSelect = ['Język1', 'Język2'];
  const levelToSelect = ['A1', 'C2'];

  const initialValues = {
    langName: '',
    langLevel: ''
  };

  const onSubmit = (
    values: ForeignLangFormType,
    { setSubmitting }: FormikHelpers<ForeignLangFormType>
  ) => {
    setTimeout(() => {
      window.alert(JSON.stringify(values));
      setSubmitting(false);
    }, 400);
  };

  return (
    <PopUpForm<ForeignLangFormType>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Select
        label="Nazwa języka"
        name="langName"
        id="langName"
        placeholder="Wybierz język:"
        options={langToSelect}
      />
      <Select
        label="Poziom"
        name="langLevel"
        id="langLevel"
        placeholder="Wybierz poziom:"
        options={levelToSelect}
      />
      <Button type="submit">Zapisz</Button>
    </PopUpForm>
  );
}

export { ForeignLangForm };
