import { render, fireEvent, act, screen, cleanup, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { slugifyAuth0Id } from 'utils';
import Other from '.';

jest.mock('@auth0/auth0-react');

(useAuth0 as jest.Mock).mockReturnValue({
  isAuthenticated: true,
  user: {
    sub: 'auth0|1234567890'
  },
  getAccessTokenSilently: async () => 'testToken',
  getIdTokenClaims: async () => ({ 'spotit/roles': [] })
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

describe('Others Page Component', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}`)
      .reply(200, { id: 1, description: 'testDescription', cvClause: 'testCVCLause' });

    nock('http://localhost:80')
      .put(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}`)
      .reply(200, { id: 1, description: 'updatedTestDescription', cvClause: 'testCVCLause' });

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <Other />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    cleanup();
  });

  test('renders correctly', async () => {
    await waitFor(() => {
      expect(screen.getByText('Inne')).toBeInTheDocument();
    });
  });

  test('form submits with valid data', async () => {
    await waitFor(() => {
      expect(screen.getByText('testDescription')).toBeInTheDocument();
      expect(screen.getByText('testCVCLause')).toBeInTheDocument();
    });

    const othersDescription = screen.getByLabelText('Opis');
    fireEvent.change(othersDescription, { target: { value: 'updatedTestDescription' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('updatedTestDescription')).toBeInTheDocument();
    });
  });
});
