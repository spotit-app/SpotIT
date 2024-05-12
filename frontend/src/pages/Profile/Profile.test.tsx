import { render, screen, act, cleanup, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { RouterProvider } from 'providers';
import { slugifyAuth0Id } from 'utils';
import Profile from '.';

jest.mock('@auth0/auth0-react');

(useAuth0 as jest.Mock).mockReturnValue({
  user: {
    sub: 'auth0|1234567890',
    picture: 'https://example-picture.com',
    name: 'John Doe',
    email: 'john.doe@gmail.com'
  },
  isLoading: false,
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

describe('Profile', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}`)
      .reply(200, {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        profilePicture: '',
        email: 'john@gmail.com',
        phoneNumber: '1234567890',
        position: 'Frontend Developer'
      });

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <RouterProvider>
            <Profile />
          </RouterProvider>
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    cleanup();
  });

  test('renders Profile component', async () => {
    await waitFor(() => {
      const name = screen.queryByText('John Doe');
      expect(name).toBeInTheDocument();
    });
  });
});
