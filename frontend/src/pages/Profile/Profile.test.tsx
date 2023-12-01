import { render, screen, act } from '@testing-library/react';
import { Profile } from './Profile';
import { useAuth0 } from '@auth0/auth0-react';
import { RouterProvider } from '../../providers';

jest.mock('@auth0/auth0-react');

describe('Profile', () => {
  test('renders Profile component', () => {
    (useAuth0 as jest.Mock).mockReturnValue({
      user: {
        picture: 'https://example-picture.com',
        name: 'John Doe',
        email: 'john.doe@gmail.com'
      },
      isLoading: false
    });

    act(() =>
      render(
        <RouterProvider>
          <Profile />
        </RouterProvider>
      )
    );

    expect(screen.getByText(/John Doe/)).toBeInTheDocument();
  });
});
