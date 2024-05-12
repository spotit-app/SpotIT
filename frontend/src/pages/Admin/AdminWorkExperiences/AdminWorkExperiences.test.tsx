import { act, render, screen, waitFor, fireEvent, cleanup } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { showModal } from 'utils';
import AdminWorkExperiences from '.';

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

describe('AdminWorkExperiences', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/workExperience`)
      .reply(200, [
        { id: 1, name: 'testWorkExperience1' },
        { id: 2, name: 'testWorkExperience2' }
      ]);
    nock('http://localhost:80')
      .post(`/api/workExperience`)
      .reply(201, { id: 3, name: 'testWorkExperience3' });
    nock('http://localhost:80')
      .put(`/api/workExperience/1`)
      .reply(200, { id: 1, name: 'testWorkExperience1Updated' });
    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <AdminWorkExperiences />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    expect(screen.getByText('Poziom doświadczenia')).toBeInTheDocument();
    await waitFor(() => expect(screen.getByText('testWorkExperience1')).toBeInTheDocument());
  });

  test('button triggers formToggle function', async () => {
    const addButton = screen.getByTestId('add-button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('add new work experience', async () => {
    const addButton = screen.getByTestId('add-button');
    fireEvent.click(addButton);

    const name = screen.getByLabelText('Nazwa');
    fireEvent.change(name, { target: { value: 'testWorkExperience3' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('testWorkExperience3')).toBeInTheDocument();
    });
  });

  test('edit work experience', async () => {
    await waitFor(() => {
      const editButton = screen.queryAllByText('Edytuj')[0];
      fireEvent.click(editButton);
    });

    const name = screen.getByLabelText('Nazwa');
    fireEvent.change(name, { target: { value: 'testWorkExperience1Updated' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('testWorkExperience1Updated')).toBeInTheDocument();
    });
  });
});

describe('AdminWorkExperiences without data', () => {
  beforeEach(() => {
    nock('http://localhost:80').get(`/api/workExperience`).reply(200, []);
    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <AdminWorkExperiences />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    expect(screen.getByText('Poziom doświadczenia')).toBeInTheDocument();
    await waitFor(() => expect(screen.getByTestId('no-content')).toBeInTheDocument());
  });
});
