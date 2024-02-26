import PropTypes from 'prop-types';
import { ReactNode } from 'react';

interface InterestCardProps {
  children?: ReactNode;
  name: string;
}

function InterestCard({ children, name }: InterestCardProps) {
  return (
    <div className="block md:flex justify-between border-b-2 border-l-2 border-primary my-4 p-3">
      <div className="block md:flex items-center w-full">
        <div className="text-xl w-full md:mx-3 font-bold">{name}</div>
      </div>
      <div className="flex justify-end">{children}</div>
    </div>
  );
}

export { InterestCard };

InterestCard.propTypes = {
  children: PropTypes.node,
  name: PropTypes.string.isRequired
};
