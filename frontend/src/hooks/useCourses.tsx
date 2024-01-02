import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { useCallback } from 'react';
import axios from 'axios';
import { DeleteResponse, ReadCourse, WriteCourse } from 'types/profile';
import { slugifyAuth0Id } from 'utils';

function useCourses() {
  const { user, getAccessTokenSilently } = useAuth0();
  const auth0Id = user?.sub;
  const queryClient = useQueryClient();
  const axiosOptions = async () => ({
    headers: {
      Authorization: `Bearer ${await getAccessTokenSilently()}`
    }
  });

  const {
    data: courses,
    error: coursesError,
    isPending: coursesIsPending
  } = useQuery({
    queryKey: ['courses', auth0Id],
    queryFn: useCallback(async (): Promise<ReadCourse[]> => {
      return await axios
        .get(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/course`, await axiosOptions())
        .then((res) => res.data);
    }, [auth0Id]),
    enabled: !!auth0Id
  });

  const createCourse = useMutation({
    mutationFn: useCallback(async (course: WriteCourse): Promise<ReadCourse> => {
      return await axios
        .post(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/course`, course, await axiosOptions())
        .then((res) => res.data);
    }, []),
    onSuccess: (createdCourse: ReadCourse) =>
      queryClient.setQueryData(['courses', auth0Id], (prev: ReadCourse[]) => [
        ...prev,
        createdCourse
      ])
  });

  const updateCourse = useMutation({
    mutationFn: useCallback(
      async (updatedCourse: ReadCourse): Promise<ReadCourse> => {
        return await axios
          .put(
            `/api/userAccount/${slugifyAuth0Id(auth0Id!)}/course/${updatedCourse.id}`,
            updatedCourse,
            await axiosOptions()
          )
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: (updatedCourse: ReadCourse) =>
      queryClient.setQueryData(['courses', auth0Id], (prev: ReadCourse[]) => [
        ...prev.map((course) => (course.id === updatedCourse.id ? updatedCourse : course))
      ])
  });

  const deleteCourse = useMutation({
    mutationFn: useCallback(
      async (id: number): Promise<DeleteResponse> => {
        return await axios
          .delete(`/api/userAccount/${slugifyAuth0Id(auth0Id!)}/course/${id}`, await axiosOptions())
          .then((res) => res.data);
      },
      [auth0Id]
    ),
    onSuccess: ({ id }) => {
      queryClient.setQueryData(['courses', auth0Id], (prev: ReadCourse[]) => [
        ...prev.filter((course) => course.id !== id)
      ]);
    }
  });

  return {
    courses,
    coursesError,
    coursesIsPending,
    createCourse,
    deleteCourse,
    updateCourse
  };
}

export { useCourses };
