import { act, render, screen, waitFor, cleanup, fireEvent } from '@testing-library/react';
import AdminEducation from '.';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { slugifyAuth0Id } from 'utils';
import { RouterProvider } from 'providers';
import { ReadCompany } from 'types/company';
import { showModal } from 'utils';

jest.mock('../../utils/modal', () => ({
  showModal: jest.fn()
}));

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

const mockedCreatedCompany: ReadCompany = {
  id: '2',
  name: 'Test Company 2',
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

describe('MyCompanies', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/company`)
      .reply(200, [mockedCompany]);
    nock('http://localhost:80')
      .post(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/company`)
      .reply(201, mockedCreatedCompany);
    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <AdminEducation />
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
    waitFor(() => {
      expect(screen.getByText('Test Company')).toBeInTheDocument();
    });
  });

  test('add new company', async () => {
    const name = screen.getByLabelText('Nazwa firmy');
    fireEvent.change(name, { target: { value: 'Test Company 2' } });

    const nip = screen.getByLabelText('NIP');
    fireEvent.change(nip, { target: { value: '1234567890' } });

    const regon = screen.getByLabelText('REGON');
    fireEvent.change(regon, { target: { value: '123456789' } });

    const website = screen.getByLabelText('Strona internetowa');
    fireEvent.change(website, { target: { value: 'http://test.com' } });

    const country = screen.getByLabelText('Kraj');
    fireEvent.change(country, { target: { value: 'Test Country' } });

    const zipCode = screen.getByLabelText('Kod pocztowy');
    fireEvent.change(zipCode, { target: { value: '11-111' } });

    const city = screen.getByLabelText('Miasto');
    fireEvent.change(city, { target: { value: 'Test City' } });

    const street = screen.getByLabelText('Ulica');
    fireEvent.change(street, { target: { value: 'Test Street' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('Test Company 2')).toBeInTheDocument();
    });
  });

  test('button triggers formToggle function', async () => {
    const addButton = screen.getByTestId('add-button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });
});

describe('MyCompanies without content', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/company`)
      .reply(200, []);
    act(() =>
      render(
        <RouterProvider>
          <QueryClientProvider client={queryClient}>
            <AdminEducation />
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
    waitFor(() => {
      expect(screen.getByTestId('no-content')).toBeInTheDocument();
    });
  });
});
