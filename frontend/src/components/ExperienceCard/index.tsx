import PropTypes from 'prop-types';
import { ReactNode } from 'react';

interface ExperienceCardProps {
  children: ReactNode;
  companyName: string;
  position: string;
  startDate: string;
  endDate: string | null;
}

function ExperienceCard({
  children,
  companyName,
  position,
  startDate,
  endDate
}: ExperienceCardProps) {
  return (
    <div className="block md:flex justify-between border-b-2 border-l-2 border-primary my-4 p-3">
      <div className="block md:flex items-center w-full">
        <div className="text-xl w-full lg:w-1/3 md:mx-3 font-bold">
          <div className="w-full">{companyName}</div>
          <div className="text-primary w-full">{position}</div>
        </div>
        <div className="flex font-bold">
          {startDate} - {endDate ? endDate : 'teraz'}
        </div>
      </div>
      <div className="flex justify-end">{children}</div>
    </div>
  );
}

export { ExperienceCard };

ExperienceCard.propTypes = {
  children: PropTypes.node.isRequired,
  companyName: PropTypes.string.isRequired,
  position: PropTypes.string.isRequired,
  startDate: PropTypes.string.isRequired,
  endDate: PropTypes.string
};
