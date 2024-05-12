import { render, screen, act, fireEvent, cleanup } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { RouterProvider } from 'providers';
import { slugifyAuth0Id } from 'utils';
import { Navbar } from '.';

jest.mock('@auth0/auth0-react');

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
      refetchOnWindowFocus: false,
      staleTime: 30000
    }
  }
});

describe('Navbar', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}`)
      .reply(200, {
        id: 1,
        firstName: 'testFirstName',
        lastName: 'testLastName',
        profilePicture: ''
      });
  });

  afterEach(() => {
    cleanup();
  });

  test('renders Navbar component without loging in', () => {
    const mockLogin = jest.fn();
    const mockLogout = jest.fn();

    (useAuth0 as jest.Mock).mockReturnValue({
      isAuthenticated: false,
      user: {
        sub: 'auth0|1234567890',
        picture: 'https://example.com/picture.png',
        name: 'John Doe'
      },
      loginWithRedirect: mockLogin,
      logout: mockLogout,
      getIdTokenClaims: async () => ({ 'spotit/roles': [] })
    });

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <RouterProvider>
            <Navbar />
          </RouterProvider>
        </QueryClientProvider>
      )
    );

    const login = screen.getByTestId('login');
    expect(login).toBeInTheDocument();
    expect(screen.getByText('Dla pracownika')).toBeInTheDocument();
    expect(screen.getByText('Dla pracodawcy')).toBeInTheDocument();
    fireEvent.click(login);
    expect(mockLogin).toHaveBeenCalled();
  });

  test('renders Navbar component with loging in', () => {
    const mockLogin = jest.fn();
    const mockLogout = jest.fn();

    (useAuth0 as jest.Mock).mockReturnValue({
      isAuthenticated: true,
      user: {
        sub: 'auth0|1234567890',
        picture: 'https://example.com/picture.png',
        name: 'John Doe',
        email: 'john.doe@gmail.com'
      },
      loginWithRedirect: mockLogin,
      logout: mockLogout,
      getIdTokenClaims: async () => ({ 'spotit/roles': [] })
    });

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <RouterProvider>
            <Navbar />
          </RouterProvider>
        </QueryClientProvider>
      )
    );

    const menuBtn = screen.getByTestId('menu-btn');
    fireEvent.click(menuBtn);
    const logout = screen.getByTestId('logout');
    expect(logout).toBeInTheDocument();
    fireEvent.click(logout);
    expect(mockLogout).toHaveBeenCalled();
  });
});
