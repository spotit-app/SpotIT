import { act, cleanup, render, screen, waitFor } from '@testing-library/react';
import PortfolioPage from '.';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import nock from 'nock';
import { useAuth0 } from '@auth0/auth0-react';
import { RouterProvider } from 'providers';
import { Portfolio } from '@/types/profile';

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

(useParams as jest.Mock).mockReturnValue({ portfolioUrl: 'test_url' });

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
      refetchOnWindowFocus: false,
      staleTime: 30000
    }
  }
});

const mockedPortfolio: Portfolio = {
  portfolioUrl: 'test_url',
  userData: {
    firstName: 'Test First Name',
    lastName: 'Test Last Name',
    email: 'test@emailcom',
    phoneNumber: '123456789',
    profilePictureUrl: 'http://test.com/profile.png',
    position: 'Test Position',
    description: 'Test Description',
    cvClause: 'Test CV Clause'
  },
  courses: [{ id: 1, name: 'Test Course', finishDate: '2020-12-12' }],
  educations: [
    {
      id: 1,
      schoolName: 'Test School',
      faculty: 'Test Faculty',
      startDate: '2020-12-12',
      endDate: '2020-12-12',
      educationLevel: 'Test Education Level'
    }
  ],
  experiences: [
    {
      id: 1,
      companyName: 'Test Company',
      position: 'Test Position',
      startDate: '2020-12-12',
      endDate: '2020-12-12'
    }
  ],
  foreignLanguages: [
    {
      id: 1,
      languageLevel: 'Test Language Level',
      foreignLanguageName: 'Test Foreign Language',
      flagUrl: 'http://test.com/flag.png'
    }
  ],
  interests: [{ id: 1, name: 'Test Interest' }],
  projects: [
    {
      id: 1,
      name: 'Test Project',
      description: 'Test Project Description',
      projectUrl: 'http://test.com'
    }
  ],
  socials: [{ id: 1, name: 'Test Social', socialUrl: 'http://test.com' }],
  softSkills: [{ id: 1, skillLevel: 1, softSkillName: 'Test Soft Skill' }],
  techSkills: [
    { id: 1, skillLevel: 1, techSkillName: 'Test Tech Skill', logoUrl: 'http://test.com/logo.png' }
  ]
};

describe('Portfolio', () => {
  beforeEach(() => {
    nock('http://localhost:80').get(`/api/portfolio/test_url`).reply(200, mockedPortfolio);

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <RouterProvider>
            <PortfolioPage />
          </RouterProvider>
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    await waitFor(() => {
      expect(screen.getByText('Test First Name')).toBeInTheDocument();
      expect(screen.getByText('Test Description')).toBeInTheDocument();
      expect(screen.getByText('Test Tech Skill')).toBeInTheDocument();
      expect(screen.getByText('Test Soft Skill')).toBeInTheDocument();
      expect(screen.getByText('Test Foreign Language')).toBeInTheDocument();
    });
  });
});

describe('Portfolio without email', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/portfolio/test_url`)
      .reply(200, {
        ...mockedPortfolio,
        userData: { ...mockedPortfolio.userData, email: undefined }
      });

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <RouterProvider>
            <PortfolioPage />
          </RouterProvider>
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    await waitFor(() => {
      expect(screen.getByText('123456789')).toBeInTheDocument();
    });
  });
});
