import { act, cleanup, render, screen, waitFor } from '@testing-library/react';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import Company from '.';
import { ReadCompany, ReadJobOffer } from 'types/company';

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
  regon: '1234567890',
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

const mockedJobOffers: ReadJobOffer[] = [
  {
    id: 1,
    name: 'Test Job Offer',
    position: 'Test Position',
    description: 'Test Description',
    minSalary: 1000,
    maxSalary: 2000,
    benefits: 'Test Benefits',
    dueDate: '2022-12-12',
    workExperienceName: 'Test Work Experience',
    techSkillNames: [],
    softSkillNames: [],
    foreignLanguageNames: [],
    workModes: [],
    company: mockedCompany
  }
];

describe('Company', () => {
  beforeEach(() => {
    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <Company />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    nock('http://localhost:80').get(`/api/company/1`).reply(200, mockedCompany);
    nock('http://localhost:80').get(`/api/company/1/jobOffer`).reply(200, mockedJobOffers);

    waitFor(() => {
      expect(screen.getByText('Test Company')).toBeInTheDocument();
      expect(screen.getByText('Test Job Offer')).toBeInTheDocument();
    });
  });

  test('renders correctly without job offers', async () => {
    nock('http://localhost:80').get(`/api/company/1`).reply(200, mockedCompany);
    nock('http://localhost:80').get(`/api/company/1/jobOffer`).reply(200, []);

    waitFor(() => {
      expect(screen.getByText('Test Company')).toBeInTheDocument();
      expect(screen.getByTestId('no-content')).toBeInTheDocument();
    });
  });

  test('renders building icon without company logo', async () => {
    nock('http://localhost:80')
      .get(`/api/company/1`)
      .reply(200, {
        ...mockedCompany,
        logoUrl: undefined
      });
    nock('http://localhost:80').get(`/api/company/1/jobOffer`).reply(200, []);

    waitFor(() => {
      expect(screen.getByTestId('building-icon')).toBeInTheDocument();
    });
  });
});
