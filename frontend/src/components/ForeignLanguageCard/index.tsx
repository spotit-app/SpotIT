import PropTypes from 'prop-types';
import { ReactNode } from 'react';

interface ForeignLanguageCardProps {
  children?: ReactNode;
  name: string;
  flagUrl: string;
  level: string;
}

function ForeignLanguageCard({ children, name, flagUrl, level }: ForeignLanguageCardProps) {
  return (
    <div className="block md:flex justify-between border-b-2 border-l-2 border-primary my-4 p-3">
      <div className="block md:flex items-center w-full">
        <div className="text-xl w-full md:w-1/3 md:mx-3 font-bold flex items-center">
          <div className="w-12 h-12 flex justify-center items-center mr-2">
            <img className="max-w-full max-h-full border-2 border-primary" src={flagUrl} />
          </div>

          {name}
        </div>

        <div className="text-xl font-bold">{level}</div>
      </div>
      <div className="flex justify-end">{children}</div>
    </div>
  );
}

export { ForeignLanguageCard };

ForeignLanguageCard.propTypes = {
  children: PropTypes.node,
  name: PropTypes.string.isRequired,
  flagUrl: PropTypes.string.isRequired,
  level: PropTypes.string.isRequired
};
