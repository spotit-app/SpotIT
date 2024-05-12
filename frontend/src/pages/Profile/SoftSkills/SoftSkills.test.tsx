import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { showModal, slugifyAuth0Id } from 'utils';
import SoftSkills from '.';
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

describe('SoftSkills Page Component', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/softSkill`)
      .reply(200, [{ id: 1, softSkillName: 'testSoftSkill1', skillLevel: 2 }]);

    nock('http://localhost:80')
      .post(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/softSkill`)
      .reply(200, {
        id: 2,
        softSkillName: 'testSoftSkill2',
        skillLevel: 2
      });

    nock('http://localhost:80')
      .delete(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/softSkill/1`)
      .reply(200, { id: 1 });

    nock('http://localhost:80')
      .get('/api/softSkillName')
      .reply(200, [
        { id: 1, name: 'testSoftSkill1' },
        { id: 2, name: 'testSoftSkill2' }
      ]);

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <SoftSkills />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    const pageTitle = screen.getByText('Umiejętności miękkie');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    fireEvent.click(screen.getByText('Wybierz...'));

    await waitFor(() => {
      const testSoftSkill = screen.getAllByText('testSoftSkill1');
      expect(testSoftSkill.length).toBe(1);
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
    await selectEvent.select(screen.getByLabelText('Nazwa'), 'testSoftSkill1');

    fireEvent.click(screen.getByText('Zapisz'));

    await waitFor(() => {
      const errors = screen.getByText('Obowiązkowe');
      expect(errors).toBeInTheDocument();
    });
  });

  test('form submits with valid data', async () => {
    await selectEvent.select(screen.getByLabelText('Nazwa'), 'testSoftSkill2');

    const softSkillLevel = screen.getByTestId('softSkillLevel-2');
    fireEvent.click(softSkillLevel);

    const mySoftSkill = screen.queryByText('Podaj swoją wartość');
    expect(mySoftSkill).not.toBeInTheDocument();

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const newTestSoftSkill = screen.queryAllByText('testSoftSkill2');
      expect(newTestSoftSkill.length).toBe(1);
    });
  });

  test('different softSkillLevel renders correctly', async () => {
    await selectEvent.select(screen.getByLabelText('Nazwa'), 'Inna');

    const mySoftSkill = screen.getByText('Podaj swoją wartość');
    expect(mySoftSkill).toBeInTheDocument();
  });

  test('softSkill delete works correctly', async () => {
    await waitFor(() => {
      const testSoftSkill = screen.queryAllByText('testSoftSkill1');
      expect(testSoftSkill.length).toBe(1);
    });

    await waitFor(() => {
      const deleteButton = screen.getByText('Usuń');
      fireEvent.click(deleteButton);
    });

    const testSoftSkill = screen.queryAllByText('testSoftSkill1');
    expect(testSoftSkill.length).toBe(0);

    await waitFor(() => {
      const noContent = screen.getByTestId('no-content');
      expect(noContent).toBeInTheDocument();
    });
  });
});
