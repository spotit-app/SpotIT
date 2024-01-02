import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { useCallback } from 'react';
import axios from 'axios';
import { DeleteResponse, ReadSocial, WriteSocial } from 'types/profile';
import { slugifyAuth0Id } from 'utils';

function useSocials() {
  const { user, getAccessTokenSilently } = useAuth0();
  const auth0Id = user?.sub;
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: socials,
    error: socialsError,
    isPending: socialsIsPending
  } = useQuery({
    queryKey: ['socials', auth0Id],
    queryFn: useCallback(async (): Promise<ReadSocial[]> => {
      return await axios
        .get(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/social`, await axiosOptions())
        .then((res) => res.data);
    }, [auth0Id]),
    enabled: !!auth0Id
  });

  const createSocial = useMutation({
    mutationFn: useCallback(async (social: WriteSocial): Promise<ReadSocial> => {
      return await axios
        .post(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/social`, social, await axiosOptions())
        .then((res) => res.data);
    }, []),
    onSuccess: (createdSocial: ReadSocial) =>
      queryClient.setQueryData(['socials', auth0Id], (prev: ReadSocial[]) => [
        ...prev,
        createdSocial
      ])
  });

  const updateSocial = useMutation({
    mutationFn: useCallback(
      async (updatedSocial: ReadSocial): Promise<ReadSocial> => {
        return await axios
          .put(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/social/${updatedSocial.id}`,
            updatedSocial,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: (updatedSocial: ReadSocial) =>
      queryClient.setQueryData(['socials', auth0Id], (prev: ReadSocial[]) => [
        ...prev.map((social) => (social.id === updatedSocial.id ? updatedSocial : social))
      ])
  });

  const deleteSocial = useMutation({
    mutationFn: useCallback(
      async (id: number): Promise<DeleteResponse> => {
        return await axios
          .delete(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/social/${id}`, await axiosOptions())
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: ({ id }) => {
      queryClient.setQueryData(['socials', auth0Id], (prev: ReadSocial[]) => [
        ...prev.filter((social) => social.id !== id)
      ]);
    }
  });

  return {
    socials,
    socialsError,
    socialsIsPending,
    createSocial,
    deleteSocial,
    updateSocial
  };
}

export { useSocials };
