import { FormikHelpers } from 'formik';
import PropTypes from 'prop-types';
import { ReadCourse, WriteCourse } from 'types/profile';
import { Button, PopUpForm, Input } from 'components';
import validationSchema from './CoursesValidation';
import { useCourses } from 'hooks/useCourses';
import { closeModal } from 'utils';

interface CoursesFormProps {
  courseToEdit?: ReadCourse;
}

function CoursesForm({ courseToEdit }: CoursesFormProps) {
  const { createCourse, updateCourse } = useCourses();

  const initialValues = {
    name: courseToEdit?.name || '',
    finishDate: courseToEdit?.finishDate || ''
  };

  const onSubmit = async (
    values: WriteCourse,
    { setSubmitting, resetForm }: FormikHelpers<WriteCourse>
  ) => {
    setSubmitting(true);

    if (courseToEdit) {
      await updateCourse.mutateAsync({ id: courseToEdit.id, ...values });
    } else {
      await createCourse.mutateAsync(values);
    }

    setSubmitting(false);
    resetForm();
    closeModal();
  };

  return (
    <PopUpForm<WriteCourse>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Nazwa kursu" name="name" id="courseName" type="text" />
      <Input label="Data ukończenia" name="finishDate" id="courseEndDate" type="date" />
      <Button type="submit" disabled={createCourse.isPending || updateCourse.isPending}>
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { CoursesForm };

CoursesForm.propTypes = {
  courseToEdit: PropTypes.object
};
