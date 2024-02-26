import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { useCallback } from 'react';
import axios from 'axios';
import { slugifyAuth0Id } from 'utils';
import {
  DeleteResponse,
  ReadForeignLanguage,
  ReadForeignLanguageName,
  WriteForeignLanguage,
  WriteForeignLanguageName
} from 'types/profile';

function useForeignLanguages() {
  const { user, getAccessTokenSilently } = useAuth0();
  const auth0Id = user?.sub;
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: foreignLanguages,
    error: foreignLanguagesError,
    isPending: foreignLanguagesIsPending
  } = useQuery({
    queryKey: ['foreignLanguages', auth0Id],
    queryFn: useCallback(async (): Promise<ReadForeignLanguage[]> => {
      return await axios
        .get(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/foreignLanguage`, await axiosOptions())
        .then((res) => res.data);
    }, [auth0Id]),
    enabled: !!auth0Id
  });

  const createForeignLanguage = useMutation({
    mutationFn: useCallback(
      async (foreignLanguage: WriteForeignLanguage): Promise<ReadForeignLanguage> => {
        return await axios
          .post(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/foreignLanguage`,
            foreignLanguage,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      []
    ),
    onSuccess: (createdForeignLanguage: ReadForeignLanguage) =>
      queryClient.setQueryData(['foreignLanguages', auth0Id], (prev: ReadForeignLanguage[]) => [
        ...prev,
        createdForeignLanguage
      ])
  });

  const deleteForeignLanguage = useMutation({
    mutationFn: useCallback(
      async (id: number): Promise<DeleteResponse> => {
        return await axios
          .delete(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/foreignLanguage/${id}`,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: ({ id }) => {
      queryClient.setQueryData(['foreignLanguages', auth0Id], (prev: ReadForeignLanguage[]) => [
        ...prev.filter((foreignLanguage) => foreignLanguage.id !== id)
      ]);
    }
  });

  const {
    data: foreignLanguageNames,
    error: foreignLanguageNamesError,
    isPending: foreignLanguageNamesIsPending
  } = useQuery({
    queryKey: ['foreignLanguageNames'],
    queryFn: useCallback(async (): Promise<ReadForeignLanguageName[]> => {
      return await axios.get(`/api/foreignLanguageName`).then((res) => res.data);
    }, [])
  });

  const createForeignLanguageName = useMutation({
    mutationFn: useCallback(
      async (foreignLanguageName: WriteForeignLanguageName): Promise<ReadForeignLanguageName> => {
        return await axios
          .post(`/api/foreignLanguageName`, foreignLanguageName, await axiosOptions())
          .then((res) => res.data);
      },
      []
    ),
    onSuccess: (createdForeignLanguageName: ReadForeignLanguageName) =>
      queryClient.setQueryData(['foreignLanguageNames'], (prev: ReadForeignLanguageName[]) => [
        ...prev,
        createdForeignLanguageName
      ])
  });

  const deleteForeignLanguageName = useMutation({
    mutationFn: useCallback(async (id: number): Promise<DeleteResponse> => {
      return await axios
        .delete(`/api/foreignLanguageName/${id}`, await axiosOptions())
        .then((res) => res.data);
    }, []),
    onSuccess: ({ id }) => {
      queryClient.setQueryData(['foreignLanguageNames'], (prev: ReadForeignLanguageName[]) => [
        ...prev.filter((foreignLanguageName) => foreignLanguageName.id !== id)
      ]);
    }
  });

  return {
    foreignLanguages,
    foreignLanguagesError,
    foreignLanguagesIsPending,
    foreignLanguageNames,
    foreignLanguageNamesError,
    foreignLanguageNamesIsPending,
    createForeignLanguage,
    createForeignLanguageName,
    deleteForeignLanguage,
    deleteForeignLanguageName
  };
}

export { useForeignLanguages };
