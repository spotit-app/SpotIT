import { act, render, screen, waitFor, fireEvent, cleanup } from '@testing-library/react';
import AdminTechSkills from '.';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuth0 } from '@auth0/auth0-react';
import nock from 'nock';
import { showModal } from 'utils';

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

describe('AdminTechSkill', () => {
  beforeEach(() => {
    nock('http://localhost:80')
      .get(`/api/techSkillName`)
      .reply(200, [
        { id: 1, name: 'testTechSkillName1', logoUrl: 'testLogoUrl1' },
        { id: 2, name: 'testTechSkillName2', logoUrl: 'testLogoUrl2' }
      ]);
    nock('http://localhost:80')
      .post(`/api/techSkillName`)
      .reply(201, { id: 3, name: 'testTechSkillName3', logoUrl: 'testLogoUrl3' });
    nock('http://localhost:80')
      .put(`/api/techSkillName/1`)
      .reply(200, { id: 1, name: 'testTechSkillName1Updated', logoUrl: 'testLogoUrl1' });
    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <AdminTechSkills />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    expect(screen.getByText('Umiejętności techniczne')).toBeInTheDocument();
    await waitFor(() => expect(screen.getByText('testTechSkillName1')).toBeInTheDocument());
  });

  test('button triggers formToggle function', async () => {
    const addButton = screen.getByTestId('add-button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('add new tech skill name', async () => {
    const addButton = screen.getByTestId('add-button');
    fireEvent.click(addButton);

    const name = screen.getByLabelText('Nazwa');
    fireEvent.change(name, { target: { value: 'testTechSkillName3' } });

    const logo = screen.getByTestId('logo');
    fireEvent.change(logo, {
      target: {
        files: [new File(['testTechSkillName3'], 'testTechSkillName3.png', { type: 'image/png' })]
      }
    });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('testTechSkillName2')).toBeInTheDocument();
    });
  });

  test('add new tech skill name without logo', async () => {
    const addButton = screen.getByTestId('add-button');
    fireEvent.click(addButton);

    const name = screen.getByLabelText('Nazwa');
    fireEvent.change(name, { target: { value: 'testTechSkillName3' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('Obowiązkowe')).toBeInTheDocument();
    });
  });

  test('edit tech skill name', async () => {
    await waitFor(() => {
      const editButton = screen.queryAllByText('Edytuj')[0];
      fireEvent.click(editButton);
    });

    const name = screen.getByLabelText('Nazwa');
    fireEvent.change(name, { target: { value: 'testTechSkillName1Updated' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('testTechSkillName1Updated')).toBeInTheDocument();
    });
  });
});

describe('AdminTechSkill without data', () => {
  beforeEach(() => {
    nock('http://localhost:80').get(`/api/techSkillName`).reply(200, []);
    act(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <AdminTechSkills />
        </QueryClientProvider>
      )
    );
  });

  afterEach(() => {
    queryClient.clear();
    cleanup();
  });

  test('renders correctly', async () => {
    expect(screen.getByText('Umiejętności techniczne')).toBeInTheDocument();
    await waitFor(() => expect(screen.getByTestId('no-content')).toBeInTheDocument());
  });
});
