import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { showModal, closeModal, slugifyAuth0Id } from 'utils';
import Socials from '.';

jest.mock('../../../utils/modal', () => ({
  showModal: jest.fn(),
  closeModal: jest.fn()
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

describe('Socials Page Component', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/social`)
      .reply(200, [{ id: 1, name: 'testSocial1', socialUrl: 'https://test.social.com/john_doe' }]);

    nock('http://localhost:80')
      .post(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/social`)
      .reply(200, { id: 2, name: 'testSocial2', socialUrl: 'https://test.social.com/james_bond' });

    nock('http://localhost:80')
      .put(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/social/1`)
      .reply(200, {
        id: 1,
        name: 'updatedTestSocial1',
        socialUrl: 'https://test.social.com/john_doe'
      });

    nock('http://localhost:80')
      .delete(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/social/1`)
      .reply(200, { id: 1 });

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <Socials />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    const pageTitle = screen.getByText('Konta społecznościowe');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    await waitFor(() => {
      const testSocial = screen.getByText('testSocial1');
      expect(testSocial).toBeInTheDocument();
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
    const socialName = screen.getByLabelText('Nazwa konta społecznościowego');
    fireEvent.change(socialName, { target: { value: 'test' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const errors = screen.getByText('Obowiązkowe');
      expect(errors).toBeInTheDocument();
    });
  });

  test('form submits with valid data', async () => {
    const socialName = screen.getByLabelText('Nazwa konta społecznościowego');
    fireEvent.change(socialName, { target: { value: 'testSocial2' } });

    const socialUrl = screen.getByLabelText('Link do konta');
    fireEvent.change(socialUrl, { target: { value: 'https://test.social.com/james_bond' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const newTestSocial = screen.getByText('testSocial2');
      expect(newTestSocial).toBeInTheDocument();
      expect(closeModal).toHaveBeenCalled();
    });
  });

  test('social update works correctly', async () => {
    await waitFor(() => {
      const editButton = screen.getByText('Edytuj');
      fireEvent.click(editButton);
    });

    const socialName = screen.getByLabelText('Nazwa konta społecznościowego');
    fireEvent.change(socialName, { target: { value: 'updatedTestSocial1' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const updatedTestSocial = screen.getByText('updatedTestSocial1');
      expect(updatedTestSocial).toBeInTheDocument();
    });
  });

  test('social delete works correctly', async () => {
    await waitFor(() => {
      const testSocial = screen.queryByText('testSocial1');
      expect(testSocial).toBeInTheDocument();
    });

    await waitFor(() => {
      const deleteButton = screen.getByText('Usuń');
      fireEvent.click(deleteButton);
    });

    const testSocial = screen.queryByText('testSocial1');
    expect(testSocial).not.toBeInTheDocument();

    await waitFor(() => {
      const noContent = screen.getByTestId('no-content');
      expect(noContent).toBeInTheDocument();
    });
  });
});
