import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { IdToken, useAuth0 } from '@auth0/auth0-react';
import { useCallback, useState, useEffect } from 'react';
import axios from 'axios';
import { CreateUser, ReadUser } from 'types/profile';
import { slugifyAuth0Id } from 'utils';
import { Role } from '@/types/auth';

function useUser() {
  const [isAdmin, setIsAdmin] = useState<boolean>(false);
  const [roles, setRoles] = useState<Role[] | null>(null);

  const {
    user,
    getAccessTokenSilently,
    getIdTokenClaims,
    isAuthenticated,
    isLoading,
    loginWithRedirect
  } = useAuth0();

  const auth0Id = user?.sub;
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  async function getRoles() {
    const claims: IdToken | undefined = await getIdTokenClaims();
    const roles: Role[] = claims ? claims['spotit/roles'] : [];
    setRoles(roles);
    setIsAdmin(roles.includes('ADMIN'));
  }

  useEffect(() => {
    auth0Id && getRoles();
  }, [auth0Id]);

  const {
    data: userData,
    error: userDataError,
    isPending: userDataIsPending
  } = useQuery({
    queryKey: ['user', auth0Id],
    queryFn: useCallback(async () => {
      return await axios
        .get(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}`, await axiosOptions())
        .then((res) => res.data);
    }, [auth0Id]),
    enabled: !!auth0Id
  });

  const createUser = useMutation({
    mutationFn: useCallback(async (userToCreate: CreateUser) => {
      return await axios
        .post(`/api/userAccount`, userToCreate, await axiosOptions())
        .then((res) => res.data);
    }, [])
  });

  const updateUser = useMutation({
    mutationFn: useCallback(
      async (updatedUser: FormData): Promise<ReadUser> => {
        return await axios
          .put(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}`, updatedUser, await axiosOptions())
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: (updatedUser: ReadUser) => queryClient.setQueryData(['user', auth0Id], updatedUser)
  });

  const userDataOthers = {
    description: userData?.description,
    cvClause: userData?.cvClause
  };

  const userPersonalData = {
    firstName: userData?.firstName,
    lastName: userData?.lastName,
    email: userData?.email,
    phoneNumber: userData?.phoneNumber,
    profilePictureUrl: userData?.profilePictureUrl,
    position: userData?.position,
    isOpen: userData?.isOpen
  };

  const userProfileData = {
    firstName: userData?.firstName,
    lastName: userData?.lastName,
    profilePicture: userData?.profilePictureUrl
  };

  const userName =
    userProfileData?.firstName && userProfileData?.lastName && !userDataIsPending
      ? userProfileData.firstName + ' ' + userProfileData.lastName
      : user?.name || '';

  const userPicture =
    userProfileData?.profilePicture && !userDataIsPending
      ? userProfileData.profilePicture
      : user?.picture || '';

  return {
    userData,
    userDataOthers,
    userDataError,
    userDataIsPending,
    createUser,
    updateUser,
    user,
    userPersonalData,
    userProfileData,
    userName,
    userPicture,
    isAdmin,
    isAuthenticated,
    isLoading,
    loginWithRedirect,
    roles
  };
}

export { useUser };
