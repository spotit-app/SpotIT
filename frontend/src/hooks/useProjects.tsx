import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { useCallback } from 'react';
import axios from 'axios';
import { DeleteResponse, ReadProject, WriteProject } from 'types/profile';
import { slugifyAuth0Id } from 'utils';

function useProjects() {
  const { user, getAccessTokenSilently } = useAuth0();
  const auth0Id = user?.sub;
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: projects,
    error: projectsError,
    isPending: projectsIsPending
  } = useQuery({
    queryKey: ['projects', auth0Id],
    queryFn: useCallback(async (): Promise<ReadProject[]> => {
      return await axios
        .get(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/project`, await axiosOptions())
        .then((res) => res.data);
    }, [auth0Id]),
    enabled: !!auth0Id
  });

  const createProject = useMutation({
    mutationFn: useCallback(async (project: WriteProject): Promise<ReadProject> => {
      return await axios
        .post(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/project`, project, await axiosOptions())
        .then((res) => res.data);
    }, []),
    onSuccess: (createdProject: ReadProject) =>
      queryClient.setQueryData(['projects', auth0Id], (prev: ReadProject[]) => [
        ...prev,
        createdProject
      ])
  });

  const updateProject = useMutation({
    mutationFn: useCallback(
      async (updatedProject: ReadProject): Promise<ReadProject> => {
        return await axios
          .put(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/project/${updatedProject.id}`,
            updatedProject,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: (updatedProject: ReadProject) =>
      queryClient.setQueryData(['projects', auth0Id], (prev: ReadProject[]) => [
        ...prev.map((project) => (project.id === updatedProject.id ? updatedProject : project))
      ])
  });

  const deleteProject = useMutation({
    mutationFn: useCallback(
      async (id: number): Promise<DeleteResponse> => {
        return await axios
          .delete(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/project/${id}`,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: ({ id }) => {
      queryClient.setQueryData(['projects', auth0Id], (prev: ReadProject[]) => [
        ...prev.filter((project) => project.id !== id)
      ]);
    }
  });

  return {
    projects,
    projectsError,
    projectsIsPending,
    createProject,
    deleteProject,
    updateProject
  };
}

export { useProjects };
