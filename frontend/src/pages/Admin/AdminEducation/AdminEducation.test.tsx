import { act, render, screen, waitFor, fireEvent, cleanup } from '@testing-library/react';
import AdminEducation from '.';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { showModal } from 'utils';

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

describe('AdminEducation', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/educationLevel`)
      .reply(200, [
        { id: 1, name: 'testEducationLevel1' },
        { id: 2, name: 'testEducationLevel2' }
      ]);
    nock('http://localhost:80')
      .post(`/api/educationLevel`)
      .reply(201, { id: 3, name: 'testEducationLevel3' });
    nock('http://localhost:80')
      .put(`/api/educationLevel/1`)
      .reply(200, { id: 1, name: 'testEducationLevel1Updated' });
    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <AdminEducation />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    expect(screen.getByText('Poziomy wykształcenia')).toBeInTheDocument();
    await waitFor(() => expect(screen.getByText('testEducationLevel1')).toBeInTheDocument());
  });

  test('button triggers formToggle function', async () => {
    const addButton = screen.getByTestId('add-button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('add new education level', async () => {
    const addButton = screen.getByTestId('add-button');
    fireEvent.click(addButton);

    const name = screen.getByLabelText('Nazwa');
    fireEvent.change(name, { target: { value: 'testEducationLevel3' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('testEducationLevel3')).toBeInTheDocument();
    });
  });

  test('edit education level', async () => {
    await waitFor(() => {
      const editButton = screen.queryAllByText('Edytuj')[0];
      fireEvent.click(editButton);
    });

    const name = screen.getByLabelText('Nazwa');
    fireEvent.change(name, { target: { value: 'testEducationLevel1Updated' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('testEducationLevel1Updated')).toBeInTheDocument();
    });
  });
});

describe('AdminEducation without data', () => {
  beforeEach(() => {
    nock('http://localhost:80').get(`/api/educationLevel`).reply(200, []);
    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <AdminEducation />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    expect(screen.getByText('Poziomy wykształcenia')).toBeInTheDocument();
    await waitFor(() => expect(screen.getByTestId('no-content')).toBeInTheDocument());
  });
});
