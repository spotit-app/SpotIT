import { useSearchParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { EmployeeFilterSidebar, Loading, Pagination, PortfolioListElement } from 'components';
import { useEmployeeSearch } from 'hooks';
import { ReadPortfolioPageDto } from 'types/profile';
import icons from 'assets/icons';

function Employees() {
  const [params, setParams] = useSearchParams();

  const { portfolios, portfoliosIsPending, setQueryParams } = useEmployeeSearch();
  const [isFirst, setIsFirst] = useState(true);
  const [isLast, setIsLast] = useState(false);
  const [page, setPage] = useState(1);

  useEffect(() => {
    const page = Math.max(0, parseInt(params.get('page') || '1') - 1);
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
    setParams((params) => {
      params.set('page', (page + value).toString());
      return params;
    });
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
            <EmployeeFilterSidebar />
          </ul>
        </div>
      </div>
    </div>
  );
}

export default Employees;
