import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { showModal, slugifyAuth0Id } from 'utils';
import Experience from '.';

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

describe('Experiences Page Component', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/experience`)
      .reply(200, [
        {
          id: 1,
          companyName: 'testExperience1',
          position: 'Frontend Developer',
          startDate: '2022-01-01',
          endDate: '2023-01-01'
        }
      ]);

    nock('http://localhost:80')
      .post(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/experience`)
      .reply(200, {
        id: 2,
        companyName: 'testExperience2',
        position: 'Frontend Developer',
        startDate: '2022-01-01',
        endDate: null
      });

    nock('http://localhost:80')
      .put(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/experience/1`)
      .reply(200, {
        id: 1,
        companyName: 'updatedTestExperience1',
        position: 'Frontend Developer',
        startDate: '2022-01-01',
        endDate: '2023-01-01'
      });

    nock('http://localhost:80')
      .delete(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/experience/1`)
      .reply(200, { id: 1 });

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <Experience />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    const pageTitle = screen.getByText('Doświadczenie');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    await waitFor(() => {
      const testExperience = screen.getByText('testExperience1');
      expect(testExperience).toBeInTheDocument();
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
    const experienceCompanyName = screen.getByLabelText('Nazwa firmy');
    fireEvent.change(experienceCompanyName, { target: { value: 'testExperience2' } });

    const position = screen.getByLabelText('Pozycja');
    fireEvent.change(position, { target: { value: 'Frontend Developer' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const errors = screen.getByText('Obowiązkowe');
      expect(errors).toBeInTheDocument();
    });
  });

  test('form submits with valid data', async () => {
    const experienceCompanyName = screen.getByLabelText('Nazwa firmy');
    fireEvent.change(experienceCompanyName, { target: { value: 'testExperience2' } });

    const position = screen.getByLabelText('Pozycja');
    fireEvent.change(position, { target: { value: 'Frontend Developer' } });

    const startDate = screen.getByLabelText('Data rozpoczęcia');
    fireEvent.change(startDate, { target: { value: '2022-01-01' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const newTestExperience = screen.getByText('testExperience2');
      expect(newTestExperience).toBeInTheDocument();
    });
  });

  test('experience update works correctly', async () => {
    await waitFor(() => {
      const editButton = screen.getByText('Edytuj');
      fireEvent.click(editButton);
    });

    const experienceName = screen.getByLabelText('Nazwa firmy');
    fireEvent.change(experienceName, { target: { value: 'updatedTestExperience1' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const updatedTestExperience = screen.getByText('updatedTestExperience1');
      expect(updatedTestExperience).toBeInTheDocument();
    });
  });

  test('experience delete works correctly', async () => {
    await waitFor(() => {
      const testExperience = screen.queryByText('testExperience1');
      expect(testExperience).toBeInTheDocument();
    });

    await waitFor(() => {
      const deleteButton = screen.getByText('Usuń');
      fireEvent.click(deleteButton);
    });

    const testExperience = screen.queryByText('testExperience1');
    expect(testExperience).not.toBeInTheDocument();

    await waitFor(() => {
      const noContent = screen.getByTestId('no-content');
      expect(noContent).toBeInTheDocument();
    });
  });
});
