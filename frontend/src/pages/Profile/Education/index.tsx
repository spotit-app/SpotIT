import { Loading, NoContent, PopUp, ProfileContainer, EducationCard, Button } from 'components';
import { useEducations } from 'hooks';
import { EducationForm } from './EducationForm';
import { ReadEducation } from 'types/profile';
import { errorToast, successToast } from 'utils';

function Education() {
  const { educations, educationsIsPending, deleteEducation } = useEducations();

  const educationElements = educations?.map((education: ReadEducation) => (
    <EducationCard key={education.id} {...education}>
      <Button
        onClick={() => {
          try {
            deleteEducation.mutate(education.id);
            successToast();
          } catch (error) {
            errorToast();
          }
        }}
      >
        Usuń
      </Button>
    </EducationCard>
  ));

  const content =
    educationsIsPending || deleteEducation.isPending ? (
      <Loading />
    ) : educationElements?.length !== 0 ? (
      educationElements
    ) : (
      <NoContent />
    );

  return (
    <ProfileContainer title="Edukacja" addText="Dodaj edukację">
      {content}

      <PopUp title="Dodaj edukację">
        <EducationForm />
      </PopUp>
    </ProfileContainer>
  );
}

export default Education;
