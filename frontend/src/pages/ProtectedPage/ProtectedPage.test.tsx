import { render, screen, act } from '@testing-library/react';
import { useAuth0 } from '@auth0/auth0-react';
import { RouterProvider } from 'providers';
import ProtectedPage from '.';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

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

describe('ProtectedPage', () => {
  test('renders ProtectedPage component when logged in', () => {
    const mockLogin = jest.fn();

    (useAuth0 as jest.Mock).mockReturnValue({
      isAuthenticated: true,
      user: {
        picture: 'https://example.com/picture.png',
        name: 'John Doe',
        email: 'john.doe@gmail.com'
      },
      loginWithRedirect: mockLogin,
      isLoading: false
    });

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <ProtectedPage>
              <div>test</div>
            </ProtectedPage>
          </QueryClientProvider>
        </RouterProvider>
      )
    );

    expect(screen.getByText('test')).toBeInTheDocument();
  });

  test('redirects when not logged in', () => {
    const mockLogin = jest.fn();
    (useAuth0 as jest.Mock).mockReturnValue({
      isAuthenticated: false,
      user: undefined,
      loginWithRedirect: mockLogin,
      isLoading: false
    });

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <ProtectedPage>
              <div>test</div>
            </ProtectedPage>
          </QueryClientProvider>
        </RouterProvider>
      )
    );

    expect(mockLogin).toHaveBeenCalled();
  });

  test('renders Loading component when loading', () => {
    const mockLogin = jest.fn();
    (useAuth0 as jest.Mock).mockReturnValue({
      isAuthenticated: false,
      user: undefined,
      loginWithRedirect: mockLogin,
      isLoading: true
    });

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <ProtectedPage>
              <div>test</div>
            </ProtectedPage>
          </QueryClientProvider>
        </RouterProvider>
      )
    );

    expect(screen.getByTestId('loading')).toBeInTheDocument();
  });
});
