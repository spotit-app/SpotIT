import { useState } from 'react';
import { Loading, NoContent, PopUp, ProfileContainer, SocialCard, Button } from 'components';
import { useSocials } from 'hooks';
import { SocialsForm } from './SocialsForm';
import { ReadSocial } from 'types/profile';
import { showModal } from 'utils';

function Socials() {
  const [socialToEdit, setSocialToEdit] = useState<ReadSocial | undefined>(undefined);

  const editSocial = (social: ReadSocial) => {
    setSocialToEdit(social);
    showModal();
  };

  const { socials, socialsIsPending, deleteSocial } = useSocials();
  const socialElements = socials?.map((social: ReadSocial) => (
    <SocialCard key={social.id} name={social.name} socialUrl={social.socialUrl}>
      <div className="mr-2">
        <Button onClick={() => editSocial(social)}>Edytuj</Button>
      </div>

      <Button onClick={() => deleteSocial.mutate(social.id)}>Usuń</Button>
    </SocialCard>
  ));

  const content =
    socialsIsPending || deleteSocial.isPending ? (
      <Loading />
    ) : socialElements?.length !== 0 ? (
      socialElements
    ) : (
      <NoContent />
    );

  return (
    <ProfileContainer
      title="Konta społecznościowe"
      addText="Dodaj konto"
      onAdd={() => setSocialToEdit(undefined)}
    >
      {content}

      <PopUp title="Dodaj konto">
        <SocialsForm socialToEdit={socialToEdit} />
      </PopUp>
    </ProfileContainer>
  );
}

export default Socials;
