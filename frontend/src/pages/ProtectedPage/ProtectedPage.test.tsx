import { render, screen, act } from '@testing-library/react';
import { RouterProvider } from '../../providers';
import { ProtectedPage } from './ProtectedPage';
import { useAuth0 } from '@auth0/auth0-react';

jest.mock('@auth0/auth0-react');

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
          <ProtectedPage>
            <div>test</div>
          </ProtectedPage>
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
          <ProtectedPage>
            <div>test</div>
          </ProtectedPage>
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
          <ProtectedPage>
            <div>test</div>
          </ProtectedPage>
        </RouterProvider>
      )
    );

    expect(screen.getByTestId('loading')).toBeInTheDocument();
  });
});
