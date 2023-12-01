import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { Projects } from './Projects';
import { showModal } from '../../../utils';

jest.mock('../../../utils/showModal', () => ({
  showModal: jest.fn()
}));

const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});

describe('Projects Page Component', () => {
  beforeEach(() => {
    act(() => render(<Projects />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Projects Page renders correctly', () => {
    const pageTitle = screen.getByText('Projekty');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    const projectsList = screen.getByRole('list');
    expect(projectsList).toBeInTheDocument();
  });

  test('Button triggers formToggle function', async () => {
    const addButton = screen.getByRole('button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('Projects Form submits with bad data', async () => {
    const projectName = screen.getByLabelText('Nazwa projektu');
    const projectDesc = screen.getByLabelText('Opis');
    const projectLink = screen.getByLabelText('Link do projektu');

    fireEvent.change(projectName, { target: { value: '' } });
    fireEvent.change(projectDesc, { target: { value: '' } });
    fireEvent.change(projectLink, { target: { value: '' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).not.toHaveBeenCalled();
    });
  });

  test('Projects Form submits with valid data', async () => {
    const projectName = screen.getByLabelText('Nazwa projektu');
    const projectDesc = screen.getByLabelText('Opis');
    const projectLink = screen.getByLabelText('Link do projektu');

    fireEvent.change(projectName, { target: { value: 'testName' } });
    fireEvent.change(projectDesc, { target: { value: 'testDesc' } });
    fireEvent.change(projectLink, { target: { value: 'www.testLink.pl' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalled();
    });
  });
});
