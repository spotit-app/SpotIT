import { act, cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import { MyCompanyJobOffers } from '.';
import { JobOfferPage, ReadCompany, ReadJobOffer } from 'types/company';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { RouterProvider } from 'providers';
import { useParams } from 'react-router-dom';
import nock from 'nock';
import { slugifyAuth0Id } from 'utils';

const mockedUsedNavigate = jest.fn();

jest.mock('../../utils/modal', () => ({
  showModal: jest.fn()
}));

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: jest.fn(),
  useNavigate: () => mockedUsedNavigate
}));

jest.mock('@auth0/auth0-react');

(useParams as jest.Mock).mockReturnValue({ id: '1' });

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
  workModes: [{ id: 1, name: 'Test Work Mode' }],
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

describe('MyCompanyJobOffers', () => {
  beforeEach(() => {
    nock('http://localhost:80').get(`/api/company/1/jobOffer?page=0`).reply(200, mockedResponse);

    nock('http://localhost:80')
      .post(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/company/1/jobOffer`)
      .reply(200, { ...mockedJobOffer, name: 'Test Job Offer 2', id: 2 });

    nock('http://localhost:80')
      .delete(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/company/1/jobOffer/1`)
      .reply(200, { id: 1 });

    nock('http://localhost:80')
      .put(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/company/1/jobOffer/1`)
      .reply(200, { ...mockedJobOffer, name: 'Test Job Offer Updated' });

    nock('http://localhost:80')
      .get(`/api/techSkillName`)
      .reply(200, [{ id: 1, name: 'Test Tech Skill', logoUrl: 'http://test.com/logo.png' }]);

    nock('http://localhost:80')
      .get(`/api/softSkillName`)
      .reply(200, [{ id: 1, name: 'Test Soft Skill' }]);

    nock('http://localhost:80')
      .get(`/api/foreignLanguageName`)
      .reply(200, [{ id: 1, name: 'Test Foreign Language', flagUrl: 'http://test.com/flag.png' }]);

    nock('http://localhost:80')
      .get(`/api/workExperience`)
      .reply(200, [{ id: 1, name: 'Test Work Experience' }]);

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <MyCompanyJobOffers />
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

  test('navigates to applications', async () => {
    await waitFor(() => {
      const applicationsButton = screen.getByText('Aplikacje');
      fireEvent.click(applicationsButton);
    });

    await waitFor(() => {
      expect(mockedUsedNavigate).toHaveBeenCalled();
    });
  });

  test('deletes correctly', async () => {
    await waitFor(() => {
      const deleteButton = screen.queryAllByText('Usuń')[0];
      fireEvent.click(deleteButton);
    });

    await waitFor(() => {
      expect(screen.queryByText('Test Job Offer')).not.toBeInTheDocument();
    });
  });
});

describe('MyCompanyJobOffers without any offers', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/company/1/jobOffer?page=0`)
      .reply(200, { ...mockedResponse, content: [], empty: true, totalElements: 0 });

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <MyCompanyJobOffers />
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
