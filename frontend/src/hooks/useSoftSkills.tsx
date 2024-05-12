import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { useCallback } from 'react';
import axios from 'axios';
import { slugifyAuth0Id } from 'utils';
import {
  DeleteResponse,
  ReadSoftSkill,
  ReadSoftSkillName,
  SoftSkillsFormType,
  WriteSoftSkill,
  WriteSoftSkillName
} from 'types/profile';

function useSoftSkills() {
  const { user, getAccessTokenSilently } = useAuth0();
  const auth0Id = user?.sub;
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: softSkills,
    error: softSkillsError,
    isPending: softSkillsIsPending
  } = useQuery({
    queryKey: ['softSkills', auth0Id],
    queryFn: useCallback(async (): Promise<ReadSoftSkill[]> => {
      return await axios
        .get(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/softSkill`, await axiosOptions())
        .then((res) => res.data);
    }, [auth0Id]),
    enabled: !!auth0Id
  });

  const createSoftSkill = useMutation({
    mutationFn: useCallback(async (softSkill: SoftSkillsFormType): Promise<ReadSoftSkill> => {
      const newSoftSkill: WriteSoftSkill = {
        softSkillName:
          softSkill.softSkillName === 'Inna'
            ? softSkill.customSoftSkillName
            : softSkill.softSkillName,
        skillLevel: softSkill.softSkillLevel
      };
      return await axios
        .post(
          `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/softSkill`,
          newSoftSkill,
          await axiosOptions()
        )
        .then((res) => res.data);
    }, []),
    onSuccess: (createdSoftSkill: ReadSoftSkill) =>
      queryClient.setQueryData(['softSkills', auth0Id], (prev: ReadSoftSkill[]) => [
        ...prev,
        createdSoftSkill
      ])
  });

  const deleteSoftSkill = useMutation({
    mutationFn: useCallback(
      async (id: number): Promise<DeleteResponse> => {
        return await axios
          .delete(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/softSkill/${id}`,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: ({ id }) => {
      queryClient.setQueryData(['softSkills', auth0Id], (prev: ReadSoftSkill[]) => [
        ...prev.filter((softSkill) => softSkill.id !== id)
      ]);
    }
  });

  const {
    data: softSkillNames,
    error: softSkillNamesError,
    isPending: softSkillNamesIsPending
  } = useQuery({
    queryKey: ['softSkillNames'],
    queryFn: useCallback(async (): Promise<ReadSoftSkillName[]> => {
      return await axios.get(`/api/softSkillName`, await axiosOptions()).then((res) => res.data);
    }, [])
  });

  const createSoftSkillName = useMutation({
    mutationFn: useCallback(
      async (softSkillName: WriteSoftSkillName): Promise<ReadSoftSkillName> => {
        return await axios
          .post(`/api/softSkillName`, softSkillName, await axiosOptions())
          .then((res) => res.data);
      },
      []
    ),
    onSuccess: (createdSoftSkillName: ReadSoftSkillName) =>
      queryClient.setQueryData(['softSkillNames'], (prev: ReadSoftSkillName[]) => [
        ...prev,
        createdSoftSkillName
      ])
  });

  const updateSoftSkillName = useMutation({
    mutationFn: useCallback(
      async (updatedSoftSkillName: ReadSoftSkillName): Promise<ReadSoftSkillName> => {
        return await axios
          .put(
            `/api/softSkillName/${updatedSoftSkillName.id}`,
            updatedSoftSkillName,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      []
    ),
    onSuccess: (updatedSoftSkillName: ReadSoftSkillName) =>
      queryClient.setQueryData(['softSkillNames'], (prev: ReadSoftSkillName[]) => [
        ...prev.map((softSkillName) =>
          softSkillName.id === updatedSoftSkillName.id ? updatedSoftSkillName : softSkillName
        )
      ])
  });

  const deleteSoftSkillName = useMutation({
    mutationFn: useCallback(async (id: number): Promise<DeleteResponse> => {
      return await axios
        .delete(`/api/softSkillName/${id}`, await axiosOptions())
        .then((res) => res.data);
    }, []),
    onSuccess: ({ id }) => {
      queryClient.setQueryData(['softSkillNames'], (prev: ReadSoftSkillName[]) => [
        ...prev.filter((softSkillName) => softSkillName.id !== id)
      ]);
    }
  });

  return {
    softSkills,
    softSkillsError,
    softSkillsIsPending,
    softSkillNames,
    softSkillNamesError,
    softSkillNamesIsPending,
    createSoftSkill,
    createSoftSkillName,
    updateSoftSkillName,
    deleteSoftSkill,
    deleteSoftSkillName
  };
}

export { useSoftSkills };
