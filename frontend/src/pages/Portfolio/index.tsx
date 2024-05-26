import {
  CourseCard,
  EducationCard,
  ExperienceCard,
  ForeignLanguageCard,
  InterestCard,
  Loading,
  ProjectCard,
  SkillCard,
  SocialCard
} from 'components';
import { usePortfolioPage } from 'hooks';
import {
  ReadCourse,
  ReadEducation,
  ReadExperience,
  ReadForeignLanguage,
  ReadInterest,
  ReadProject,
  ReadSocial,
  ReadSoftSkill,
  ReadTechSkill
} from 'types/profile';
import { useParams } from 'react-router-dom';

function Portfolio() {
  const { portfolioUrl } = useParams();
  const { portfolio, portfolioIsPending } = usePortfolioPage(portfolioUrl!);

  const socialCards = portfolio?.socials?.map((social: ReadSocial) => (
    <SocialCard key={social.id} {...social} />
  ));

  const educationCards = portfolio?.educations?.map((education: ReadEducation) => (
    <EducationCard key={education.id} {...education} />
  ));

  const experienceCards = portfolio?.experiences?.map((experience: ReadExperience) => (
    <ExperienceCard key={experience.id} {...experience} />
  ));

  const techSkillCards = portfolio?.techSkills?.map((techSkill: ReadTechSkill) => (
    <SkillCard
      key={techSkill.id}
      name={techSkill.techSkillName}
      level={techSkill.skillLevel}
      logo={techSkill.logoUrl}
    />
  ));

  const softSkillCards = portfolio?.softSkills?.map((softSkill: ReadSoftSkill) => (
    <SkillCard key={softSkill.id} name={softSkill.softSkillName} level={softSkill.skillLevel} />
  ));

  const foreignLanguageCards = portfolio?.foreignLanguages?.map(
    (foreignLanguage: ReadForeignLanguage) => (
      <ForeignLanguageCard
        key={foreignLanguage.id}
        name={foreignLanguage.foreignLanguageName}
        flagUrl={foreignLanguage.flagUrl}
        level={foreignLanguage.languageLevel}
      />
    )
  );

  const projectCards = portfolio?.projects?.map((project: ReadProject) => (
    <ProjectCard key={project.id} {...project} />
  ));

  const interestCards = portfolio?.interests?.map((interest: ReadInterest) => (
    <InterestCard key={interest.id} {...interest} />
  ));

  const courseCards = portfolio?.courses?.map((course: ReadCourse) => (
    <CourseCard key={course.id} {...course} />
  ));

  return portfolioIsPending ? (
    <Loading />
  ) : (
    <>
      <header className="flex justify-around items-center bg-base-200 flex-wrap p-3">
        <div className="avatar">
          <div className="w-40 rounded-full border-b-4 border-l-4 border-primary">
            <img referrerPolicy="no-referrer" src={portfolio?.userData.profilePictureUrl} />
          </div>
        </div>
        <div>
          <div className="text-4xl flex justify-center my-3 uppercase font-bold flex-wrap">
            <h1 className="mr-4 text-primary">{portfolio?.userData.firstName}</h1>
            <h1>{portfolio?.userData.lastName}</h1>
          </div>
          <h3 className="text-center font-bold text-xl mb-2">{portfolio?.userData.position}</h3>
        </div>
      </header>
      <div className="p-2 lg:p-5 my-3">
        {portfolio?.userData.description && (
          <div className="my-10">
            <h1 className="text-3xl font-bold mb-3 break-words">O mnie</h1>
            <div className="text-base border-b-2 border-l-2 border-primary p-3">
              {portfolio?.userData.description}
            </div>
          </div>
        )}
        {(portfolio?.userData.email || portfolio?.userData.phoneNumber) && (
          <div className="my-10">
            <h1 className="text-3xl font-bold mb-3 break-words">Kontakt</h1>
            <div className="border-b-2 border-l-2 border-primary p-3">
              {portfolio?.userData.email && (
                <div className="flex flex-wrap">
                  <div className="font-bold mr-2">Email:</div>
                  <div>{portfolio?.userData.email}</div>
                </div>
              )}
              {portfolio?.userData.phoneNumber && (
                <div className="flex flex-wrap">
                  <div className="font-bold mr-2">Telefon:</div>
                  <div>{portfolio?.userData.phoneNumber}</div>
                </div>
              )}
            </div>
          </div>
        )}
        {socialCards && socialCards.length > 0 && (
          <div className="my-10">
            <h1 className="text-3xl font-bold mb-3 break-words">Konta społecznościowe</h1>
            {socialCards}
          </div>
        )}
        {educationCards && educationCards.length > 0 && (
          <div className="my-10">
            <h1 className="text-3xl font-bold mb-3 break-words">Edukacja</h1>
            {educationCards}
          </div>
        )}
        {experienceCards && experienceCards.length > 0 && (
          <div className="my-10">
            <h1 className="text-3xl font-bold mb-3 break-words">Doświadczenie</h1>
            {experienceCards}
          </div>
        )}
        {techSkillCards && techSkillCards.length > 0 && (
          <div className="my-10">
            <h1 className="text-3xl font-bold mb-3 break-words">Umiejętności techniczne</h1>
            {techSkillCards}
          </div>
        )}
        {softSkillCards && softSkillCards.length > 0 && (
          <div className="my-10">
            <h1 className="text-3xl font-bold mb-3 break-words">Umiejętności miękkie</h1>
            {softSkillCards}
          </div>
        )}
        {foreignLanguageCards && foreignLanguageCards.length > 0 && (
          <div className="my-10">
            <h1 className="text-3xl font-bold mb-3 break-words">Języki obce</h1>
            {foreignLanguageCards}
          </div>
        )}
        {projectCards && projectCards.length > 0 && (
          <div className="my-10">
            <h1 className="text-3xl font-bold mb-3 break-words">Projekty</h1>
            {projectCards}
          </div>
        )}
        {interestCards && interestCards.length > 0 && (
          <div className="my-10">
            <h1 className="text-3xl font-bold mb-3 break-words">Zainteresowania</h1>
            {interestCards}
          </div>
        )}
        {courseCards && courseCards.length > 0 && (
          <div className="my-10">
            <h1 className="text-3xl font-bold mb-3 break-words">Kursy</h1>
            {courseCards}
          </div>
        )}
        {portfolio?.userData.cvClause && (
          <div className="my-10">
            <h1 className="text-3xl font-bold mb-3 break-words">Klauzula informacyjna</h1>
            {portfolio?.userData.cvClause}
          </div>
        )}
      </div>
    </>
  );
}

export default Portfolio;
