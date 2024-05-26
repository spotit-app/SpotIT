import PropTypes from 'prop-types';
import { ReactNode } from 'react';

interface AdminItemCardProps {
  children?: ReactNode;
  name: string;
  logo?: string;
}

function AdminItemCard({ children, name, logo }: AdminItemCardProps) {
  return (
    <div className="flex justify-between border-b-2 border-l-2 border-primary my-4 p-3">
      <div className="block md:flex items-center w-full">
        <div className="text-xl w-full font-bold flex items-center">
          {logo && (
            <div
              className="w-12 h-12 flex justify-center items-center mr-2"
              data-testid="admin-item-card-logo"
            >
              <img src={logo} alt="Skill Logo" className="max-w-full max-h-full" />
            </div>
          )}
          <div>{name}</div>
        </div>
      </div>
      <div className="flex items-center">{children}</div>
    </div>
  );
}

export { AdminItemCard };

AdminItemCard.propTypes = {
  children: PropTypes.node,
  name: PropTypes.string.isRequired,
  logo: PropTypes.string
};
