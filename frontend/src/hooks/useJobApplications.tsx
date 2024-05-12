import {
  JobApplicationPage,
  JobApplicationsQueryParams,
  ReadJobApplication,
  UpdateApplicationStatus
} from 'types/company';
import { slugifyAuth0Id } from 'utils';
import { useAuth0 } from '@auth0/auth0-react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import axios from 'axios';
import { useCallback, useState } from 'react';

interface CheckDataResponse {
  isUserOffer: boolean;
  hasUserApplied: boolean;
}

function useJobApplications() {
  const [queryParams, setQueryParams] = useState<JobApplicationsQueryParams>({
    page: 0,
    status: ''
  });

  const { user, getAccessTokenSilently } = useAuth0();
  const auth0Id = user?.sub;
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`,
      'Content-Type': 'application/json'
    }
  });

  const {
    data: myJobApplications,
    error: myJobApplicationsError,
    isPending: myJobApplicationsIsPending
  } = useQuery({
    queryKey: ['myJobApplications', auth0Id, queryParams],
    queryFn: useCallback(async (): Promise<JobApplicationPage> => {
      const queryData = new URLSearchParams();

      queryData.append('page', queryParams.page.toString());

      if (queryParams.status != '') {
        queryData.append('status', queryParams.status);
      }

      return await axios
        .get(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/application`, {
          ...(await axiosOptions()),
          params: queryData
        })
        .then((res) => res.data);
    }, [auth0Id, queryParams]),
    staleTime: 0,
    enabled: !!auth0Id && !!queryParams
  });

  const createJobApplication = useMutation({
    mutationFn: useCallback(
      async (jobOfferId: number): Promise<ReadJobApplication> => {
        return await axios
          .post(`/api/jobOffer/${jobOfferId}/application`, { auth0Id }, await axiosOptions())
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: (createdJobApplication: ReadJobApplication) => {
      queryClient.setQueryData(['offerData', createdJobApplication.jobOffer.id, auth0Id], {
        isUserOffer: false,
        hasUserApplied: true
      });
      queryClient.setQueryData(
        ['myJobApplications', auth0Id],
        (prev: ReadJobApplication[] = []) => [...prev, createdJobApplication]
      );
      queryClient.setQueryData(
        ['jobApplications', createdJobApplication.jobOffer.id],
        (prev: ReadJobApplication[] = []) => [...prev, createdJobApplication]
      );
    }
  });

  const updateStatus = useMutation({
    mutationFn: useCallback(
      async (data: UpdateApplicationStatus): Promise<ReadJobApplication> => {
        return await axios
          .put(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/company/${data.companyId}/jobOffer/${
              data.jobOfferId
            }/application/${data.applicationId}/changeStatus`,
            data.newStatus,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: (updatedJobApplication: ReadJobApplication) => {
      queryClient.refetchQueries({
        queryKey: ['jobApplications', updatedJobApplication.jobOffer.id, queryParams]
      });
    }
  });

  const checkOfferData = (jobOfferId: number) => {
    return useQuery({
      queryKey: ['offerData', jobOfferId, auth0Id],
      queryFn: useCallback(async (): Promise<CheckDataResponse> => {
        return await axios
          .get(
            `/api/jobOffer/${jobOfferId}/application/${slugifyAuth0Id(auth0Id!)}`,
            await axiosOptions()
          )
          .then((res) => res.data);
      }, [auth0Id, jobOfferId]),
      enabled: !!auth0Id
    });
  };

  const getJobApplications = (companyId: number, jobOfferId: number) => {
    return useQuery({
      queryKey: ['jobApplications', jobOfferId, queryParams],
      queryFn: useCallback(async (): Promise<JobApplicationPage> => {
        const queryData = new URLSearchParams();

        queryData.append('page', queryParams.page.toString());

        if (queryParams.status != '') {
          queryData.append('status', queryParams.status);
        }

        return await axios
          .get(
            `/api/userAccount/${slugifyAuth0Id(
              auth0Id!
            )}/company/${companyId}/jobOffer/${jobOfferId}/application`,
            { ...(await axiosOptions()), params: queryData }
          )
          .then((res) => res.data);
      }, [auth0Id, jobOfferId, companyId, queryParams]),
      enabled: !!auth0Id && !!queryParams
    });
  };

  return {
    setQueryParams,
    myJobApplications,
    myJobApplicationsError,
    myJobApplicationsIsPending,
    createJobApplication,
    updateStatus,
    checkOfferData,
    getJobApplications
  };
}

export { useJobApplications };
