import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { useCallback } from 'react';
import axios from 'axios';
import { slugifyAuth0Id } from 'utils';
import {
  DeleteResponse,
  EducationFormType,
  ReadEducation,
  ReadEducationLevel,
  WriteEducation,
  WriteEducationLevel
} from 'types/profile';

function useEducations() {
  const { user, getAccessTokenSilently } = useAuth0();
  const auth0Id = user?.sub;
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: educations,
    error: educationsError,
    isPending: educationsIsPending
  } = useQuery({
    queryKey: ['educations', auth0Id],
    queryFn: useCallback(async (): Promise<ReadEducation[]> => {
      return await axios
        .get(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/education`, await axiosOptions())
        .then((res) => res.data);
    }, [auth0Id]),
    enabled: !!auth0Id
  });

  const createEducation = useMutation({
    mutationFn: useCallback(async (education: EducationFormType): Promise<ReadEducation> => {
      const newEducation: WriteEducation = {
        schoolName: education.schoolName,
        educationLevel:
          education.educationLevel === 'Inny'
            ? education.customEducationLevel
            : education.educationLevel,
        faculty: education.faculty,
        startDate: education.startDate,
        endDate: education.endDate
      };
      return await axios
        .post(
          `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/education`,
          newEducation,
          await axiosOptions()
        )
        .then((res) => res.data);
    }, []),
    onSuccess: (createdEducation: ReadEducation) =>
      queryClient.setQueryData(['educations', auth0Id], (prev: ReadEducation[]) => [
        ...prev,
        createdEducation
      ])
  });

  const deleteEducation = useMutation({
    mutationFn: useCallback(
      async (id: number): Promise<DeleteResponse> => {
        return await axios
          .delete(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/education/${id}`,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: ({ id }) => {
      queryClient.setQueryData(['educations', auth0Id], (prev: ReadEducation[]) => [
        ...prev.filter((education) => education.id !== id)
      ]);
    }
  });

  const {
    data: educationLevels,
    error: educationLevelsError,
    isPending: educationLevelsIsPending
  } = useQuery({
    queryKey: ['educationLevel'],
    queryFn: useCallback(async (): Promise<ReadEducationLevel[]> => {
      return await axios.get(`/api/educationLevel`, await axiosOptions()).then((res) => res.data);
    }, [])
  });

  const createEducationLevel = useMutation({
    mutationFn: useCallback(
      async (educationLevel: WriteEducationLevel): Promise<ReadEducationLevel> => {
        return await axios
          .post(`/api/educationLevel`, educationLevel, await axiosOptions())
          .then((res) => res.data);
      },
      []
    ),
    onSuccess: (createdEducationLevel: ReadEducationLevel) =>
      queryClient.setQueryData(['educationLevel'], (prev: ReadEducationLevel[]) => [
        ...prev,
        createdEducationLevel
      ])
  });

  const updateEducationLevel = useMutation({
    mutationFn: useCallback(
      async (educationLevel: ReadEducationLevel): Promise<ReadEducationLevel> => {
        return await axios
          .put(`/api/educationLevel/${educationLevel.id}`, educationLevel, await axiosOptions())
          .then((res) => res.data);
      },
      []
    ),
    onSuccess: (updatedEducationLevel: ReadEducationLevel) =>
      queryClient.setQueryData(['educationLevel'], (prev: ReadEducationLevel[]) => [
        ...prev.map((educationLevel) =>
          educationLevel.id === updatedEducationLevel.id ? updatedEducationLevel : educationLevel
        )
      ])
  });

  const deleteEducationLevel = useMutation({
    mutationFn: useCallback(async (id: number): Promise<DeleteResponse> => {
      return await axios
        .delete(`/api/educationLevel/${id}`, await axiosOptions())
        .then((res) => res.data);
    }, []),
    onSuccess: ({ id }) => {
      queryClient.setQueryData(['educationLevel'], (prev: ReadEducationLevel[]) => [
        ...prev.filter((educationLevel) => educationLevel.id !== id)
      ]);
    }
  });

  return {
    educations,
    educationsError,
    educationsIsPending,
    createEducation,
    deleteEducation,
    educationLevels,
    educationLevelsError,
    educationLevelsIsPending,
    createEducationLevel,
    updateEducationLevel,
    deleteEducationLevel
  };
}

export { useEducations };
