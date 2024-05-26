import { useForeignLanguages } from 'hooks';
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
import { errorToast, successToast } from 'utils';

function ForeignLanguages() {
  const { foreignLanguages, foreignLanguagesIsPending, deleteForeignLanguage } =
    useForeignLanguages();
  const foreignLanguageElements = foreignLanguages?.map((foreignLanguage: ReadForeignLanguage) => (
    <ForeignLanguageCard
      key={foreignLanguage.id}
      name={foreignLanguage.foreignLanguageName}
      flagUrl={foreignLanguage.flagUrl}
      level={foreignLanguage.languageLevel}
    >
      <Button
        onClick={() => {
          try {
            deleteForeignLanguage.mutate(foreignLanguage.id);
            successToast();
          } catch (error) {
            errorToast();
          }
        }}
      >
        Usuń
      </Button>
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
