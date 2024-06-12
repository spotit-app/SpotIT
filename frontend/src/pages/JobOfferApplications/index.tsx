import {
  Button,
  FilterStatusButton,
  Loading,
  NoContent,
  Pagination,
  PortfolioListElement
} from 'components';
import { useJobApplications } from 'hooks';
import { ReadJobApplication } from 'types/company';
import { useParams, useSearchParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { translateStatus } from '@/utils';
import icons from 'assets/icons';

function JobOfferApplications() {
  const [params, setParams] = useSearchParams();

  const { companyId, jobOfferId } = useParams<{ companyId: string; jobOfferId: string }>();
  const { setQueryParams, updateStatus, getJobApplications } = useJobApplications();
  const { data: jobApplications, isLoading } = getJobApplications(+companyId!, +jobOfferId!);

  const [isFirst, setIsFirst] = useState(true);
  const [isLast, setIsLast] = useState(false);
  const [page, setPage] = useState(1);
  const [applicationStatus, setApplicationStatus] = useState('');

  useEffect(() => {
    const status = params.get('status') || '';
    const page = parseInt(params.get('page') || '1') - 1;

    setPage(page + 1);
    setApplicationStatus(status);
    setQueryParams({
      page,
      status: translateStatus(status)
    });
  }, [params]);

  const changeStatus = (newStatus: string) => {
    applicationStatus == newStatus ? setParams({}) : setParams({ status: newStatus });
  };

  const statusesToChooseFrom = ['Dostarczono', 'Zaakceptowano', 'Odrzucono']?.map(
    (experience: string) => (
      <FilterStatusButton
        key={experience}
        entity={experience}
        onChange={() => changeStatus(experience)}
        isChecked={applicationStatus == experience}
      />
    )
  );

  const changePage = (value: number) => {
    setParams((params) => {
      params.set('page', (page + value).toString());
      return params;
    });
  };

  const handleButtonClick = async (
    event: React.MouseEvent<HTMLElement>,
    applicationId: number,
    status: string
  ) => {
    event.preventDefault();

    await updateStatus.mutateAsync({
      companyId: +companyId!,
      jobOfferId: +jobOfferId!,
      applicationId,
      newStatus: status
    });
  };

  useEffect(() => {
    setIsFirst(!!jobApplications?.first);
    setIsLast(!!jobApplications?.last);
  }, [jobApplications]);

  const jobApplicationElements = jobApplications?.content.map(
    (jobApplication: ReadJobApplication) => (
      <div key={jobApplication.id} className="my-2">
        <h2 className="text-xl font-bold">
          Status: <span className="text-primary">{jobApplication.applicationStatus}</span>
        </h2>
        <PortfolioListElement
          portfolio={jobApplication.portfolio}
          key={jobApplication.portfolio.portfolioUrl}
        >
          {jobApplication.applicationStatus === 'Dostarczono' && (
            <>
              <Button
                type="button"
                className="btn-md"
                onClick={(event) => handleButtonClick(event, jobApplication.id, 'ACCEPTED')}
              >
                <icons.GoCheck size={30} />
              </Button>
              <Button
                type="button"
                className="btn-secondary btn-md"
                onClick={(event) => handleButtonClick(event, jobApplication.id, 'REJECTED')}
              >
                <icons.RxCross2 size={30} />
              </Button>
            </>
          )}
        </PortfolioListElement>
      </div>
    )
  );

  const content = isLoading ? (
    <Loading />
  ) : jobApplications?.totalElements !== 0 ? (
    jobApplicationElements
  ) : (
    <NoContent />
  );

  return (
    <div className="p-2 lg:p-4 w-full flex flex-col">
      <h1 className="text-2xl font-bold text-center">Aplikacje</h1>
      <div className="flex flex-col sm:flex-row sm:justify-end space-y-5 sm:space-y-0 sm:space-x-5 pt-5">
        {statusesToChooseFrom}
      </div>
      {content}
      {jobApplicationElements?.length !== 0 && (
        <Pagination page={page} isFirst={isFirst} isLast={isLast} changePage={changePage} />
      )}
    </div>
  );
}

export default JobOfferApplications;
