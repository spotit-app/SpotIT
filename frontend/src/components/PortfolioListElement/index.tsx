import { ReadPortfolioPageDto } from 'types/profile';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { ReactNode } from 'react';

interface PortfolioListElementProps {
  portfolio: ReadPortfolioPageDto;
  children?: ReactNode;
}

function PortfolioListElement({ portfolio, children }: PortfolioListElementProps) {
  return (
    <Link
      to={`/portfolio/${portfolio.portfolioUrl}`}
      key={portfolio.portfolioUrl}
      className="w-full flex items-center p-4 border-2 border-primary rounded-lg my-3"
    >
      <div className="avatar">
        <div className="w-20 h-20 rounded-full">
          <img
            src={portfolio.userData.profilePictureUrl}
            referrerPolicy="no-referrer"
            alt="Profile Picture"
          />
        </div>
      </div>
      <div className="ml-3 flex flex-col sm:flex-row justify-between w-full">
        <div>
          <div className="text-2xl font-bold">
            {portfolio.userData.firstName} {portfolio.userData.lastName}
          </div>
          <span className="text-primary">{portfolio.portfolioUrl}</span>
        </div>
        <div className="space-x-2">{children}</div>
      </div>
    </Link>
  );
}

PortfolioListElement.propTypes = {
  portfolio: PropTypes.object.isRequired,
  children: PropTypes.node
};

export { PortfolioListElement };
