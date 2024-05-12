import { useState } from 'react';
import { Button, Loading, NoContent, PopUp, ProfileContainer, CourseCard } from 'components';
import { useCourses } from 'hooks';
import { CoursesForm } from './CoursesForm';
import { ReadCourse } from 'types/profile';
import { showModal } from 'utils';

function Courses() {
  const [courseToEdit, setCourseToEdit] = useState<ReadCourse | undefined>(undefined);

  const editCourse = (course: ReadCourse) => {
    setCourseToEdit(course);
    showModal();
  };

  const { courses, coursesIsPending, deleteCourse } = useCourses();

  const courseElements = courses?.map((course: ReadCourse) => (
    <CourseCard key={course.id} {...course}>
      <div className="mr-2">
        <Button onClick={() => editCourse(course)}>Edytuj</Button>
      </div>

      <Button onClick={() => deleteCourse.mutate(course.id)}>Usuń</Button>
    </CourseCard>
  ));

  const content =
    coursesIsPending || deleteCourse.isPending ? (
      <Loading />
    ) : courseElements?.length !== 0 ? (
      courseElements
    ) : (
      <NoContent />
    );

  return (
    <ProfileContainer title="Kursy" addText="Dodaj kurs" onAdd={() => setCourseToEdit(undefined)}>
      {content}

      <PopUp title="Dodaj umiejętność">
        <CoursesForm courseToEdit={courseToEdit} />
      </PopUp>
    </ProfileContainer>
  );
}

export default Courses;
