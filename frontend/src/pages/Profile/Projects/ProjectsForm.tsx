import { FormikHelpers } from 'formik';
import validationSchema from './ProjectsValidation';
import { Button, PopUpForm, Input, TextArea } from '../../../components';

interface ProjectsFormTypes {
  projectName: string;
  description: string;
  projectUrl: string;
}

function ProjectsForm() {
  const initialValues = {
    projectName: '',
    description: '',
    projectUrl: ''
  };

  const onSubmit = (
    values: ProjectsFormTypes,
    { setSubmitting }: FormikHelpers<ProjectsFormTypes>
  ) => {
    setTimeout(() => {
      window.alert(JSON.stringify(values));
      setSubmitting(false);
    }, 400);
  };

  return (
    <PopUpForm<ProjectsFormTypes>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Nazwa projektu" name="projectName" id="projectName" type="text" />
      <TextArea label="Opis" name="description" id="description" rows={4} />
      <Input label="Link do projektu" name="projectUrl" id="projectUrl" type="text" />
      <Button type="submit">Zapisz</Button>
    </PopUpForm>
  );
}

export { ProjectsForm };
