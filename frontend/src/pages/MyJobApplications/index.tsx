import { ReadJobApplication } from 'types/company';
import { FilterStatusButton, JobOfferCard, Loading, NoContent, Pagination } from 'components';
import { useJobApplications } from 'hooks';
import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { translateStatus } from 'utils';

function MyJobApplications() {
  const [params, setParams] = useSearchParams();

  const { setQueryParams, myJobApplications, myJobApplicationsIsPending } = useJobApplications();
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

  useEffect(() => {
    setIsFirst(!!myJobApplications?.first);
    setIsLast(!!myJobApplications?.last);
  }, [myJobApplications]);

  const myJobApplicationElements = myJobApplications?.content.map(
    (jobApplication: ReadJobApplication) => (
      <div key={jobApplication.id} className="my-2">
        <h2 className="text-xl font-bold">
          Status: <span className="text-primary">{jobApplication.applicationStatus}</span>
        </h2>
        <JobOfferCard jobOffer={jobApplication.jobOffer} />
      </div>
    )
  );

  const content = myJobApplicationsIsPending ? (
    <Loading />
  ) : myJobApplications?.totalElements !== 0 ? (
    myJobApplicationElements
  ) : (
    <NoContent />
  );

  return (
    <div className="p-2 lg:p-4 w-full flex flex-col">
      <h1 className="text-2xl font-bold text-center">Moje aplikacje</h1>
      <div className="flex flex-row justify-end space-x-5 pt-5">{statusesToChooseFrom}</div>
      {content}
      {myJobApplicationElements?.length !== 0 && (
        <Pagination page={page} isFirst={isFirst} isLast={isLast} changePage={changePage} />
      )}
    </div>
  );
}

export default MyJobApplications;
