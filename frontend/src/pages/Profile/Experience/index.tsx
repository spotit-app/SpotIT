import { useState } from 'react';
import { Button, Loading, NoContent, PopUp, ProfileContainer, ExperienceCard } from 'components';
import { useExperiences } from 'hooks';
import { ExperienceForm } from './ExperienceForm';
import { ReadExperience } from 'types/profile';
import { errorToast, showModal, successToast } from 'utils';

function Experience() {
  const [experienceToEdit, setExperienceToEdit] = useState<ReadExperience | undefined>(undefined);

  const editExperience = (experience: ReadExperience) => {
    setExperienceToEdit(experience);
    showModal();
  };

  const { experiences, experiencesIsPending, deleteExperience } = useExperiences();

  const experienceElements = experiences?.map((experience: ReadExperience) => (
    <ExperienceCard key={experience.id} {...experience}>
      <div className="mr-2">
        <Button onClick={() => editExperience(experience)}>Edytuj</Button>
      </div>

      <Button
        onClick={() => {
          try {
            deleteExperience.mutate(experience.id);
            successToast();
          } catch (error) {
            errorToast();
          }
        }}
      >
        Usuń
      </Button>
    </ExperienceCard>
  ));

  const content =
    experiencesIsPending || deleteExperience.isPending ? (
      <Loading />
    ) : experienceElements?.length !== 0 ? (
      experienceElements
    ) : (
      <NoContent />
    );

  return (
    <ProfileContainer
      title="Doświadczenie"
      addText="Dodaj doświadczenie"
      onAdd={() => setExperienceToEdit(undefined)}
    >
      {content}

      <PopUp title="Dodaj doświadczenie">
        <ExperienceForm experienceToEdit={experienceToEdit} />
      </PopUp>
    </ProfileContainer>
  );
}

export default Experience;
