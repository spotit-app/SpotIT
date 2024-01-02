import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import userEvent from '@testing-library/user-event';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { showModal, slugifyAuth0Id } from 'utils';
import ForeignLanguages from '.';

jest.mock('../../../utils/modal', () => ({
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

describe('ForeignLanguages Page Component', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/foreignLanguage`)
      .reply(200, [
        {
          id: 1,
          languageLevel: 'A1',
          foreignLanguageName: 'Angielski',
          flagUrl: 'https://restcountries.eu/data/gbr.svg'
        }
      ]);

    nock('http://localhost:80')
      .post(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/foreignLanguage`)
      .reply(200, {
        id: 2,
        languageLevel: 'A1',
        foreignLanguageName: 'Niemiecki',
        flagUrl: 'https://restcountries.eu/data/ger.svg'
      });

    nock('http://localhost:80')
      .delete(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/foreignLanguage/1`)
      .reply(200, { id: 1 });

    nock('http://localhost:80')
      .get('/api/foreignLanguageName')
      .reply(200, [
        { id: 1, name: 'Angielski', flagUrl: 'https://restcountries.eu/data/gbr.svg' },
        { id: 2, name: 'Niemiecki', flagUrl: 'https://restcountries.eu/data/ger.svg' }
      ]);

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <ForeignLanguages />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    const pageTitle = screen.getByText('Języki obce');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    await waitFor(() => {
      const testForeignLanguage = screen.getAllByText('Angielski');
      expect(testForeignLanguage.length).toBe(2);
    });
  });

  test('button triggers formToggle function', async () => {
    const addButton = screen.getByTestId('add-button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('form submits with bad data', async () => {
    await waitFor(() => {
      expect(screen.queryByText('Niemiecki')).toBeInTheDocument();
    });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const errors = screen.getByText('Obowiązkowe');
      expect(errors).toBeInTheDocument();
    });
  });

  test('form submits with valid data', async () => {
    await waitFor(() => {
      expect(screen.queryByText('Niemiecki')).toBeInTheDocument();
    });

    const foreignLanguageName = screen.getByLabelText('Nazwa języka');
    await userEvent.selectOptions(foreignLanguageName, 'Niemiecki');

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const newTestForeignLanguage = screen.queryAllByText('Niemiecki');
      expect(newTestForeignLanguage.length).toBe(2);
    });
  });

  test('foreignLanguage delete works correctly', async () => {
    await waitFor(() => {
      const testForeignLanguage = screen.queryAllByText('Angielski');
      expect(testForeignLanguage.length).toBe(2);
    });

    await waitFor(() => {
      const deleteButton = screen.getByText('Usuń');
      fireEvent.click(deleteButton);
    });

    const testForeignLanguage = screen.queryAllByText('Angielski');
    expect(testForeignLanguage.length).toBe(1);

    await waitFor(() => {
      const noContent = screen.getByTestId('no-content');
      expect(noContent).toBeInTheDocument();
    });
  });
});
