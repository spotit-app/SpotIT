import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { Other } from './Other';

jest.mock('../../../utils/showModal', () => ({
  toggleForm: jest.fn()
}));

const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});

describe('Others Page Component', () => {
  beforeEach(() => {
    act(() => render(<Other />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Others Page renders correctly', () => {
    const pageTitle = screen.getByText('Inne');
    expect(pageTitle).toBeInTheDocument();
  });

  test('Others Form submits with valid data', async () => {
    const othersName = screen.getByLabelText('Opis');
    const othersCVClause = screen.getByLabelText('Klauzula informacyjna');

    fireEvent.change(othersName, { target: { value: 'testOthers' } });
    fireEvent.change(othersCVClause, { target: { value: 'testCVCLause' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalled();
    });
  });
});
