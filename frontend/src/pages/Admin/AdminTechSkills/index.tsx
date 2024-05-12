import { useTechSkills } from 'hooks';
import { AdminItemCard, Button, Loading, NoContent, PopUp, ProfileContainer } from 'components';
import { ReadTechSkillName } from 'types/profile';
import { AdminTechSkillsForm } from './AdminTechSkillsForm';
import { useState } from 'react';
import { showModal } from 'utils';

function AdminTechSkills() {
  const [techSkillToEdit, setTechSkillToEdit] = useState<ReadTechSkillName | undefined>(undefined);

  const editTechSkill = (techSkill: ReadTechSkillName) => {
    setTechSkillToEdit(techSkill);
    showModal();
  };

  const { techSkillNames, techSkillNamesIsPending } = useTechSkills();
  const techSkillNameElements = techSkillNames?.map((techSkillName: ReadTechSkillName) => (
    <AdminItemCard key={techSkillName.id} name={techSkillName.name} logo={techSkillName.logoUrl}>
      <Button onClick={() => editTechSkill(techSkillName)}>Edytuj</Button>
    </AdminItemCard>
  ));

  const content = techSkillNamesIsPending ? (
    <Loading />
  ) : techSkillNameElements?.length !== 0 ? (
    techSkillNameElements
  ) : (
    <NoContent />
  );

  return (
    <ProfileContainer
      title="Umiejętności techniczne"
      addText="Dodaj umiejętność"
      onAdd={() => setTechSkillToEdit(undefined)}
    >
      {content}
      <PopUp title="Dodaj umiejętność">
        <AdminTechSkillsForm techSkillToEdit={techSkillToEdit} />
      </PopUp>
    </ProfileContainer>
  );
}

export default AdminTechSkills;
