import { SkillCard, Loading, Button, NoContent, PopUp, ProfileContainer } from 'components';
import { useSoftSkills } from 'hooks';
import { SoftSkillsForm } from './SoftSkillsForm';
import { ReadSoftSkill } from 'types/profile';

function SoftSkills() {
  const { softSkills, softSkillsIsPending, deleteSoftSkill } = useSoftSkills();
  const softSkillElements = softSkills?.map((softSkill: ReadSoftSkill) => (
    <SkillCard key={softSkill.id} name={softSkill.softSkillName} level={softSkill.skillLevel}>
      <Button onClick={() => deleteSoftSkill.mutate(softSkill.id)}>Usuń</Button>
    </SkillCard>
  ));

  const content =
    softSkillsIsPending || deleteSoftSkill.isPending ? (
      <Loading />
    ) : softSkillElements?.length !== 0 ? (
      softSkillElements
    ) : (
      <NoContent />
    );

  return (
    <ProfileContainer title="Umiejętności miękkie" addText="Dodaj umiejętność">
      {content}

      <PopUp title="Dodaj umiejętność">
        <SoftSkillsForm />
      </PopUp>
    </ProfileContainer>
  );
}

export default SoftSkills;
