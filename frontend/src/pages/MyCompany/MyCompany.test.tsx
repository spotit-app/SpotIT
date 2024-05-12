import { act, cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import MyCompany from '.';
import { ReadCompany } from 'types/company';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import { RouterProvider } from 'providers';
import { useParams } from 'react-router-dom';
import nock from 'nock';
import { slugifyAuth0Id } from 'utils';

jest.mock('../../utils/modal', () => ({
  showModal: jest.fn()
}));

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: jest.fn()
}));

jest.mock('@auth0/auth0-react');

(useParams as jest.Mock).mockReturnValue({ id: '1' });

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

const mockedCompany: ReadCompany = {
  id: '1',
  name: 'Test Company',
  nip: '1234567890',
  regon: '123456789',
  websiteUrl: 'http://test.com',
  logoUrl: 'http://test.com/logo.png',
  address: {
    id: '1',
    city: 'Test City',
    street: 'Test Street',
    country: 'Test Country',
    zipCode: '11-111'
  }
};

describe('MyCompany', () => {
  beforeEach(() => {
    nock('http://localhost:80').get(`/api/company/1`).reply(200, mockedCompany);

    nock('http://localhost:80')
      .put(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/company/1`)
      .reply(200, { ...mockedCompany, name: 'Test Company Updated' });

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <MyCompany />
          </QueryClientProvider>
        </RouterProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    await waitFor(() => {
      expect(screen.getByDisplayValue('Test Company')).toBeInTheDocument();
    });
  });

  test('updates correctly', async () => {
    await waitFor(() => {
      const website = screen.getByLabelText('Strona internetowa');
      fireEvent.change(website, { target: { value: 'http://updated.com' } });
    });

    const picture = screen.getByLabelText('Zmień zdjęcie');
    fireEvent.change(picture, { target: { files: ['test.png'] } });

    const submitButton = screen.queryAllByText('Zapisz')[0];
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByDisplayValue('http://updated.com')).toBeInTheDocument();
    });
  });
});

describe('MyCompany without profile picture', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/company/1`)
      .reply(200, { ...mockedCompany, logoUrl: undefined });

    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <MyCompany />
          </QueryClientProvider>
        </RouterProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    await waitFor(() => {
      expect(screen.getByTestId('building-icon')).toBeInTheDocument();
    });
  });
});
