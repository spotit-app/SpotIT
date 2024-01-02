import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { useCallback } from 'react';
import axios from 'axios';
import { slugifyAuth0Id } from 'utils';
import {
  DeleteResponse,
  ReadTechSkill,
  ReadTechSkillName,
  TechSkillsFormType,
  WriteTechSkill,
  WriteTechSkillName
} from 'types/profile';

function useTechSkills() {
  const { user, getAccessTokenSilently } = useAuth0();
  const auth0Id = user?.sub;
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: techSkills,
    error: techSkillsError,
    isPending: techSkillsIsPending
  } = useQuery({
    queryKey: ['techSkills', auth0Id],
    queryFn: useCallback(async (): Promise<ReadTechSkill[]> => {
      return await axios
        .get(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/techSkill`, await axiosOptions())
        .then((res) => res.data);
    }, [auth0Id]),
    enabled: !!auth0Id
  });

  const createTechSkill = useMutation({
    mutationFn: useCallback(async (techSkill: TechSkillsFormType): Promise<ReadTechSkill> => {
      const newTechSkill: WriteTechSkill = {
        techSkillName:
          techSkill.techSkillName === 'Inna'
            ? techSkill.customTechSkillName
            : techSkill.techSkillName,
        skillLevel: techSkill.techSkillLevel
      };
      return await axios
        .post(
          `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/techSkill`,
          newTechSkill,
          await axiosOptions()
        )
        .then((res) => res.data);
    }, []),
    onSuccess: (createdTechSkill: ReadTechSkill) =>
      queryClient.setQueryData(['techSkills', auth0Id], (prev: ReadTechSkill[]) => [
        ...prev,
        createdTechSkill
      ])
  });

  const deleteTechSkill = useMutation({
    mutationFn: useCallback(
      async (id: number): Promise<DeleteResponse> => {
        return await axios
          .delete(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/techSkill/${id}`,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: ({ id }) => {
      queryClient.setQueryData(['techSkills', auth0Id], (prev: ReadTechSkill[]) => [
        ...prev.filter((techSkill) => techSkill.id !== id)
      ]);
    }
  });

  const {
    data: techSkillNames,
    error: techSkillNamesError,
    isPending: techSkillNamesIsPending
  } = useQuery({
    queryKey: ['techSkillNames'],
    queryFn: useCallback(async (): Promise<ReadTechSkillName[]> => {
      return await axios.get(`/api/techSkillName`, await axiosOptions()).then((res) => res.data);
    }, [])
  });

  const createTechSkillName = useMutation({
    mutationFn: useCallback(
      async (techSkillName: WriteTechSkillName): Promise<ReadTechSkillName> => {
        return await axios
          .post(`/api/techSkillName`, techSkillName, await axiosOptions())
          .then((res) => res.data);
      },
      []
    ),
    onSuccess: (createdTechSkillName: ReadTechSkillName) =>
      queryClient.setQueryData(['techSkillNames'], (prev: ReadTechSkillName[]) => [
        ...prev,
        createdTechSkillName
      ])
  });

  const deleteTechSkillName = useMutation({
    mutationFn: useCallback(async (id: number): Promise<DeleteResponse> => {
      return await axios
        .delete(`/api/techSkillName/${id}`, await axiosOptions())
        .then((res) => res.data);
    }, []),
    onSuccess: ({ id }) => {
      queryClient.setQueryData(['techSkillNames'], (prev: ReadTechSkillName[]) => [
        ...prev.filter((techSkillName) => techSkillName.id !== id)
      ]);
    }
  });

  return {
    techSkills,
    techSkillsError,
    techSkillsIsPending,
    techSkillNames,
    techSkillNamesError,
    techSkillNamesIsPending,
    createTechSkill,
    createTechSkillName,
    deleteTechSkill,
    deleteTechSkillName
  };
}

export { useTechSkills };
