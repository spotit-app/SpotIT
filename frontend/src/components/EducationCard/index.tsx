import PropTypes from 'prop-types';
import { ReactNode } from 'react';

interface EducationCardProps {
  children: ReactNode;
  schoolName: string;
  faculty: string;
  startDate: string;
  endDate: string | null;
  educationLevel: string;
}

function EducationCard({
  children,
  schoolName,
  faculty,
  startDate,
  endDate,
  educationLevel
}: EducationCardProps) {
  return (
    <div className="block md:flex justify-between border-b-2 border-l-2 border-primary my-4 p-3">
      <div className="block md:flex items-center w-full">
        <div className="text-xl w-full md:mx-3 font-bold">
          <div className="w-full">
            {schoolName} - {educationLevel}
          </div>
          <div className="text-primary w-full">{faculty}</div>
        </div>
        <div className="flex font-bold w-full">
          {startDate} - {endDate ? endDate : 'teraz'}
        </div>
      </div>
      <div className="flex justify-end">{children}</div>
    </div>
  );
}

export { EducationCard };

EducationCard.propTypes = {
  children: PropTypes.node.isRequired,
  schoolName: PropTypes.string.isRequired,
  faculty: PropTypes.string.isRequired,
  startDate: PropTypes.string.isRequired,
  endDate: PropTypes.string,
  educationLevel: PropTypes.string.isRequired
};
