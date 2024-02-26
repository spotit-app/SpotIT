import { useSearchParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { FilterSidebar, Loading, PortfolioListElement } from '@/components';
import { useEmployeeSearch } from '@/hooks/useEmployeeSearch';
import { ReadPortfolioPageDto } from '@/types/profile';
import icons from '@/assets/icons';

function Employees() {
  const [params, setParams] = useSearchParams();

  const { portfolios, portfoliosIsPending, setQueryParams } = useEmployeeSearch();
  const [isFirst, setIsFirst] = useState(true);
  const [isLast, setIsLast] = useState(false);
  const [page, setPage] = useState(1);

  useEffect(() => {
    const page = parseInt(params.get('page') || '1') - 1;
    const techSkillNameIds = params.getAll('techSkillNameIds');
    const foreignLanguageNameIds = params.getAll('foreignLanguageNameIds');

    setPage(page + 1);

    setQueryParams({ page, techSkillNameIds, foreignLanguageNameIds });
  }, [params]);

  useEffect(() => {
    setIsFirst(!!portfolios?.first);
    setIsLast(!!portfolios?.last);
  }, [portfolios]);

  const changePage = (value: number) => {
    setParams((prev) => ({ ...prev, page: (page + value).toString() }));
  };

  const mappedPortfolios = portfolios?.content.map((portfolio: ReadPortfolioPageDto) => (
    <PortfolioListElement portfolio={portfolio} key={portfolio.portfolioUrl} />
  ));

  return (
    <div className="flex">
      <div className="drawer lg:drawer-open">
        <input id="my-drawer-2" type="checkbox" className="drawer-toggle" />
        {portfoliosIsPending ? (
          <Loading />
        ) : (
          <div className="drawer-content flex">
            <div className="flex flex-col w-full items-center p-5">
              <span className="p-5 text-2xl font-semibold">Wyszukani pracownicy:</span>
              {mappedPortfolios}
              <div className="join mt-10 justify-center">
                <button className="join-item btn" disabled={isFirst} onClick={() => changePage(-1)}>
                  «
                </button>
                <button className="join-item btn">Strona {page}</button>
                <button className="join-item btn" disabled={isLast} onClick={() => changePage(1)}>
                  »
                </button>
              </div>
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
            <FilterSidebar />
          </ul>
        </div>
      </div>
    </div>
  );
}

export default Employees;
