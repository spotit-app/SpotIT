import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { showModal, slugifyAuth0Id } from 'utils';
import TechSkills from '.';
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

describe('TechSkills Page Component', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/techSkill`)
      .reply(200, [
        { id: 1, techSkillName: 'testTechSkill1', skillLevel: 2, logoUrl: 'testLogoUrl1' }
      ]);

    nock('http://localhost:80')
      .post(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/techSkill`)
      .reply(200, {
        id: 2,
        techSkillName: 'testTechSkill2',
        skillLevel: 2
      });

    nock('http://localhost:80')
      .delete(`/api/userAccount/${slugifyAuth0Id('auth0|1234567890')}/techSkill/1`)
      .reply(200, { id: 1 });

    nock('http://localhost:80')
      .get('/api/techSkillName')
      .reply(200, [
        { id: 1, name: 'testTechSkill1', logoUrl: 'testLogoUrl1' },
        { id: 2, name: 'testTechSkill2', logoUrl: 'testLogoUrl2' }
      ]);

    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <TechSkills />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    const pageTitle = screen.getByText('Umiejętności techniczne');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    await waitFor(() => {
      const testTechSkill = screen.getAllByText('testTechSkill1');
      expect(testTechSkill.length).toBe(1);
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
    await selectEvent.select(screen.getByLabelText('Nazwa'), 'testTechSkill1');

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const errors = screen.getByText('Obowiązkowe');
      expect(errors).toBeInTheDocument();
    });
  });

  test('form submits with valid data', async () => {
    await selectEvent.select(screen.getByLabelText('Nazwa'), 'testTechSkill2');

    const techSkillLevel = screen.getByTestId('techSkillLevel-2');
    fireEvent.click(techSkillLevel);

    const myTechSkill = screen.queryByText('Podaj swoją wartość');
    expect(myTechSkill).not.toBeInTheDocument();

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      const newTestTechSkill = screen.queryAllByText('testTechSkill2');
      expect(newTestTechSkill.length).toBe(1);
    });
  });

  test('different techSkillLevel renders correctly', async () => {
    await selectEvent.select(screen.getByLabelText('Nazwa'), 'Inna');

    const myTechSkill = screen.getByText('Podaj swoją wartość');
    expect(myTechSkill).toBeInTheDocument();
  });

  test('techSkill delete works correctly', async () => {
    await waitFor(() => {
      const testTechSkill = screen.queryAllByText('testTechSkill1');
      expect(testTechSkill.length).toBe(1);
    });

    await waitFor(() => {
      const deleteButton = screen.getByText('Usuń');
      fireEvent.click(deleteButton);
    });

    const testTechSkill = screen.queryAllByText('testTechSkill1');
    expect(testTechSkill.length).toBe(0);

    await waitFor(() => {
      const noContent = screen.getByTestId('no-content');
      expect(noContent).toBeInTheDocument();
    });
  });
});
