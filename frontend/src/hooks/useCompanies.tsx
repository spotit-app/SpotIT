import { ReadCompany, WriteCompany } from 'types/company';
import { useAuth0 } from '@auth0/auth0-react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useCallback } from 'react';
import axios from 'axios';
import { slugifyAuth0Id } from 'utils';

function useCompanies() {
  const { user, getAccessTokenSilently } = useAuth0();
  const auth0Id = user?.sub;
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: companies,
    error: companiesError,
    isPending: companiesIsPending
  } = useQuery({
    queryKey: ['companies', auth0Id],
    queryFn: useCallback(async (): Promise<ReadCompany[]> => {
      return await axios
        .get(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/company`, await axiosOptions())
        .then((res) => res.data);
    }, [auth0Id]),
    enabled: !!auth0Id
  });

  const getCompany = (companyId: number) => {
    return useQuery({
      queryKey: ['company', companyId],
      queryFn: useCallback(async (): Promise<ReadCompany> => {
        return await axios.get(`/api/company/${companyId}`).then((res) => res.data);
      }, [auth0Id, companyId])
    });
  };

  const createCompany = useMutation({
    mutationFn: useCallback(async (company: WriteCompany): Promise<ReadCompany> => {
      return await axios
        .post(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/company`, company, await axiosOptions())
        .then((res) => res.data);
    }, []),
    onSuccess: (createdCompany: ReadCompany) =>
      queryClient.setQueryData(['companies', auth0Id], (prev: ReadCompany[]) => [
        ...prev,
        createdCompany
      ])
  });

  const updateCompany = useMutation({
    mutationFn: useCallback(
      async (data: { id: number; formData: FormData }): Promise<ReadCompany> => {
        return await axios
          .put(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/company/${data.id}`,
            data.formData,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: (updatedCompany: ReadCompany) => {
      queryClient.setQueryData(['companies', auth0Id], (prev: ReadCompany[]) => [
        ...prev.map((company) => (company.id === updatedCompany.id ? updatedCompany : company))
      ]);
      queryClient.setQueryData(['company', updatedCompany.id], updatedCompany);
    }
  });

  return {
    companies,
    companiesError,
    companiesIsPending,
    createCompany,
    getCompany,
    updateCompany
  };
}

export { useCompanies };
