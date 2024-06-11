import { Badge, Button, Loading } from 'components';
import { useJobOffers, useJobApplications } from 'hooks';
import { Link, useParams } from 'react-router-dom';
import icons from 'assets/icons';
import { errorToast, mapToNames, successToast } from 'utils';
import { useAuth0 } from '@auth0/auth0-react';

function JobOffer() {
  const { user } = useAuth0();
  const { id } = useParams<{ id: string }>();
  const { getJobOffer } = useJobOffers();
  const { checkOfferData, createJobApplication } = useJobApplications();
  const { data: checkIf } = checkOfferData(+id!);
  const { data: jobOffer, isPending: jobOfferIsPending } = getJobOffer(+id!);

  const onApply = async () => {
    try {
      await createJobApplication.mutateAsync(+id!);
      successToast('Aplikacja została wysłana!');
    } catch (error) {
      errorToast('Błąd: Upewnij się, że portfolio zostało wygenerowane!');
    }
  };

  const details = [
    {
      icon: icons.BsPersonWorkspace,
      title: jobOffer?.position,
      subtitle: ''
    },
    {
      icon: icons.BsBarChartFill,
      title: jobOffer?.workExperienceName,
      subtitle: ''
    },
    {
      icon: icons.FaLocationDot,
      title: `${jobOffer?.company.address.city}, ${jobOffer?.company.address.country}`,
      subtitle: `${jobOffer?.company.address.street}, ${jobOffer?.company.address.zipCode}`
    },
    {
      icon: icons.MdHomeWork,
      title: `${mapToNames(jobOffer?.workModes || []).join(', ')}`,
      subtitle: ''
    }
  ];

  const companyLogo = jobOffer?.company.logoUrl ? (
    <img
      src={jobOffer?.company.logoUrl}
      referrerPolicy="no-referrer"
      alt="Company Logo"
      className="w-16 h-16 lg:w-20 lg:h-20 rounded-full"
    />
  ) : (
    <div className="w-20 h-20 bg-red flex justify-center items-center">
      <icons.BsBuildings size="45" className="text-primary" />
    </div>
  );

  const detailElements = details.map((detail, index) => {
    const Icon = detail.icon;
    return (
      <div key={index} className="w-full md:w-1/2 flex items-center my-3">
        <div className="w-12 h-12 bg-primary rounded flex items-center justify-center mx-2">
          <Icon size="25" className="text-base-100" />
        </div>
        <div>
          <div className="font-semibold text-xl">{detail.title}</div>
          {detail.subtitle && <div className="text-sm font-medium">{detail.subtitle}</div>}
        </div>
      </div>
    );
  });

  const techSkillBadges = jobOffer?.techSkillNames.map((techSkill) => (
    <Badge key={techSkill.id} name={techSkill.name} />
  ));
  const softSkillBadges = jobOffer?.softSkillNames.map((softSkill) => (
    <Badge key={softSkill.id} name={softSkill.name} />
  ));
  const foreignLanguageBadges = jobOffer?.foreignLanguageNames.map((foreignLanguage) => (
    <Badge key={foreignLanguage.id} name={foreignLanguage.name} />
  ));

  return jobOfferIsPending ? (
    <Loading />
  ) : (
    <>
      <div className="flex flex-col">
        <div className="bg-base-300 mb-3 flex flex-wrap">
          <div className="w-full flex items-center justify-between flex-wrap p-2 lg:p-4">
            <div className="flex items-center">
              <div className="w-24 h-24 flex items-center">{companyLogo}</div>
              <div>
                <h1 className="text-2xl md:text-3xl text-primary font-bold">{jobOffer?.name}</h1>
                <div className="flex flex-wrap">
                  <p className="text-lg font-bold mr-3">{jobOffer?.company.name}</p>
                  <Link
                    to={`/firmy/${jobOffer?.company.id}`}
                    className="text-lg text-primary flex items-center hover:underline"
                  >
                    <icons.BsBriefcaseFill
                      size="20"
                      className="text-primary mr-2"
                    ></icons.BsBriefcaseFill>
                    Profil firmy
                  </Link>
                </div>
              </div>
            </div>
            <div className="bg-primary p-3 flex items-center justify-center rounded-lg my-2">
              <icons.RiMoneyDollarCircleFill size="60" className="text-base-100 mr-3" />
              <div className="text-base-100 font-bold text-xl">
                {jobOffer?.minSalary} {jobOffer?.maxSalary && ` - ${jobOffer?.maxSalary}`} zł
              </div>
            </div>
          </div>

          <div className="w-full border-t-2 border-primary" />

          <div className="p-2 lg:p-4 flex flex-wrap w-full">{detailElements}</div>
        </div>

        <div className="border-2 border-primary rounded-lg my-3 p-2 lg:p-4 m-2 lg:m-4">
          <h1 className="text-3xl text-primary font-bold">Opis stanowiska</h1>
          <p className="text-lg whitespace-pre-wrap">{jobOffer?.description}</p>
          <h3 className="text-xl font-bold mt-5">Benefity:</h3>
          <p className="text-lg whitespace-pre-wrap">{jobOffer?.benefits}</p>
          <h3 className="text-xl font-bold mt-5">Oferta ważna do:</h3>
          <p className="text-lg">{jobOffer?.dueDate}</p>
        </div>
        <div className="border-2 border-primary rounded-lg my-3 p-2 lg:p-4 m-2 lg:m-4">
          <h1 className="text-3xl text-primary font-bold">Wymagania</h1>
          <h3 className="text-xl font-bold my-3">Umiejętności techniczne</h3>
          <div>{techSkillBadges}</div>

          <div className="border-b-2 border-primary my-3" />

          <h3 className="text-xl font-bold my-3">Umiejętności miękkie</h3>
          <div>{softSkillBadges}</div>

          <div className="border-b-2 border-primary my-3" />

          <h3 className="text-xl font-bold my-3">Języki</h3>
          <div>{foreignLanguageBadges}</div>
        </div>

        {user && !checkIf?.isUserOffer && (
          <div className="mockup-window border-2 border-primary rounded-lg bg-base-300 m-2 lg:m-4">
            <div className="px-4 py-8 bg-base-100">
              <h1 className="text-2xl font-bold mb-6 text-center">Zainteresowany ofertą?</h1>
              <Button
                type="button"
                className="px-10 text-xl mx-auto block"
                disabled={checkIf?.hasUserApplied || createJobApplication.isPending}
                onClick={onApply}
              >
                {checkIf?.hasUserApplied ? 'APLIKOWANO' : 'APLIKUJ'}
              </Button>
            </div>
          </div>
        )}
      </div>
    </>
  );
}

export default JobOffer;
