import { render, fireEvent, act, screen, cleanup, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { slugifyAuth0Id } from 'utils';
import PersonalData from '.';
import { RouterProvider } from '@/providers';

jest.mock('@auth0/auth0-react');

(useAuth0 as jest.Mock).mockReturnValue({
  isAuthenticated: true,
  user: {
    sub: 'auth0|1234567890'
  },
  getAccessTokenSilently: async () => 'testToken'
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

describe('PersonalDatas Page Component', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}`)
      .reply(200, {
        id: 1,
        firstName: 'testFirstName',
        lastName: 'testLastName',
        email: 'testEmail@gmail.com',
        phoneNumber: '123456789',
        profilePicture: '',
        position: 'testPosition'
      });

    nock('http://localhost:80')
      .put(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}`)
      .reply(200, {
        id: 1,
        firstName: 'updatedTestFirstName',
        lastName: 'testLastName',
        email: 'testEmail@gmail.com',
        phoneNumber: '123456789',
        profilePicture: '',
        position: 'testPosition'
      });

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <PersonalData />
          </QueryClientProvider>
        </RouterProvider>
      )
    );
  });

  afterEach(() => {
    cleanup();
  });

  test('renders correctly', async () => {
    await waitFor(() => {
      expect(screen.getByText('Dane osobowe')).toBeInTheDocument();
    });
  });

  test('form submits with valid data', async () => {
    await waitFor(() => {
      const firstName = screen.getByLabelText('Imię');
      expect(firstName).toHaveValue('testFirstName');
    });

    const personalDatasDescription = screen.getByLabelText('Imię');
    fireEvent.change(personalDatasDescription, { target: { value: 'updatedTestFirstName' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const firstName = screen.getByLabelText('Imię');
      expect(firstName).toHaveValue('updatedTestFirstName');
    });
  });
});
