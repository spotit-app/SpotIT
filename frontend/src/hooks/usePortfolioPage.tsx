import { Portfolio } from '@/types/profile';
import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import { useCallback } from 'react';

function usePortfolioPage(portfolioUrl: string) {
  const {
    data: portfolio,
    error: portfolioError,
    isPending: portfolioIsPending
  } = useQuery({
    queryKey: ['portfolio', portfolioUrl],
    queryFn: useCallback(async (): Promise<Portfolio> => {
      return await axios.get(`/api/portfolio/${portfolioUrl}`).then((res) => res.data);
    }, [portfolioUrl]),
    enabled: !!portfolioUrl
  });

  return {
    portfolio,
    portfolioError,
    portfolioIsPending
  };
}

export { usePortfolioPage };
