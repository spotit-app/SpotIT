import { render, screen, act, fireEvent, cleanup, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { RouterProvider } from 'providers';
import { slugifyAuth0Id } from 'utils';
import { Sidebar } from '.';

jest.mock('@auth0/auth0-react');

(useAuth0 as jest.Mock).mockReturnValue({
  user: {
    sub: 'auth0|1234567890',
    picture: 'https://example-picture.com',
    name: 'John Doe',
    email: 'john.doe@gmail.com'
  },
  isLoading: false
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

describe('Sidebar', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}`)
      .reply(200, {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        profilePicture: '',
        email: 'john@gmail.com',
        phoneNumber: '1234567890',
        position: 'Frontend Developer'
      });

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <RouterProvider>
            <Sidebar />
          </RouterProvider>
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    cleanup();
  });

  test('renders Sidebar component', () => {
    const nameElement = screen.getByText('John Doe');
    expect(nameElement).toBeInTheDocument();

    const imageElement = screen.getByAltText('Profile Picture');
    expect(imageElement).toBeInTheDocument();

    const navigation = [
      'Dane osobowe',
      'Konta społecznościowe',
      'Edukacja',
      'Doświadczenie',
      'Umiejętności techniczne',
      'Umiejętności miękkie',
      'Języki obce',
      'Projekty',
      'Zainteresowania',
      'Kursy',
      'Inne'
    ];
    navigation.forEach((el) => {
      expect(screen.getByText(el)).toBeInTheDocument();
    });
  });

  test('Sidebar navigation working', async () => {
    const personalDataLink = screen.getByText('Dane osobowe');
    fireEvent.click(personalDataLink);
    await waitFor(() => {
      expect(window.location.pathname).toBe('/profil/dane-osobowe');
    });

    const experienceLink = screen.getByText('Doświadczenie');
    fireEvent.click(experienceLink);
    await waitFor(() => {
      expect(window.location.pathname).toBe('/profil/doswiadczenie');
    });

    const othersLink = screen.getByText('Inne');
    fireEvent.click(othersLink);
    await waitFor(() => {
      expect(window.location.pathname).toBe('/profil/inne');
    });
  });
});
