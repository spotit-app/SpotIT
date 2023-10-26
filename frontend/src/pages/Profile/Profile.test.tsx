import { render, screen, act } from '@testing-library/react';
import { Profile } from './Profile';
import { useAuth0 } from '@auth0/auth0-react';

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

    act(() => render(<Profile />));

    expect(screen.getByText('Profile')).toBeInTheDocument();
    expect(screen.getByText(/John Doe/)).toBeInTheDocument();
    expect(screen.getByText(/john.doe@gmail.com/)).toBeInTheDocument();
  });

  test('renders Loading component when loading', () => {
    (useAuth0 as jest.Mock).mockReturnValue({
      user: undefined,
      isLoading: true
    });

    act(() => render(<Profile />));

    expect(screen.getByTestId('loading')).toBeInTheDocument();
  });
});
