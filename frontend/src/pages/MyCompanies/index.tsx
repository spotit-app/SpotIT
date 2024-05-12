import { CompanyCard, Loading, NoContent, PopUp, ProfileContainer } from 'components';
import { CompaniesForm } from './CompaniesForm';
import { useCompanies } from 'hooks';
import { ReadCompany } from 'types/company';

function MyCompanies() {
  const { companies, companiesIsPending } = useCompanies();

  const companyElements = companies?.map((company: ReadCompany) => (
    <CompanyCard key={company.id} company={company}></CompanyCard>
  ));

  const content = companiesIsPending ? (
    <Loading />
  ) : companyElements?.length !== 0 ? (
    companyElements
  ) : (
    <NoContent />
  );

  return (
    <ProfileContainer title="Twoje firmy" addText="Dodaj firmę">
      {content}

      <PopUp title="Dodaj firmę">
        <CompaniesForm />
      </PopUp>
    </ProfileContainer>
  );
}

export default MyCompanies;
