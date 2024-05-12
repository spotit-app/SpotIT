import { AdminItemCard, Button, Loading, NoContent, PopUp, ProfileContainer } from 'components';
import { ReadWorkMode } from 'types/company';
import { AdminWorkModesForm } from './AdminWorkModesForm';
import { useState } from 'react';
import { showModal } from 'utils';
import { useWorkModes } from 'hooks';

function AdminWorkModes() {
  const [workModeToEdit, setWorkModeToEdit] = useState<ReadWorkMode | undefined>(undefined);

  const editWorkMode = (workMode: ReadWorkMode) => {
    setWorkModeToEdit(workMode);
    showModal();
  };

  const { workModes, workModesIsPending } = useWorkModes();
  const workModeElements = workModes?.map((workMode: ReadWorkMode) => (
    <AdminItemCard key={workMode.id} name={workMode.name}>
      <Button onClick={() => editWorkMode(workMode)}>Edytuj</Button>
    </AdminItemCard>
  ));

  const content = workModesIsPending ? (
    <Loading />
  ) : workModeElements?.length !== 0 ? (
    workModeElements
  ) : (
    <NoContent />
  );

  return (
    <ProfileContainer
      title="Tryby pracy"
      addText="Dodaj tryb pracy"
      onAdd={() => setWorkModeToEdit(undefined)}
    >
      {content}
      <PopUp title="Dodaj tryb pracy">
        <AdminWorkModesForm workModeToEdit={workModeToEdit} />
      </PopUp>
    </ProfileContainer>
  );
}

export default AdminWorkModes;
