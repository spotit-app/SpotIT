import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { Courses } from './Courses';
import { showModal } from '../../../utils';

jest.mock('../../../utils/showModal', () => ({
  showModal: jest.fn()
}));

const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});

describe('Courses Page Components', () => {
  beforeEach(() => {
    act(() => render(<Courses />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Courses Page renders correctly', () => {
    const pageTitle = screen.getByText('Kursy');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    const coursesList = screen.getByRole('list');
    expect(coursesList).toBeInTheDocument();
  });

  test('Button triggers formToggle function', async () => {
    const addButton = screen.getByRole('button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('Courses Form submits with bad data', async () => {
    const courseNameInput = screen.getByLabelText('Nazwa kursu');
    const courseEndDateInput = screen.getByLabelText('Data ukończenia');

    fireEvent.change(courseNameInput, { target: { value: '' } });
    fireEvent.change(courseEndDateInput, { target: { value: 'BadData' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).not.toHaveBeenCalled();
    });
  });

  test('Courses Form submits with valid data', async () => {
    const courseNameInput = screen.getByLabelText('Nazwa kursu');
    const courseEndDateInput = screen.getByLabelText('Data ukończenia');

    fireEvent.change(courseNameInput, { target: { value: 'Example Course' } });
    fireEvent.change(courseEndDateInput, { target: { value: '2023-12-31' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalled();
    });
  });
});
