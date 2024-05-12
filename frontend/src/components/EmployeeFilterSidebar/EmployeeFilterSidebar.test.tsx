import { act, cleanup, render, screen, waitFor } from '@testing-library/react';
import { EmployeeFilterSidebar } from '.';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { RouterProvider } from 'providers';
import nock from 'nock';

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: jest.fn()
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

describe('EmployeeFilterSidebar', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/techSkillName`)
      .reply(200, [{ id: 1, name: 'Test Tech Skill', logoUrl: 'http://test.com/logo.png' }]);

    nock('http://localhost:80')
      .get(`/api/foreignLanguageName`)
      .reply(200, [{ id: 1, name: 'Test Foreign Language', flagUrl: 'http://test.com/flag.png' }]);

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <EmployeeFilterSidebar />
          </QueryClientProvider>
        </RouterProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    await waitFor(() => {
      expect(screen.getByText('Test Tech Skill')).toBeInTheDocument();
      expect(screen.getByText('Test Foreign Language')).toBeInTheDocument();
    });
  });
});
