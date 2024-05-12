import { act, cleanup, render, screen, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { RouterProvider } from 'providers';
import nock from 'nock';
import { PortfolioPage, ReadPortfolioPageDto } from 'types/profile';
import Employees from '.';

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

const mockedJobApplication: ReadPortfolioPageDto = {
  portfolioUrl: 'Test_Portfolio_Url',
  userData: {
    firstName: 'Test Name',
    lastName: 'Test LastName',
    email: 'TestEmail@gmail.com',
    phoneNumber: '123456789',
    profilePictureUrl: 'http://test.com/logo.png',
    position: 'Test Position',
    description: 'Test Description',
    cvClause: 'Test Clause',
    isOpen: true
  }
};

const mockedResponse: PortfolioPage = {
  content: [mockedJobApplication],
  pageable: {
    pageNumber: 0,
    pageSize: 20,
    sort: {
      unsorted: false,
      sorted: true,
      empty: false
    },
    offset: 0,
    paged: true,
    unpaged: false
  },
  totalPages: 1,
  totalElements: 1,
  last: true,
  numberOfElements: 1,
  size: 20,
  number: 0,
  sort: {
    unsorted: false,
    sorted: true,
    empty: false
  },
  first: true,
  empty: false
};

describe('Employees', () => {
  beforeEach(() => {
    nock('http://localhost:80').get(`/api/portfolios?page=0`).reply(200, mockedResponse);

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <Employees />
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
      expect(screen.getByText('Test_Portfolio_Url')).toBeInTheDocument();
    });
  });
});
