import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { Education } from './Education';
import { showModal } from '../../../utils';

jest.mock('../../../utils/showModal', () => ({
  showModal: jest.fn()
}));

const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});

describe('Education Page Component', () => {
  beforeEach(() => {
    act(() => render(<Education />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Education Page renders correctly', () => {
    const pageTitle = screen.getByText('Edukacja');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    const educationList = screen.getByRole('list');
    expect(educationList).toBeInTheDocument();
  });

  test('Button triggers formToggle function', async () => {
    const addButton = screen.getByRole('button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('Input customName appears after certain selected option', async () => {
    const educationNameInput = screen.getByLabelText('Stopień edukacji');
    fireEvent.change(educationNameInput, { target: { value: 'Inny' } });

    await waitFor(() => {
      expect(screen.getByLabelText('Podaj swoją wartość')).toBeInTheDocument();
    });
  });

  test('Input customName is on default not visible', async () => {
    const educationNameInput = screen.getByLabelText('Stopień edukacji');
    fireEvent.change(educationNameInput, { target: { value: 'Stopień1' } });

    await waitFor(() => {
      expect(screen.queryByLabelText('Podaj swoją wartość')).not.toBeInTheDocument();
    });
  });

  test('Education endDate showing up', async () => {
    const checkbox = screen.getByLabelText('Ukończono?');
    fireEvent.click(checkbox);

    const educationEndDate = screen.getByLabelText('Data ukończenia');

    await waitFor(() => {
      expect(educationEndDate).toBeInTheDocument();
    });
  });

  test('Education endDate hidden on default', async () => {
    const educationEndDate = screen.queryByLabelText('Data ukończenia');
    expect(educationEndDate).not.toBeInTheDocument();
  });

  test('Education Form submits with bad data', async () => {
    const educationSchoolName = screen.getByLabelText('Nazwa szkoły');
    const educationNameInput = screen.getByLabelText('Stopień edukacji');
    const educationFaculty = screen.getByLabelText('Kierunek');
    const educationStartDate = screen.getByLabelText('Data rozpoczęcia');

    fireEvent.change(educationSchoolName, { target: { value: '' } });
    fireEvent.change(educationNameInput, { target: { value: '' } });
    fireEvent.change(educationFaculty, { target: { value: '' } });
    fireEvent.change(educationStartDate, { target: { value: 'BadData' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).not.toHaveBeenCalled();
    });
  });

  test('Education Form submits with valid data', async () => {
    const educationChecked = screen.getByLabelText('Ukończono?');
    fireEvent.click(educationChecked);

    const educationSchoolName = screen.getByLabelText('Nazwa szkoły');
    const educationNameInput = screen.getByLabelText('Stopień edukacji');
    const educationFaculty = screen.getByLabelText('Kierunek');
    const educationStartDate = screen.getByLabelText('Data rozpoczęcia');
    const educationEndDate = screen.getByLabelText('Data ukończenia');

    fireEvent.change(educationSchoolName, { target: { value: 'testEducation' } });
    fireEvent.change(educationNameInput, { target: { value: 'Stopień1' } });
    fireEvent.change(educationFaculty, { target: { value: 'testFaculty' } });
    fireEvent.change(educationStartDate, { target: { value: '2023-12-30' } });
    fireEvent.change(educationEndDate, { target: { value: '2023-12-31' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalled();
    });
  });
});
