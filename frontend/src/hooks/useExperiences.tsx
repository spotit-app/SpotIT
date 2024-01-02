import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { useCallback } from 'react';
import axios from 'axios';
import { DeleteResponse, ReadExperience, WriteExperience } from 'types/profile';
import { slugifyAuth0Id } from 'utils';

function useExperiences() {
  const { user, getAccessTokenSilently } = useAuth0();
  const auth0Id = user?.sub;
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: experiences,
    error: experiencesError,
    isPending: experiencesIsPending
  } = useQuery({
    queryKey: ['experiences', auth0Id],
    queryFn: useCallback(async (): Promise<ReadExperience[]> => {
      return await axios
        .get(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/experience`, await axiosOptions())
        .then((res) => res.data);
    }, [auth0Id]),
    enabled: !!auth0Id
  });

  const createExperience = useMutation({
    mutationFn: useCallback(async (experience: WriteExperience): Promise<ReadExperience> => {
      return await axios
        .post(
          `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/experience`,
          experience,
          await axiosOptions()
        )
        .then((res) => res.data);
    }, []),
    onSuccess: (createdExperience: ReadExperience) =>
      queryClient.setQueryData(['experiences', auth0Id], (prev: ReadExperience[]) => [
        ...prev,
        createdExperience
      ])
  });

  const updateExperience = useMutation({
    mutationFn: useCallback(
      async (updatedExperience: ReadExperience): Promise<ReadExperience> => {
        return await axios
          .put(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/experience/${updatedExperience.id}`,
            updatedExperience,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: (updatedExperience: ReadExperience) =>
      queryClient.setQueryData(['experiences', auth0Id], (prev: ReadExperience[]) => [
        ...prev.map((experience) =>
          experience.id === updatedExperience.id ? updatedExperience : experience
        )
      ])
  });

  const deleteExperience = useMutation({
    mutationFn: useCallback(
      async (id: number): Promise<DeleteResponse> => {
        return await axios
          .delete(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/experience/${id}`,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: ({ id }) => {
      queryClient.setQueryData(['experiences', auth0Id], (prev: ReadExperience[]) => [
        ...prev.filter((experience) => experience.id !== id)
      ]);
    }
  });

  return {
    experiences,
    experiencesError,
    experiencesIsPending,
    createExperience,
    deleteExperience,
    updateExperience
  };
}

export { useExperiences };
