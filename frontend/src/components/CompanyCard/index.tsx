import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { ReadCompany } from 'types/company';
import icons from 'assets/icons';

function CompanyCard({ company }: { company: ReadCompany }) {
  return (
    <Link
      to={`/moje-firmy/${company.id}`}
      className="w-full flex items-center p-4 border-2 border-primary rounded-lg my-3"
    >
      {company.logoUrl ? (
        <img
          src={company.logoUrl}
          referrerPolicy="no-referrer"
          alt="Company Logo"
          className="w-20 h-20 rounded-full"
        />
      ) : (
        <div className="w-20 h-20 bg-red flex justify-center items-center">
          <icons.BsBuildings size="45" className="text-primary" />
        </div>
      )}
      <div className="ml-3">
        <div className="text-2xl font-bold">{company.name}</div>
        <div className="text-primary">NIP: {company.nip}</div>
      </div>
    </Link>
  );
}

CompanyCard.propTypes = {
  company: PropTypes.object.isRequired
};

export { CompanyCard };
