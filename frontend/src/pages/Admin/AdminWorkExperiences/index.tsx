import { AdminItemCard, Button, Loading, NoContent, PopUp, ProfileContainer } from 'components';
import { ReadWorkExperience } from 'types/company';
import { AdminWorkExperiencesForm } from './AdminWorkExperiencesForm';
import { useState } from 'react';
import { showModal } from 'utils';
import { useWorkExperiences } from 'hooks';

function AdminWorkExperiences() {
  const [workExperienceToEdit, setWorkExperienceToEdit] = useState<ReadWorkExperience | undefined>(
    undefined
  );

  const editWorkExperience = (workExperience: ReadWorkExperience) => {
    setWorkExperienceToEdit(workExperience);
    showModal();
  };

  const { workExperiences, workExperiencesIsPending } = useWorkExperiences();
  const workExperienceElements = workExperiences?.map((workExperience: ReadWorkExperience) => (
    <AdminItemCard key={workExperience.id} name={workExperience.name}>
      <Button onClick={() => editWorkExperience(workExperience)}>Edytuj</Button>
    </AdminItemCard>
  ));

  const content = workExperiencesIsPending ? (
    <Loading />
  ) : workExperienceElements?.length !== 0 ? (
    workExperienceElements
  ) : (
    <NoContent />
  );

  return (
    <ProfileContainer
      title="Poziom doświadczenia"
      addText="Dodaj poziom doświadczenia"
      onAdd={() => setWorkExperienceToEdit(undefined)}
    >
      {content}
      <PopUp title="Dodaj poziom doświadczenia">
        <AdminWorkExperiencesForm workExperienceToEdit={workExperienceToEdit} />
      </PopUp>
    </ProfileContainer>
  );
}

export default AdminWorkExperiences;
