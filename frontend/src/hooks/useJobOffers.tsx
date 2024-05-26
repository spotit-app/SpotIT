import { slugifyAuth0Id } from 'utils';
import { useAuth0 } from '@auth0/auth0-react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useCallback, useState } from 'react';
import axios from 'axios';
import { JobOfferPage, PaginationQueryParams, ReadJobOffer, WriteJobOffer } from 'types/company';
import { DeleteResponse } from 'types/profile';

function useJobOffers(companyId?: number) {
  const [queryParams, setQueryParams] = useState<PaginationQueryParams>({
    page: 0
  });

  const { user, getAccessTokenSilently } = useAuth0();
  const auth0Id = user?.sub;
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: jobOffers,
    error: jobOffersError,
    isPending: jobOffersIsPending
  } = useQuery({
    queryKey: ['jobOffers', companyId, queryParams],
    queryFn: useCallback(async (): Promise<JobOfferPage> => {
      const queryData = new URLSearchParams();

      queryData.append('page', queryParams.page.toString());

      return await axios
        .get(`/api/company/${companyId}/jobOffer`, {
          params: queryData
        })
        .then((res) => res.data);
    }, [auth0Id, companyId, queryParams]),
    enabled: !!companyId && !!queryParams
  });

  const getJobOffer = (jobOfferId: number) => {
    return useQuery({
      queryKey: ['jobOffer', jobOfferId],
      queryFn: useCallback(async (): Promise<ReadJobOffer> => {
        return await axios.get(`/api/jobOffer/${jobOfferId}`).then((res) => res.data);
      }, [auth0Id, companyId, jobOfferId])
    });
  };

  const createJobOffer = useMutation({
    mutationFn: useCallback(
      async (jobOffer: WriteJobOffer): Promise<ReadJobOffer> => {
        return await axios
          .post(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/company/${companyId}/jobOffer`,
            jobOffer,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id, companyId]
    ),
    onSuccess: (createdJobOffer: ReadJobOffer) => {
      queryClient.refetchQueries({
        queryKey: ['jobOffers', createdJobOffer.company.id, queryParams]
      });
    }
  });

  const deleteJobOffer = useMutation({
    mutationFn: useCallback(
      async (id: number): Promise<DeleteResponse> => {
        return await axios
          .delete(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/company/${companyId}/jobOffer/${id}`,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id, companyId]
    ),
    onSuccess: () => {
      queryClient.refetchQueries({
        queryKey: ['jobOffers', companyId, queryParams]
      });
    }
  });

  const updateJobOffer = useMutation({
    mutationFn: useCallback(
      async (jobOffer: WriteJobOffer & { id: number }): Promise<ReadJobOffer> => {
        return await axios
          .put(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/company/${companyId}/jobOffer/${
              jobOffer.id
            }`,
            jobOffer,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id, companyId]
    ),
    onSuccess: (updatedJobOffer: ReadJobOffer) => {
      queryClient.refetchQueries({
        queryKey: ['jobOffers', updatedJobOffer.company.id, queryParams]
      });
    }
  });

  return {
    setQueryParams,
    jobOffers,
    jobOffersError,
    jobOffersIsPending,
    createJobOffer,
    deleteJobOffer,
    updateJobOffer,
    getJobOffer
  };
}

export { useJobOffers };
