import { useJobOffers } from 'hooks';
import { Button, JobOfferCard, Loading, NoContent, Pagination, PopUp, ProfileContainer } from '..';
import { JobOfferForm } from './JobOfferForm';
import { useNavigate, useParams, useSearchParams } from 'react-router-dom';
import { ReadJobOffer } from 'types/company';
import { useEffect, useState } from 'react';
import { showModal } from 'utils';

function MyCompanyJobOffers() {
  const { id } = useParams<{ id: string }>();
  const [params, setParams] = useSearchParams();

  const { setQueryParams, jobOffers, jobOffersIsPending, deleteJobOffer } = useJobOffers(+id!);

  const [jobOfferToEdit, setJobOfferToEdit] = useState<ReadJobOffer | undefined>(undefined);
  const [isFirst, setIsFirst] = useState(true);
  const [isLast, setIsLast] = useState(false);
  const [page, setPage] = useState(1);

  const navigate = useNavigate();

  useEffect(() => {
    const page = parseInt(params.get('page') || '1') - 1;

    setPage(page + 1);
    setQueryParams({ page });
  }, [params]);

  const changePage = (value: number) => {
    setParams({ page: (page + value).toString() });
  };

  useEffect(() => {
    setIsFirst(!!jobOffers?.first);
    setIsLast(!!jobOffers?.last);
  }, [jobOffers]);

  const editJobOffer = (jobOffer: ReadJobOffer) => {
    setJobOfferToEdit(jobOffer);
    showModal();
  };

  const jobOfferElements = jobOffers?.content?.map((jobOffer: ReadJobOffer) => {
    const isDueDateExpired = new Date(jobOffer.dueDate) < new Date();

    return (
      <JobOfferCard
        key={jobOffer.id}
        jobOffer={jobOffer}
        dateClassName={isDueDateExpired ? 'bg-base-300 text-error font-bold' : ''}
      >
        <div className="mr-2">
          <Button
            onClick={(e) => {
              e.preventDefault();
              navigate(`/moje-firmy/${id}/oferty-pracy/${jobOffer.id}/aplikacje`);
            }}
          >
            Aplikacje
          </Button>
        </div>

        <div className="mr-2">
          <Button
            onClick={(e) => {
              e.preventDefault();
              editJobOffer(jobOffer);
            }}
          >
            Edytuj
          </Button>
        </div>

        <Button
          onClick={(e) => {
            e.preventDefault();
            deleteJobOffer.mutate(jobOffer.id);
          }}
        >
          Usuń
        </Button>
      </JobOfferCard>
    );
  });

  const content =
    jobOffersIsPending || deleteJobOffer.isPending ? (
      <Loading />
    ) : jobOffers?.totalElements !== 0 ? (
      jobOfferElements
    ) : (
      <NoContent />
    );

  return (
    <ProfileContainer
      title="Oferty pracy"
      addText="Dodaj ofertę"
      onAdd={() => setJobOfferToEdit(undefined)}
    >
      {content}

      {jobOfferElements?.length !== 0 && (
        <Pagination page={page} isFirst={isFirst} isLast={isLast} changePage={changePage} />
      )}

      <PopUp title="Dodaj ofertę">
        <JobOfferForm jobOfferToEdit={jobOfferToEdit} />
      </PopUp>
    </ProfileContainer>
  );
}

export { MyCompanyJobOffers };
