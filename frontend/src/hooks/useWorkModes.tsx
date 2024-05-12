import { ReadWorkMode, WriteWorkMode } from 'types/company';
import { useAuth0 } from '@auth0/auth0-react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import axios from 'axios';
import { useCallback } from 'react';

function useWorkModes() {
  const { getAccessTokenSilently } = useAuth0();
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: workModes,
    error: workModesError,
    isPending: workModesIsPending
  } = useQuery({
    queryKey: ['workModes'],
    queryFn: useCallback(async (): Promise<ReadWorkMode[]> => {
      return await axios.get(`/api/workMode`).then((res) => res.data);
    }, [])
  });

  const createWorkMode = useMutation({
    mutationFn: useCallback(async (workMode: WriteWorkMode): Promise<ReadWorkMode> => {
      return await axios
        .post(`/api/workMode`, workMode, await axiosOptions())
        .then((res) => res.data);
    }, []),
    onSuccess: (createdWorkMode: ReadWorkMode) =>
      queryClient.setQueryData(['workModes'], (prev: ReadWorkMode[]) => [...prev, createdWorkMode])
  });

  const updateWorkMode = useMutation({
    mutationFn: useCallback(async (updatedWorkMode: ReadWorkMode): Promise<ReadWorkMode> => {
      return await axios
        .put(`/api/workMode/${updatedWorkMode.id}`, updatedWorkMode, await axiosOptions())
        .then((res) => res.data);
    }, []),
    onSuccess: (updatedWorkMode: ReadWorkMode) =>
      queryClient.setQueryData(['workModes'], (prev: ReadWorkMode[]) => [
        ...prev.map((workMode) => (workMode.id === updatedWorkMode.id ? updatedWorkMode : workMode))
      ])
  });

  return { workModes, workModesError, workModesIsPending, createWorkMode, updateWorkMode };
}

export { useWorkModes };
