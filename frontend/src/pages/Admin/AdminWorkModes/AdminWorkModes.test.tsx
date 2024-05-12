import { act, render, screen, waitFor, fireEvent, cleanup } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { showModal } from 'utils';
import AdminWorkModes from '.';

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

describe('AdminWorkModes', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/workMode`)
      .reply(200, [
        { id: 1, name: 'testWorkMode1' },
        { id: 2, name: 'testWorkMode2' }
      ]);
    nock('http://localhost:80').post(`/api/workMode`).reply(201, { id: 3, name: 'testWorkMode3' });
    nock('http://localhost:80')
      .put(`/api/workMode/1`)
      .reply(200, { id: 1, name: 'testWorkMode1Updated' });
    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <AdminWorkModes />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    expect(screen.getByText('Tryby pracy')).toBeInTheDocument();
    await waitFor(() => expect(screen.getByText('testWorkMode1')).toBeInTheDocument());
  });

  test('button triggers formToggle function', async () => {
    const addButton = screen.getByTestId('add-button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('add new work mode', async () => {
    const addButton = screen.getByTestId('add-button');
    fireEvent.click(addButton);

    const name = screen.getByLabelText('Nazwa');
    fireEvent.change(name, { target: { value: 'testWorkMode3' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('testWorkMode3')).toBeInTheDocument();
    });
  });

  test('edit work mode', async () => {
    await waitFor(() => {
      const editButton = screen.queryAllByText('Edytuj')[0];
      fireEvent.click(editButton);
    });

    const name = screen.getByLabelText('Nazwa');
    fireEvent.change(name, { target: { value: 'testWorkMode1Updated' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('testWorkMode1Updated')).toBeInTheDocument();
    });
  });
});

describe('AdminWorkModes without data', () => {
  beforeEach(() => {
    nock('http://localhost:80').get(`/api/workMode`).reply(200, []);
    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <AdminWorkModes />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    expect(screen.getByText('Tryby pracy')).toBeInTheDocument();
    await waitFor(() => expect(screen.getByTestId('no-content')).toBeInTheDocument());
  });
});
