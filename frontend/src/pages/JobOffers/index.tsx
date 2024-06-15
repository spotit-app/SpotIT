import { useSearchParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { JobOffersFilterSidebar, Loading, JobOfferCard, Pagination } from 'components';
import icons from 'assets/icons';
import { useJobOffersSearch } from 'hooks';
import { ReadJobOffer } from 'types/company';

function JobOffers() {
  const [params, setParams] = useSearchParams();

  const { jobOffers, jobOffersisPending, setQueryParams } = useJobOffersSearch();
  const [isFirst, setIsFirst] = useState(true);
  const [isLast, setIsLast] = useState(false);
  const [page, setPage] = useState(1);

  useEffect(() => {
    const page = Math.max(0, parseInt(params.get('page') || '1') - 1);
    const techSkillNameIds = params.getAll('techSkillNameIds');
    const foreignLanguageNameIds = params.getAll('foreignLanguageNameIds');
    const experienceNameIds = params.getAll('experienceNameIds');
    const workModeIds = params.getAll('workModeIds');

    setPage(page + 1);
    setQueryParams({
      page,
      techSkillNameIds,
      foreignLanguageNameIds,
      experienceNameIds,
      workModeIds
    });
  }, [params]);

  const changePage = (value: number) => {
    setParams((params) => {
      params.set('page', (page + value).toString());
      return params;
    });
  };

  useEffect(() => {
    setIsFirst(!!jobOffers?.first);
    setIsLast(!!jobOffers?.last);
  }, [jobOffers]);

  const mappedJobOffers = jobOffers?.content.map((jobOffer: ReadJobOffer) => (
    <JobOfferCard key={jobOffer.id} jobOffer={jobOffer} />
  ));

  return (
    <div className="flex">
      <div className="drawer lg:drawer-open">
        <input id="my-drawer-2" type="checkbox" className="drawer-toggle" />
        {jobOffersisPending ? (
          <Loading />
        ) : (
          <div className="drawer-content flex">
            <div className="flex flex-col w-full items-center p-5">
              <span className="p-5 text-2xl font-semibold">Oferty pracy:</span>
              {mappedJobOffers}
              <Pagination page={page} isFirst={isFirst} isLast={isLast} changePage={changePage} />
            </div>
            <label
              htmlFor="my-drawer-2"
              className="btn btn-primary drawer-button lg:hidden sidebar-toggle fixed"
            >
              <icons.HiMenuAlt2 size="30" />
            </label>
          </div>
        )}
        <div className="drawer-side">
          <label htmlFor="my-drawer-2" aria-label="close sidebar" className="drawer-overlay" />
          <ul className="menu p-4 min-h-full bg-base-200 text-base-content">
            <JobOffersFilterSidebar />
          </ul>
        </div>
      </div>
    </div>
  );
}

export default JobOffers;
