import { useSoftSkills } from 'hooks';
import { AdminItemCard, Button, Loading, NoContent, PopUp, ProfileContainer } from 'components';
import { ReadSoftSkillName } from 'types/profile';
import { AdminSoftSkillsForm } from './AdminSoftSkillsForm';
import { useState } from 'react';
import { showModal } from 'utils';

function AdminSoftSkills() {
  const [softSkillToEdit, setSoftSkillToEdit] = useState<ReadSoftSkillName | undefined>(undefined);

  const editSoftSkill = (softSkill: ReadSoftSkillName) => {
    setSoftSkillToEdit(softSkill);
    showModal();
  };

  const { softSkillNames, softSkillNamesIsPending } = useSoftSkills();
  const softSkillNameElements = softSkillNames?.map((softSkillName: ReadSoftSkillName) => (
    <AdminItemCard key={softSkillName.id} name={softSkillName.name}>
      <Button onClick={() => editSoftSkill(softSkillName)}>Edytuj</Button>
    </AdminItemCard>
  ));

  const content = softSkillNamesIsPending ? (
    <Loading />
  ) : softSkillNameElements?.length !== 0 ? (
    softSkillNameElements
  ) : (
    <NoContent />
  );

  return (
    <ProfileContainer
      title="Umiejętności miękkie"
      addText="Dodaj umiejętność"
      onAdd={() => setSoftSkillToEdit(undefined)}
    >
      {content}
      <PopUp title="Dodaj umiejętność">
        <AdminSoftSkillsForm softSkillToEdit={softSkillToEdit} />
      </PopUp>
    </ProfileContainer>
  );
}

export default AdminSoftSkills;
