import { FormikHelpers } from 'formik';
import validationSchema from './CoursesValidation';
import { Button, PopUpForm, Input } from '../../../components';

interface CoursesFormType {
  courseName: string;
  courseEndDate: string;
}

function CoursesForm() {
  const initialValues = {
    courseName: '',
    courseEndDate: ''
  };

  const onSubmit = (values: CoursesFormType, { setSubmitting }: FormikHelpers<CoursesFormType>) => {
    setTimeout(() => {
      window.alert(JSON.stringify(values));
      setSubmitting(false);
    }, 400);
  };

  return (
    <PopUpForm<CoursesFormType>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Nazwa kursu" name="courseName" id="courseName" type="text" />
      <Input label="Data ukończenia" name="courseEndDate" id="courseEndDate" type="date" />
      <Button type="submit">Zapisz</Button>
    </PopUpForm>
  );
}

export { CoursesForm };
