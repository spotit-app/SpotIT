import { useEducations } from 'hooks';
import { ReadEducationLevel } from 'types/profile';
import { AdminItemCard, Button, Loading, NoContent, PopUp, ProfileContainer } from 'components';
import { useState } from 'react';
import { showModal } from 'utils';
import { AdminEducationForm } from './AdminEducationForm';

function AdminEducation() {
  const [educationLevelToEdit, setEducationLevelToEdit] = useState<ReadEducationLevel | undefined>(
    undefined
  );

  const editEducationLevel = (educationLevel: ReadEducationLevel) => {
    setEducationLevelToEdit(educationLevel);
    showModal();
  };

  const { educationLevels, educationLevelsIsPending } = useEducations();
  const educationLevelElements = educationLevels?.map((educationLevel: ReadEducationLevel) => (
    <AdminItemCard key={educationLevel.id} name={educationLevel.name}>
      <Button onClick={() => editEducationLevel(educationLevel)}>Edytuj</Button>
    </AdminItemCard>
  ));

  const content = educationLevelsIsPending ? (
    <Loading />
  ) : educationLevelElements?.length !== 0 ? (
    educationLevelElements
  ) : (
    <NoContent />
  );

  return (
    <ProfileContainer
      title="Poziomy wykształcenia"
      addText="Dodaj poziom wykształcenia"
      onAdd={() => setEducationLevelToEdit(undefined)}
    >
      {content}
      <PopUp title="Dodaj poziom wykształcenia">
        <AdminEducationForm educationLevelToEdit={educationLevelToEdit} />
      </PopUp>
    </ProfileContainer>
  );
}

export default AdminEducation;
