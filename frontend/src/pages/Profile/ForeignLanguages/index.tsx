import { useForeignLanguages } from 'hooks/useForeignLanguages';
import { ForeignLanguageForm } from './ForeignLanguageForm';
import { ReadForeignLanguage } from 'types/profile';
import {
  Button,
  ForeignLanguageCard,
  Loading,
  NoContent,
  PopUp,
  ProfileContainer
} from 'components';

function ForeignLanguages() {
  const { foreignLanguages, foreignLanguagesIsPending, deleteForeignLanguage } =
    useForeignLanguages();
  const foreignLanguageElements = foreignLanguages?.map((foreignLanguage: ReadForeignLanguage) => (
    <ForeignLanguageCard
      key={foreignLanguage.id}
      name={foreignLanguage.foreignLanguageName}
      flagUrl={foreignLanguage.flagUrl}
    >
      <Button onClick={() => deleteForeignLanguage.mutate(foreignLanguage.id)}>Usuń</Button>
    </ForeignLanguageCard>
  ));

  const content =
    foreignLanguagesIsPending || deleteForeignLanguage.isPending ? (
      <Loading />
    ) : foreignLanguageElements?.length ? (
      foreignLanguageElements
    ) : (
      <NoContent />
    );

  return (
    <ProfileContainer title="Języki obce" addText="Dodaj język">
      {content}

      <PopUp title="Dodaj język">
        <ForeignLanguageForm />
      </PopUp>
    </ProfileContainer>
  );
}

export default ForeignLanguages;
