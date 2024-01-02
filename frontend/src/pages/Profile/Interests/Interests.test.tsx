import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { showModal, slugifyAuth0Id } from 'utils';
import Interests from '.';

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

describe('Interests Page Component', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/interest`)
      .reply(200, [{ id: 1, name: 'testInterest' }]);

    nock('http://localhost:80')
      .post(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/interest`)
      .reply(200, { id: 2, name: 'newTestInterest' });

    nock('http://localhost:80')
      .put(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/interest/1`)
      .reply(200, { id: 1, name: 'updatedTestInterest' });

    nock('http://localhost:80')
      .delete(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/interest/1`)
      .reply(200, { id: 1 });

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <Interests />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    const pageTitle = screen.getByText('Zainteresowania');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    await waitFor(() => {
      const testInterest = screen.getByText('testInterest');
      expect(testInterest).toBeInTheDocument();
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
    const interestName = screen.getByLabelText('Hobby');
    fireEvent.change(interestName, { target: { value: '' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const errors = screen.getByText('Obowiązkowe');
      expect(errors).toBeInTheDocument();
    });
  });

  test('form submits with valid data', async () => {
    const interestName = screen.getByLabelText('Hobby');
    fireEvent.change(interestName, { target: { value: 'newTestInterest' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const newTestInterest = screen.getByText('newTestInterest');
      expect(newTestInterest).toBeInTheDocument();
    });
  });

  test('interest update works correctly', async () => {
    await waitFor(() => {
      const editButton = screen.getByText('Edytuj');
      fireEvent.click(editButton);
    });

    const interestName = screen.getByLabelText('Hobby');
    fireEvent.change(interestName, { target: { value: 'updatedTestInterest' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const updatedTestInterest = screen.getByText('updatedTestInterest');
      expect(updatedTestInterest).toBeInTheDocument();
    });
  });

  test('interest delete works correctly', async () => {
    await waitFor(() => {
      const testInterest = screen.queryByText('testInterest');
      expect(testInterest).toBeInTheDocument();
    });

    await waitFor(() => {
      const deleteButton = screen.getByText('Usuń');
      fireEvent.click(deleteButton);
    });

    const testInterest = screen.queryByText('testInterest');
    expect(testInterest).not.toBeInTheDocument();

    await waitFor(() => {
      const noContent = screen.getByTestId('no-content');
      expect(noContent).toBeInTheDocument();
    });
  });
});
