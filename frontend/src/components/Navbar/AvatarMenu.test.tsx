import { render, screen, act, fireEvent } from '@testing-library/react';
import { AvatarMenu } from './AvatarMenu';
import { RouterProvider } from '../../providers';

describe('AvatarMenu', () => {
  test('renders AvatarMenu component', () => {
    const mockFn = jest.fn((x) => x);
    const mockPicture = 'https://example.com/picture.png';

    act(() =>
      render(
        <RouterProvider>
          <AvatarMenu picture={mockPicture} logout={mockFn} />
        </RouterProvider>
      )
    );

    expect(screen.getByText('Profile')).toBeInTheDocument();
    expect(screen.getByText('Logout')).toBeInTheDocument();
  });

  test('clicking on logout button calls logout function', () => {
    const mockFn = jest.fn((x) => x);
    const mockPicture = 'https://example.com/picture.png';

    act(() =>
      render(
        <RouterProvider>
          <AvatarMenu picture={mockPicture} logout={mockFn} />
        </RouterProvider>
      )
    );

    const profile = screen.getByTestId('profile');
    fireEvent.click(profile);
    const logoutButton = screen.getByTestId('logout');
    expect(logoutButton).toBeInTheDocument();
    fireEvent.click(logoutButton);
    expect(mockFn).toHaveBeenCalled();
  });
});
