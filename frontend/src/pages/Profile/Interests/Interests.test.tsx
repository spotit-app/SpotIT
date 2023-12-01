import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { Interests } from './Interests';
import { showModal } from '../../../utils';

jest.mock('../../../utils/showModal', () => ({
  showModal: jest.fn()
}));

const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});

describe('Interests Page Component', () => {
  beforeEach(() => {
    act(() => render(<Interests />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Interests Page renders correctly', () => {
    const pageTitle = screen.getByText('Zainteresowania');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    const interestsList = screen.getByRole('list');
    expect(interestsList).toBeInTheDocument();
  });

  test('Button triggers formToggle function', async () => {
    const addButton = screen.getByRole('button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('Interests Form submits with bad data', async () => {
    const interestName = screen.getByLabelText('Hobby');
    fireEvent.change(interestName, { target: { value: '' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).not.toHaveBeenCalled();
    });
  });

  test('Interests Form submits with valid data', async () => {
    const interestName = screen.getByLabelText('Hobby');
    fireEvent.change(interestName, { target: { value: 'testInterest' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalled();
    });
  });
});
