import { PortfolioPage, PortfoliosQueryParams } from 'types/profile';
import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import { useCallback, useState } from 'react';

function useEmployeeSearch() {
  const [queryParams, setQueryParams] = useState<PortfoliosQueryParams>({
    page: 0,
    techSkillNameIds: [],
    foreignLanguageNameIds: []
  });

  const {
    data: portfolios,
    error: portfoliosError,
    isPending: portfoliosIsPending
  } = useQuery({
    queryKey: ['portfolios', queryParams],
    queryFn: useCallback(async (): Promise<PortfolioPage> => {
      const queryData = new URLSearchParams();

      queryData.append('page', queryParams.page.toString());

      queryParams.techSkillNameIds.forEach((techSkillId) => {
        queryData.append('techSkillNameIds', techSkillId.toString());
      });

      queryParams.foreignLanguageNameIds.forEach((languageId) => {
        queryData.append('foreignLanguageNameIds', languageId.toString());
      });

      return await axios.get(`/api/portfolios`, { params: queryData }).then((res) => res.data);
    }, [queryParams]),
    enabled: !!queryParams
  });

  return {
    setQueryParams,
    portfolios,
    portfoliosError,
    portfoliosIsPending
  };
}

export { useEmployeeSearch };
