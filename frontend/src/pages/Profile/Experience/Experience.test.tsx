import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { Experience } from './Experience';
import { showModal } from '../../../utils';

jest.mock('../../../utils/showModal', () => ({
  showModal: jest.fn()
}));

const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});

describe('Experience Page Component', () => {
  beforeEach(() => {
    act(() => render(<Experience />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Experience Page renders correctly', () => {
    const pageTitle = screen.getByText('Doświadczenie');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    const experienceList = screen.getByRole('list');
    expect(experienceList).toBeInTheDocument();
  });

  test('Button triggers formToggle function', async () => {
    const addButton = screen.getByRole('button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('Experience endDate showing up', async () => {
    const checkbox = screen.getByLabelText('Ukończono?');
    fireEvent.click(checkbox);

    const experienceEndDate = screen.getByLabelText('Data ukończenia');

    await waitFor(() => {
      expect(experienceEndDate).toBeInTheDocument();
    });
  });

  test('Experience endDate hidden on default', async () => {
    const experienceEndDate = screen.queryByLabelText('Data ukończenia');
    expect(experienceEndDate).not.toBeInTheDocument();
  });

  test('Experience Form submits with bad data', async () => {
    const experienceCompanyName = screen.getByLabelText('Nazwa firmy');
    const experiencePosition = screen.getByLabelText('Pozycja');
    const experienceStartDate = screen.getByLabelText('Data rozpoczęcia');

    fireEvent.change(experienceCompanyName, { target: { value: '' } });
    fireEvent.change(experiencePosition, { target: { value: '' } });
    fireEvent.change(experienceStartDate, { target: { value: 'BadData' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).not.toHaveBeenCalled();
    });
  });

  test('Experience Form submits with valid data', async () => {
    const experienceChecked = screen.getByLabelText('Ukończono?');
    fireEvent.click(experienceChecked);

    const experienceCompanyName = screen.getByLabelText('Nazwa firmy');
    const experiencePosition = screen.getByLabelText('Pozycja');
    const experienceStartDate = screen.getByLabelText('Data rozpoczęcia');
    const experienceEndDate = screen.getByLabelText('Data ukończenia');

    fireEvent.change(experienceCompanyName, { target: { value: 'testCompany' } });
    fireEvent.change(experiencePosition, { target: { value: 'testPosition' } });
    fireEvent.change(experienceStartDate, { target: { value: '2023-12-30' } });
    fireEvent.change(experienceEndDate, { target: { value: '2023-12-31' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalled();
    });
  });
});
