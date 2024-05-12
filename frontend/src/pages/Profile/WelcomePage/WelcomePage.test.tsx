import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { act, render, screen, waitFor, fireEvent, cleanup } from '@testing-library/react';
import WelcomePage from '.';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { slugifyAuth0Id } from '@/utils';

const mockedUsedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockedUsedNavigate
}));

jest.mock('@auth0/auth0-react');

(useAuth0 as jest.Mock).mockReturnValue({
  isAuthenticated: true,
  user: {
    sub: 'auth0|1234567890',
    firstName: 'testFirstName',
    lastName: 'testLastName'
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

describe('WelcomePage', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}`)
      .reply(200, {
        id: 1,
        auth0Id: 'auth0|1234567890',
        firstName: 'testFirstName',
        lastName: 'testLastName',
        email: 'testEmail',
        phoneNumber: 'testPhoneNumber',
        profilePictureUrl: 'testProfilePictureUrl',
        position: 'testPosition',
        description: 'testDescription',
        cvClause: 'testCvClause',
        isOpen: true
      });
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/portfolio`)
      .reply(200, 'testFirstName_testLastName');
    nock('http://localhost:80')
      .post(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/portfolio`)
      .reply(201);
    nock('http://localhost:80')
      .put(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/portfolio`)
      .reply(200);
    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <WelcomePage />
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
      expect(screen.getByText('Witaj, testFirstName!')).toBeInTheDocument();
    });
  });

  test('navigates to portfolio page', async () => {
    await waitFor(() => {
      const portfolioButton = screen.getByText('Podgląd portfolio');
      fireEvent.click(portfolioButton);
    });

    expect(mockedUsedNavigate).toHaveBeenCalled();
  });

  test('navigates to edit profile page', async () => {
    await waitFor(() => {
      const editProfileButton = screen.getByText('Edytuj informacje profilowe');
      fireEvent.click(editProfileButton);
    });

    expect(mockedUsedNavigate).toHaveBeenCalled();
  });

  test('update portfolio button navigates to edit portfolio page', async () => {
    await waitFor(() => {
      const updatePortfolioButton = screen.getByText('Zaktualizuj portfolio');
      fireEvent.click(updatePortfolioButton);
    });

    expect(mockedUsedNavigate).toHaveBeenCalled();
  });
});

describe('WelcomePage without portfolio', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}`)
      .reply(200, {
        id: 1,
        auth0Id: 'auth0|1234567890',
        firstName: 'testFirstName2',
        lastName: 'testLastName',
        email: 'testEmail',
        phoneNumber: 'testPhoneNumber',
        profilePictureUrl: 'testProfilePictureUrl',
        position: 'testPosition',
        description: 'testDescription',
        cvClause: 'testCvClause',
        isOpen: true
      });
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/portfolio`)
      .reply(200, undefined);
    nock('http://localhost:80')
      .post(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/portfolio`)
      .reply(201, {
        portfolioUrl: 'testFirstName_testLastName'
      }),
      nock('http://localhost:80')
        .put(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/portfolio`)
        .reply(200);
    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <WelcomePage />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('creates new portfolio and navigates to it', async () => {
    await waitFor(() => {
      const createPortfolioButton = screen.getByText('Wygeneruj portfolio');

      fireEvent.click(createPortfolioButton);
    });

    expect(mockedUsedNavigate).toHaveBeenCalledWith('/portfolio/testFirstName_testLastName');
  });
});
