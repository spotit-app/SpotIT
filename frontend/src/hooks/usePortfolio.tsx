import { Portfolio } from '@/types/profile';
import { slugifyAuth0Id } from '@/utils';
import { useAuth0 } from '@auth0/auth0-react';
import { useMutation, useQuery } from '@tanstack/react-query';
import axios from 'axios';
import { useCallback } from 'react';

function usePortfolio() {
  const { user, getAccessTokenSilently } = useAuth0();
  const auth0Id = user?.sub;
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: portfolioUrl,
    error: portfolioUrlError,
    isPending: portfolioUrlIsPending
  } = useQuery({
    queryKey: ['portfolioUrl', auth0Id],
    queryFn: useCallback(async (): Promise<string> => {
      return await axios
        .get(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/portfolio`, await axiosOptions())
        .then((res) => res.data);
    }, [auth0Id]),
    enabled: !!auth0Id
  });

  const createPortfolio = useMutation({
    mutationFn: useCallback(async (): Promise<Portfolio> => {
      return await axios
        .post(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/portfolio`, {}, await axiosOptions())
        .then((res) => res.data);
    }, [])
  });

  const updatePortfolio = useMutation({
    mutationFn: useCallback(async (): Promise<Portfolio> => {
      return await axios
        .put(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/portfolio`, {}, await axiosOptions())
        .then((res) => res.data);
    }, [])
  });

  return {
    portfolioUrl,
    portfolioUrlError,
    portfolioUrlIsPending,
    createPortfolio,
    updatePortfolio
  };
}

export { usePortfolio };
