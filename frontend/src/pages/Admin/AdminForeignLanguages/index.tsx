import { useForeignLanguages } from 'hooks';
import { AdminItemCard, Button, Loading, NoContent, PopUp, ProfileContainer } from 'components';
import { ReadForeignLanguageName } from 'types/profile';
import { AdminForeignLanguagesForm } from './AdminForeignLanguagesForm';
import { useState } from 'react';
import { showModal } from 'utils';

function AdminForeignLanguages() {
  const [foreignLanguageToEdit, setForeignLanguageToEdit] = useState<
    ReadForeignLanguageName | undefined
  >(undefined);

  const editForeignLanguage = (foreignLanguage: ReadForeignLanguageName) => {
    setForeignLanguageToEdit(foreignLanguage);
    showModal();
  };

  const { foreignLanguageNames, foreignLanguageNamesIsPending } = useForeignLanguages();
  const foreignLanguageNameElements = foreignLanguageNames?.map(
    (foreignLanguageName: ReadForeignLanguageName) => (
      <AdminItemCard
        key={foreignLanguageName.id}
        name={foreignLanguageName.name}
        logo={foreignLanguageName.flagUrl}
      >
        <Button onClick={() => editForeignLanguage(foreignLanguageName)}>Edytuj</Button>
      </AdminItemCard>
    )
  );

  const content = foreignLanguageNamesIsPending ? (
    <Loading />
  ) : foreignLanguageNameElements?.length !== 0 ? (
    foreignLanguageNameElements
  ) : (
    <NoContent />
  );

  return (
    <ProfileContainer
      title="Języki obce"
      addText="Dodaj język"
      onAdd={() => setForeignLanguageToEdit(undefined)}
    >
      {content}
      <PopUp title="Dodaj język">
        <AdminForeignLanguagesForm foreignLanguageToEdit={foreignLanguageToEdit} />
      </PopUp>
    </ProfileContainer>
  );
}

export default AdminForeignLanguages;
