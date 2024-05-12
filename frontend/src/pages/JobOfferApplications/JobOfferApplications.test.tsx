import { act, cleanup, render, screen, waitFor } from '@testing-library/react';
import { JobApplicationPage, ReadCompany, ReadJobApplication, ReadJobOffer } from 'types/company';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { RouterProvider } from 'providers';
import { useParams } from 'react-router-dom';
import nock from 'nock';
import { slugifyAuth0Id } from 'utils';
import JobOfferApplications from '.';

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: jest.fn()
}));

jest.mock('@auth0/auth0-react');

(useParams as jest.Mock).mockReturnValue({ companyId: '1', jobOfferId: '1' });

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

const mockedJobApplication: ReadJobApplication = {
  id: 1,
  jobOffer: mockedJobOffer,
  portfolio: {
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
  },
  applicationStatus: 'Test status'
};

const mockedResponse: JobApplicationPage = {
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

describe('JobOfferApplications', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(
        `/api/userAccount/${slugifyAuth0Id(
          'auth0|1234567890'
        )}/company/1/jobOffer/1/application?page=0`
      )
      .reply(200, mockedResponse);

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <JobOfferApplications />
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
      expect(screen.getByText('Test status')).toBeInTheDocument();
    });
  });
});

describe('JobOfferApplications without any offers', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(
        `/api/userAccount/${slugifyAuth0Id(
          'auth0|1234567890'
        )}/company/1/jobOffer/1/application?page=0`
      )
      .reply(200, { ...mockedResponse, content: [], empty: true, totalElements: 0 });

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <JobOfferApplications />
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
      expect(screen.getByTestId('no-content')).toBeInTheDocument();
    });
  });
});
