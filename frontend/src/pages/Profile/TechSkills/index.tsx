import { Button, Loading, NoContent, PopUp, ProfileContainer, SkillCard } from 'components';
import { useTechSkills } from 'hooks/useTechSkills';
import { TechSkillsForm } from './TechSkillsForm';
import { ReadTechSkill } from 'types/profile';

function TechSkills() {
  const { techSkills, techSkillsIsPending, deleteTechSkill } = useTechSkills();
  const techSkillElements = techSkills?.map((techSkill: ReadTechSkill) => (
    <SkillCard
      key={techSkill.id}
      name={techSkill.techSkillName}
      level={techSkill.skillLevel}
      logo={techSkill.logoUrl}
    >
      <Button onClick={() => deleteTechSkill.mutate(techSkill.id)}>Usuń</Button>
    </SkillCard>
  ));

  const content =
    techSkillsIsPending || deleteTechSkill.isPending ? (
      <Loading />
    ) : techSkillElements?.length !== 0 ? (
      techSkillElements
    ) : (
      <NoContent />
    );

  return (
    <ProfileContainer title="Umiejętności techniczne" addText="Dodaj umiejętność">
      {content}

      <PopUp title="Dodaj umiejętność">
        <TechSkillsForm />
      </PopUp>
    </ProfileContainer>
  );
}

export default TechSkills;
