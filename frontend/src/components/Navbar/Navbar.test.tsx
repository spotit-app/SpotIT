import { render, screen, act, fireEvent } from '@testing-library/react';
import { Navbar } from './Navbar';
import { RouterProvider } from '../../providers';
import { useAuth0 } from '@auth0/auth0-react';

jest.mock('@auth0/auth0-react');

describe('Navbar', () => {
  test('renders Navbar component without loging in', () => {
    const mockLogin = jest.fn();
    const mockLogout = jest.fn();

    (useAuth0 as jest.Mock).mockReturnValue({
      isAuthenticated: false,
      user: undefined,
      loginWithRedirect: mockLogin,
      logout: mockLogout
    });

    act(() =>
      render(
        <RouterProvider>
          <Navbar />
        </RouterProvider>
      )
    );

    const login = screen.getByTestId('login');
    const singIn = screen.getByTestId('sing-in');
    expect(login).toBeInTheDocument();
    expect(singIn).toBeInTheDocument();
    expect(screen.getByText('Dla pracownika')).toBeInTheDocument();
    expect(screen.getByText('Dla pracodawcy')).toBeInTheDocument();
    expect(screen.getByText('CV generator')).toBeInTheDocument();
    expect(screen.getByText('Portfolio')).toBeInTheDocument();
    fireEvent.click(login);
    fireEvent.click(singIn);
    expect(mockLogin).toHaveBeenCalled();
  });

  test('renders Navbar component with loging in', () => {
    const mockLogin = jest.fn();
    const mockLogout = jest.fn();

    (useAuth0 as jest.Mock).mockReturnValue({
      isAuthenticated: true,
      user: {
        picture: 'https://example.com/picture.png',
        name: 'John Doe',
        email: 'john.doe@gmail.com'
      },
      loginWithRedirect: mockLogin,
      logout: mockLogout
    });

    act(() =>
      render(
        <RouterProvider>
          <Navbar />
        </RouterProvider>
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
