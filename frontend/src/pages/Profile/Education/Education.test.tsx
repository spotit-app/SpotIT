import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { showModal, slugifyAuth0Id } from 'utils';
import Education from '.';
import selectEvent from 'react-select-event';

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

describe('Educations Page Component', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/education`)
      .reply(200, [
        {
          id: 1,
          schoolName: 'testEducation1',
          educationLevel: 'testEducationLevel',
          faculty: 'testFaculty',
          startDate: '2022-01-01',
          endDate: '2023-01-01'
        }
      ]);

    nock('http://localhost:80')
      .post(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/education`)
      .reply(200, {
        id: 2,
        schoolName: 'testEducation2',
        educationLevel: 'testEducationLevel',
        faculty: 'testFaculty',
        startDate: '2022-01-01',
        endDate: null
      });

    nock('http://localhost:80')
      .delete(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/education/1`)
      .reply(200, { id: 1 });

    nock('http://localhost:80')
      .get('/api/educationLevel')
      .reply(200, [
        {
          id: 1,
          name: 'testEducationLevel'
        }
      ]);

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <Education />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    const pageTitle = screen.getByText('Edukacja');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    await waitFor(() => {
      const testEducation = screen.getByText('testEducation1 - testEducationLevel');
      expect(testEducation).toBeInTheDocument();
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
    await selectEvent.select(screen.getByLabelText('Stopień edukacji'), 'testEducationLevel');

    const educationSchoolName = screen.getByLabelText('Nazwa szkoły');
    fireEvent.change(educationSchoolName, { target: { value: 'testEducation2' } });

    fireEvent.click(screen.getByText('testEducationLevel'));

    const faculty = screen.getByLabelText('Kierunek');
    fireEvent.change(faculty, { target: { value: 'testFaculty' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const errors = screen.getByText('Obowiązkowe');
      expect(errors).toBeInTheDocument();
    });
  });

  test('form submits with valid data', async () => {
    await selectEvent.select(screen.getByLabelText('Stopień edukacji'), 'testEducationLevel');

    const educationSchoolName = screen.getByLabelText('Nazwa szkoły');
    fireEvent.change(educationSchoolName, { target: { value: 'testEducation2' } });

    const faculty = screen.getByLabelText('Kierunek');
    fireEvent.change(faculty, { target: { value: 'testFaculty' } });

    const startDate = screen.getByLabelText('Data rozpoczęcia');
    fireEvent.change(startDate, { target: { value: '2022-01-01' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const newTestEducation = screen.getByText('testEducation2 - testEducationLevel');
      expect(newTestEducation).toBeInTheDocument();
    });
  });

  test('education delete works correctly', async () => {
    await waitFor(() => {
      const testEducation = screen.queryByText('testEducation1 - testEducationLevel');
      expect(testEducation).toBeInTheDocument();
    });

    await waitFor(() => {
      const deleteButton = screen.getByText('Usuń');
      fireEvent.click(deleteButton);
    });

    const testEducation = screen.queryByText('testEducation1 - testEducationLevel');
    expect(testEducation).not.toBeInTheDocument();

    await waitFor(() => {
      const noContent = screen.getByTestId('no-content');
      expect(noContent).toBeInTheDocument();
    });
  });
});
