import { act, cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import JobOffer from '.';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import nock from 'nock';
import { ReadCompany, ReadJobApplication, ReadJobOffer } from 'types/company';
import { useAuth0 } from '@auth0/auth0-react';
import { slugifyAuth0Id } from 'utils';
import { RouterProvider } from 'providers';

jest.mock('@auth0/auth0-react');

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: jest.fn()
}));

(useAuth0 as jest.Mock).mockReturnValue({
  isAuthenticated: true,
  user: {
    sub: 'auth0|1234567890'
  },
  getAccessTokenSilently: async () => 'testToken'
});

(useParams as jest.Mock).mockReturnValue({ id: '1' });

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
  applicationStatus: 'test_application_status'
};

describe('JobOffer', () => {
  beforeEach(() => {
    nock('http://localhost:80').get(`/api/jobOffer/1`).reply(200, mockedJobOffer);
    nock('http://localhost:80')
      .get(`/api/jobOffer/1/application/${slugifyAuth0Id('auth0|1234567890')}`)
      .reply(200, { isUserOffer: false, hasUserApplied: false });
    nock('http://localhost:80')
      .post(`/api/jobOffer/1/application`, {
        auth0Id: 'auth0|1234567890'
      })
      .reply(201, mockedJobApplication);
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/application?page=0`)
      .reply(200, []);

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <JobOffer />
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
    });
  });

  test('applies for job offer', async () => {
    await waitFor(() => {
      const applyButton = screen.getByText('APLIKUJ');
      fireEvent.click(applyButton);
    });

    await waitFor(() => {
      expect(screen.getByText('APLIKOWANO')).toBeInTheDocument();
    });
  });
});
