import { act, cleanup, render, screen, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { RouterProvider } from 'providers';
import nock from 'nock';
import { JobOfferPage, ReadCompany, ReadJobOffer } from 'types/company';
import JobOffers from '.';

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

const mockedCompany: ReadCompany = {
  id: '1',
  name: 'Test Company',
  nip: '1234567890',
  regon: '123456789',
  websiteUrl: 'http://test.com',
  logoUrl: 'http://test.com/logo.png',
  address: {
    id: '1',
    city: 'Test City',
    street: 'Test Street',
    country: 'Test Country',
    zipCode: '11-111'
  }
};

const mockedJobOffer: ReadJobOffer = {
  id: 1,
  name: 'Test Job Offer',
  position: 'Test Position',
  description: 'Test Description',
  minSalary: 1000,
  maxSalary: 2000,
  benefits: 'Test Benefits',
  dueDate: '2022-12-12',
  workExperienceName: 'Test Work Experience',
  techSkillNames: [{ id: 1, name: 'Test Tech Skill', logoUrl: 'http://test.com/logo.png' }],
  softSkillNames: [{ id: 1, name: 'Test Soft Skill' }],
  foreignLanguageNames: [
    { id: 1, name: 'Test Foreign Language', flagUrl: 'http://test.com/flag.png' }
  ],
  workModes: [],
  company: mockedCompany
};

const mockedResponse: JobOfferPage = {
  content: [mockedJobOffer],
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

describe('JobOffers', () => {
  beforeEach(() => {
    nock('http://localhost:80').get(`/api/jobOffer?page=0`).reply(200, mockedResponse);

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <JobOffers />
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
      expect(screen.getByText('Test Job Offer')).toBeInTheDocument();
      expect(screen.getByText('Test Company')).toBeInTheDocument();
    });
  });
});
