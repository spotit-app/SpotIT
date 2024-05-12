import { JobOfferCard, Loading, NoContent } from 'components';
import { useCompanies, useJobOffers } from 'hooks';
import icons from 'assets/icons';
import { useParams } from 'react-router-dom';
import { ReadJobOffer } from 'types/company';

function Company() {
  const { id } = useParams<{ id: string }>();
  const { getCompany } = useCompanies();
  const { data: company, isPending } = getCompany(+id!);
  const { jobOffers, jobOffersIsPending } = useJobOffers(+id!);

  const companyLogo = company?.logoUrl ? (
    <img
      src={company.logoUrl}
      referrerPolicy="no-referrer"
      alt="Company Logo"
      className="w-16 h-16 lg:w-20 lg:h-20 rounded-full"
    />
  ) : (
    <div className="w-20 h-20 bg-red flex justify-center items-center">
      <icons.BsBuildings size="45" className="text-primary" />
    </div>
  );
  const jobOfferElements = jobOffers?.content?.map((jobOffer: ReadJobOffer) => (
    <JobOfferCard key={jobOffer.id} jobOffer={jobOffer}></JobOfferCard>
  ));

  const content = jobOffersIsPending ? (
    <Loading />
  ) : jobOfferElements?.length !== 0 ? (
    jobOfferElements
  ) : (
    <NoContent />
  );

  return isPending || jobOffersIsPending ? (
    <Loading />
  ) : (
    <div>
      <div className="bg-base-300 p-2 md:p-8 sm:flex items-center justify-between">
        <div className="flex items-center justify-center">
          <div className="w-32 h-32 flex items-center justify-center">{companyLogo}</div>
          <div>
            <h1 className="text-2xl font-bold">{company?.name}</h1>
            <div className="flex items-center my-1">
              <icons.TbWorldWww size="20" className="mr-2 text-primary" />
              <a
                href={company?.websiteUrl}
                target="_blank"
                rel="noreferrer"
                className="link-hover text-primary"
              >
                Odwiedź stronę
              </a>
            </div>

            <p className="text-sm font-semibold">NIP: {company?.nip}</p>
            <p className="text-sm font-semibold">REGON: {company?.regon}</p>
          </div>
        </div>
        <div className="flex flex-col items-center p-5 sm:p-0">
          <div className="flex items-center">
            <icons.FaLocationDot size="20" className="mr-2 text-primary" />
            <div>
              <p className="text-sm font-semibold">
                {company?.address.street}, {company?.address.zipCode}
              </p>
              <p className="text-sm font-semibold">
                {company?.address.city}, {company?.address.country}
              </p>
            </div>
          </div>

          <p className="text-xl font-bold mt-5">Liczba ofert: {jobOffers?.totalElements}</p>
        </div>
      </div>

      <div className="flex flex-col items-center p-2 md:p-8">
        <h1 className="text-2xl font-bold text-center my-2">Oferty pracy</h1>
        {content}
      </div>
    </div>
  );
}

export default Company;
