import { ReadPortfolioPageDto } from '@/types/profile';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

interface PortfolioListElementProps {
  portfolio: ReadPortfolioPageDto;
}

function PortfolioListElement({ portfolio }: PortfolioListElementProps) {
  return (
    <div key={portfolio.portfolioUrl} className="w-full my-2">
      <div className="card border-2 border-primary rounded-lg place-items-center">
        <div className="w-full p-4 flex items-center gap-x-4">
          <img
            src={portfolio.userData.profilePictureUrl}
            referrerPolicy="no-referrer"
            alt="Profile Picture"
            className="w-20 h-20 rounded-full"
          />
          <div className="flex flex-col pl-10 pr-20">
            <span className="text-2xl font-bold">
              {portfolio.userData.firstName} {portfolio.userData.lastName}
            </span>
            <span className="text-primary">
              <Link to={`/portfolio/${portfolio.portfolioUrl}`}>{portfolio.portfolioUrl}</Link>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}

PortfolioListElement.propTypes = {
  portfolio: PropTypes.object.isRequired
};

export { PortfolioListElement };
