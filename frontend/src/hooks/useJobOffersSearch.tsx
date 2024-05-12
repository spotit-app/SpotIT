import { JobOfferPage, JobOffersQueryParams } from 'types/company';
import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import { useCallback, useState } from 'react';

function useJobOffersSearch() {
  const [queryParams, setQueryParams] = useState<JobOffersQueryParams>({
    page: 0,
    techSkillNameIds: [],
    foreignLanguageNameIds: [],
    experienceNameIds: [],
    workModeIds: []
  });

  const {
    data: jobOffers,
    error: jobOffersError,
    isPending: jobOffersisPending
  } = useQuery({
    queryKey: ['joboffers', queryParams],
    queryFn: useCallback(async (): Promise<JobOfferPage> => {
      const queryData = new URLSearchParams();

      queryData.append('page', queryParams.page.toString());

      queryParams.techSkillNameIds.forEach((techSkillId) => {
        queryData.append('techSkillNameIds', techSkillId.toString());
      });

      queryParams.foreignLanguageNameIds.forEach((foreignLanguageId) => {
        queryData.append('foreignLanguageNameIds', foreignLanguageId.toString());
      });

      queryParams.experienceNameIds.forEach((experienceId) => {
        queryData.append('experienceNameIds', experienceId.toString());
      });

      queryParams.workModeIds.forEach((workModeId) => {
        queryData.append('workModeIds', workModeId.toString());
      });

      return await axios.get(`/api/jobOffer`, { params: queryData }).then((res) => res.data);
    }, [queryParams]),
    enabled: !!queryParams
  });

  return {
    setQueryParams,
    jobOffers,
    jobOffersError,
    jobOffersisPending
  };
}

export { useJobOffersSearch };
