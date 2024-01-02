import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { useCallback } from 'react';
import axios from 'axios';
import { DeleteResponse, ReadInterest, WriteInterest } from 'types/profile';
import { slugifyAuth0Id } from 'utils';

function useInterests() {
  const { user, getAccessTokenSilently } = useAuth0();
  const auth0Id = user?.sub;
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: interests,
    error: interestsError,
    isPending: interestsIsPending
  } = useQuery({
    queryKey: ['interests', auth0Id],
    queryFn: useCallback(async (): Promise<ReadInterest[]> => {
      return await axios
        .get(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/interest`, await axiosOptions())
        .then((res) => res.data);
    }, [auth0Id]),
    enabled: !!auth0Id
  });

  const createInterest = useMutation({
    mutationFn: useCallback(async (interest: WriteInterest): Promise<ReadInterest> => {
      return await axios
        .post(
          `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/interest`,
          interest,
          await axiosOptions()
        )
        .then((res) => res.data);
    }, []),
    onSuccess: (createdInterest: ReadInterest) =>
      queryClient.setQueryData(['interests', auth0Id], (prev: ReadInterest[]) => [
        ...prev,
        createdInterest
      ])
  });

  const updateInterest = useMutation({
    mutationFn: useCallback(
      async (updatedInterest: ReadInterest): Promise<ReadInterest> => {
        return await axios
          .put(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/interest/${updatedInterest.id}`,
            updatedInterest,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: (updatedInterest: ReadInterest) =>
      queryClient.setQueryData(['interests', auth0Id], (prev: ReadInterest[]) => [
        ...prev.map((interest) => (interest.id === updatedInterest.id ? updatedInterest : interest))
      ])
  });

  const deleteInterest = useMutation({
    mutationFn: useCallback(
      async (id: number): Promise<DeleteResponse> => {
        return await axios
          .delete(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/interest/${id}`,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: ({ id }) => {
      queryClient.setQueryData(['interests', auth0Id], (prev: ReadInterest[]) => [
        ...prev.filter((interest) => interest.id !== id)
      ]);
    }
  });

  return {
    interests,
    interestsError,
    interestsIsPending,
    createInterest,
    deleteInterest,
    updateInterest
  };
}

export { useInterests };
