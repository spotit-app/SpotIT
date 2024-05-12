import { ReadJobOffer } from 'types/company';
import icons from 'assets/icons';
import { ReactNode } from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { Badge } from '../Badge';
import { useNavigate } from 'react-router-dom';
import { twMerge } from 'tailwind-merge';

interface JobOfferCardProps {
  dateClassName?: string;
  children?: ReactNode;
  jobOffer: ReadJobOffer;
}

function JobOfferCard({ dateClassName, children, jobOffer }: JobOfferCardProps) {
  const navigate = useNavigate();

  const techSkillBadges = jobOffer.techSkillNames.map((techSkill) => (
    <Badge key={techSkill.id} name={techSkill.name} />
  ));

  const companyLogo = jobOffer?.company.logoUrl ? (
    <img
      src={jobOffer?.company.logoUrl}
      referrerPolicy="no-referrer"
      alt="Company Logo"
      className="w-16 h-16 lg:w-20 lg:h-20 rounded-full"
    />
  ) : (
    <div className="w-20 h-20 bg-red flex justify-center items-center" data-testid="building-icon">
      <icons.BsBuildings size="45" className="text-primary" />
    </div>
  );

  return (
    <Link
      to={`/oferty-pracy/${jobOffer.id}`}
      className="w-full flex flex-col justify-between border-2 border-primary rounded-lg my-3"
    >
      <div className="flex p-2 lg:p-4">
        <div className="w-32 h-32 flex items-center justify-center">{companyLogo}</div>
        <div className="flex flex-col w-full p-4 lg:h-52 justify-between">
          <div>
            <h2 className="text-2xl font-bold text-primary">{jobOffer.name}</h2>
            <p className="text-lg font-semibold">
              {jobOffer.minSalary} {jobOffer.maxSalary && ` - ${jobOffer.maxSalary}`} zł / miesiąc
            </p>
            <div className="flex flex-wrap">
              <p className="text-lg font-bold mr-3">{jobOffer?.company.name}</p>
              <button
                onClick={(e) => {
                  e.preventDefault();
                  navigate('/firmy/' + jobOffer?.company.id);
                }}
                className="link-hover text-lg text-primary flex items-center hover:underline"
              >
                <icons.BsBriefcaseFill
                  size="20"
                  className="text-primary mr-2"
                ></icons.BsBriefcaseFill>
                Profil firmy
              </button>
            </div>
            <div>
              {jobOffer?.company.address.city}, {jobOffer.company.address.country}
            </div>
          </div>

          <div>{techSkillBadges}</div>
        </div>
      </div>
      <div>
        <div className="p-2 lg:p-4 flex justify-center md:justify-end">{children}</div>
        <div className={twMerge('border-t-2 border-primary p-2 text-right', dateClassName)}>
          Termin oferty: {jobOffer.dueDate}
        </div>
      </div>
    </Link>
  );
}

export { JobOfferCard };

JobOfferCard.propTypes = {
  dateClassName: PropTypes.string,
  children: PropTypes.node,
  jobOffer: PropTypes.object.isRequired
};
