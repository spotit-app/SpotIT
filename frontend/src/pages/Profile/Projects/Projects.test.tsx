import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { showModal, slugifyAuth0Id } from 'utils';
import Projects from '.';

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

describe('Projects Page Component', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/project`)
      .reply(200, [
        {
          id: 1,
          name: 'testProject1',
          description: 'testDescription1',
          projectUrl: 'https://test.project.com/'
        }
      ]);

    nock('http://localhost:80')
      .post(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/project`)
      .reply(200, {
        id: 2,
        name: 'testProject2',
        description: 'testDescription2',
        projectUrl: 'https://test.project.com/'
      });

    nock('http://localhost:80')
      .put(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/project/1`)
      .reply(200, {
        id: 1,
        name: 'updatedTestProject1',
        description: 'testDescription1',
        projectUrl: 'https://test.project.com/'
      });

    nock('http://localhost:80')
      .delete(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/project/1`)
      .reply(200, { id: 1 });

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <Projects />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    const pageTitle = screen.getByText('Projekty');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    await waitFor(() => {
      const testProject = screen.getByText('testProject1');
      expect(testProject).toBeInTheDocument();
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
    const projectName = screen.getByLabelText('Nazwa projektu');
    fireEvent.change(projectName, { target: { value: 'test' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const errors = screen.getByText('Obowiązkowe');
      expect(errors).toBeInTheDocument();
    });
  });

  test('form submits with valid data', async () => {
    const projectName = screen.getByLabelText('Nazwa projektu');
    fireEvent.change(projectName, { target: { value: 'testProject2' } });

    const projectDescription = screen.getByLabelText('Opis');
    fireEvent.change(projectDescription, { target: { value: 'testDescription2' } });

    const projectUrl = screen.getByLabelText('Link do projektu');
    fireEvent.change(projectUrl, { target: { value: 'https://test.project.com/' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const newTestProject = screen.getByText('testProject2');
      expect(newTestProject).toBeInTheDocument();
    });
  });

  test('project update works correctly', async () => {
    await waitFor(() => {
      const editButton = screen.getByText('Edytuj');
      fireEvent.click(editButton);
    });

    const projectName = screen.getByLabelText('Nazwa projektu');
    fireEvent.change(projectName, { target: { value: 'updatedTestProject1' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const updatedTestProject = screen.getByText('updatedTestProject1');
      expect(updatedTestProject).toBeInTheDocument();
    });
  });

  test('project delete works correctly', async () => {
    await waitFor(() => {
      const testProject = screen.queryByText('testProject1');
      expect(testProject).toBeInTheDocument();
    });

    await waitFor(() => {
      const deleteButton = screen.getByText('Usuń');
      fireEvent.click(deleteButton);
    });

    const testProject = screen.queryByText('testProject1');
    expect(testProject).not.toBeInTheDocument();

    await waitFor(() => {
      const noContent = screen.getByTestId('no-content');
      expect(noContent).toBeInTheDocument();
    });
  });
});
