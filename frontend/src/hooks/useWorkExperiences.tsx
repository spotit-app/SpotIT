import { ReadWorkExperience, WriteWorkExperience } from 'types/company';
import { useAuth0 } from '@auth0/auth0-react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import axios from 'axios';
import { useCallback } from 'react';

function useWorkExperiences() {
  const { getAccessTokenSilently } = useAuth0();
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: workExperiences,
    error: workExperiencesError,
    isPending: workExperiencesIsPending
  } = useQuery({
    queryKey: ['workExperiences'],
    queryFn: useCallback(async (): Promise<ReadWorkExperience[]> => {
      return await axios.get(`/api/workExperience`).then((res) => res.data);
    }, [])
  });

  const createWorkExperience = useMutation({
    mutationFn: useCallback(
      async (workExperience: WriteWorkExperience): Promise<ReadWorkExperience> => {
        return await axios
          .post(`/api/workExperience`, workExperience, await axiosOptions())
          .then((res) => res.data);
      },
      []
    ),
    onSuccess: (createdWorkExperience: ReadWorkExperience) =>
      queryClient.setQueryData(['workExperiences'], (prev: ReadWorkExperience[]) => [
        ...prev,
        createdWorkExperience
      ])
  });

  const updateWorkExperience = useMutation({
    mutationFn: useCallback(
      async (updatedWorkExperience: ReadWorkExperience): Promise<ReadWorkExperience> => {
        return await axios
          .put(
            `/api/workExperience/${updatedWorkExperience.id}`,
            updatedWorkExperience,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      []
    ),
    onSuccess: (updatedWorkExperience: ReadWorkExperience) =>
      queryClient.setQueryData(['workExperiences'], (prev: ReadWorkExperience[]) => [
        ...prev.map((workExperience) =>
          workExperience.id === updatedWorkExperience.id ? updatedWorkExperience : workExperience
        )
      ])
  });

  return {
    workExperiences,
    workExperiencesError,
    workExperiencesIsPending,
    createWorkExperience,
    updateWorkExperience
  };
}

export { useWorkExperiences };
