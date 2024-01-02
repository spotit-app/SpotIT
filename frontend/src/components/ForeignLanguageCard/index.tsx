import PropTypes from 'prop-types';
import { ReactNode } from 'react';

interface ForeignLanguageCardProps {
  children: ReactNode;
  name: string;
  flagUrl: string;
}

function ForeignLanguageCard({ children, name, flagUrl }: ForeignLanguageCardProps) {
  return (
    <div className="block md:flex justify-between border-b-2 border-l-2 border-primary my-4 p-3">
      <div className="block md:flex items-center w-full">
        <div className="text-xl w-full md:mx-3 font-bold">
          <img className="w-4 mr-2" src={flagUrl} />
          {name}
        </div>
      </div>
      <div className="flex justify-end">{children}</div>
    </div>
  );
}

export { ForeignLanguageCard };

ForeignLanguageCard.propTypes = {
  children: PropTypes.node.isRequired,
  name: PropTypes.string.isRequired,
  flagUrl: PropTypes.string.isRequired
};
