import { FormikHelpers } from 'formik';
import PropTypes from 'prop-types';
import { Button, PopUpForm, Input, TextArea } from 'components';
import { ReadProject, WriteProject } from 'types/profile';
import validationSchema from './ProjectsValidation';
import { useProjects } from 'hooks';
import { closeModal, successToast, errorToast } from 'utils';

interface ProjectsFormProps {
  projectToEdit?: ReadProject;
}

function ProjectsForm({ projectToEdit }: ProjectsFormProps) {
  const { createProject, updateProject } = useProjects();

  const initialValues = {
    name: projectToEdit?.name || '',
    description: projectToEdit?.description || '',
    projectUrl: projectToEdit?.projectUrl || ''
  };

  const onSubmit = async (
    values: WriteProject,
    { setSubmitting, resetForm }: FormikHelpers<WriteProject>
  ) => {
    setSubmitting(true);
    try {
      if (projectToEdit) {
        await updateProject.mutateAsync({ id: projectToEdit.id, ...values });
      } else {
        await createProject.mutateAsync(values);
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
    <PopUpForm<WriteProject>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Nazwa projektu" name="name" id="projectName" type="text" />
      <TextArea label="Opis" name="description" id="description" rows={4} />
      <Input label="Link do projektu" name="projectUrl" id="projectUrl" type="text" />
      <Button type="submit" disabled={createProject.isPending || updateProject.isPending}>
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { ProjectsForm };

ProjectsForm.propTypes = {
  projectToEdit: PropTypes.object
};
