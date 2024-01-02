import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { showModal, slugifyAuth0Id } from 'utils';
import Courses from '.';

jest.mock('../../../utils/modal', () => ({
  showModal: jest.fn()
}));

jest.mock('@auth0/auth0-react');

(useAuth0 as jest.Mock).mockReturnValue({
  isAuthenticated: true,
  user: {
    sub: 'auth0|1234567890'
  },
  getAccessTokenSilently: async () => 'testToken'
});

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
      refetchOnWindowFocus: false,
      staleTime: 30000
    }
  }
});

describe('Courses Page Component', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/course`)
      .reply(200, [{ id: 1, name: 'testCourse1', finishDate: '2023-01-01' }]);

    nock('http://localhost:80')
      .post(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/course`)
      .reply(200, { id: 2, name: 'testCourse2', finishDate: '2023-01-01' });

    nock('http://localhost:80')
      .put(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/course/1`)
      .reply(200, {
        id: 1,
        name: 'updatedTestCourse1',
        finishDate: '2023-01-01'
      });

    nock('http://localhost:80')
      .delete(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/course/1`)
      .reply(200, { id: 1 });

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <Courses />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    const pageTitle = screen.getByText('Kursy');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    await waitFor(() => {
      const testCourse = screen.getByText('testCourse1');
      expect(testCourse).toBeInTheDocument();
    });
  });

  test('button triggers formToggle function', async () => {
    const addButton = screen.getByTestId('add-button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('form submits with bad data', async () => {
    const courseName = screen.getByLabelText('Nazwa kursu');
    fireEvent.change(courseName, { target: { value: 'test' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const errors = screen.getByText('Obowiązkowe');
      expect(errors).toBeInTheDocument();
    });
  });

  test('form submits with valid data', async () => {
    const courseName = screen.getByLabelText('Nazwa kursu');
    fireEvent.change(courseName, { target: { value: 'testCourse2' } });

    const courseFinishDate = screen.getByLabelText('Data ukończenia');
    fireEvent.change(courseFinishDate, { target: { value: '2023-01-01' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const newTestCourse = screen.getByText('testCourse2');
      expect(newTestCourse).toBeInTheDocument();
    });
  });

  test('course update works correctly', async () => {
    await waitFor(() => {
      const editButton = screen.getByText('Edytuj');
      fireEvent.click(editButton);
    });

    const courseName = screen.getByLabelText('Nazwa kursu');
    fireEvent.change(courseName, { target: { value: 'updatedTestCourse1' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const updatedTestCourse = screen.getByText('updatedTestCourse1');
      expect(updatedTestCourse).toBeInTheDocument();
    });
  });

  test('course delete works correctly', async () => {
    await waitFor(() => {
      const testCourse = screen.queryByText('testCourse1');
      expect(testCourse).toBeInTheDocument();
    });

    await waitFor(() => {
      const deleteButton = screen.getByText('Usuń');
      fireEvent.click(deleteButton);
    });

    const testCourse = screen.queryByText('testCourse1');
    expect(testCourse).not.toBeInTheDocument();

    await waitFor(() => {
      const noContent = screen.getByTestId('no-content');
      expect(noContent).toBeInTheDocument();
    });
  });
});
