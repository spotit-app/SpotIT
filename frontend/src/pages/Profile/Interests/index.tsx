import { useState } from 'react';
import { Button, InterestCard, Loading, NoContent, PopUp, ProfileContainer } from 'components';
import { useInterests } from 'hooks';
import { InterestsForm } from './InterestsForm';
import { ReadInterest } from 'types/profile';
import { showModal } from 'utils';

function Interests() {
  const [interestToEdit, setInterestToEdit] = useState<ReadInterest | undefined>(undefined);

  const editInterest = (interest: ReadInterest) => {
    setInterestToEdit(interest);
    showModal();
  };

  const { interests, interestsIsPending, deleteInterest } = useInterests();

  const interestElements = interests?.map((interest: ReadInterest) => (
    <InterestCard key={interest.id} name={interest.name}>
      <div className="mr-2">
        <Button onClick={() => editInterest(interest)}>Edytuj</Button>
      </div>

      <Button onClick={() => deleteInterest.mutate(interest.id)}>Usuń</Button>
    </InterestCard>
  ));

  const content =
    interestsIsPending || deleteInterest.isPending ? (
      <Loading />
    ) : interestElements?.length !== 0 ? (
      interestElements
    ) : (
      <NoContent />
    );

  return (
    <ProfileContainer
      title="Zainteresowania"
      addText="Dodaj zainteresowanie"
      onAdd={() => setInterestToEdit(undefined)}
    >
      {content}

      <PopUp title="Dodaj zainteresowanie">
        <InterestsForm interestToEdit={interestToEdit} />
      </PopUp>
    </ProfileContainer>
  );
}

export default Interests;
