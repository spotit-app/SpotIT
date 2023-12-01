import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { PersonalData } from './PersonalData';

jest.mock('../../../utils/showModal', () => ({
  toggleForm: jest.fn()
}));

const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});

describe('PersonalData Page Component', () => {
  beforeEach(() => {
    act(() => render(<PersonalData />));
  });

  afterEach(() => {
    cleanup();
  });

  test('PersonalData Page renders correctly', () => {
    const pageTitle = screen.getByText('Dane osobowe');
    expect(pageTitle).toBeInTheDocument();
  });

  test('PersonalData Form submits with valid data', async () => {
    const personalDataName = screen.getByLabelText('Imię i nazwisko');
    const personalDataEmail = screen.getByLabelText('Email');
    const personalDataPhoneNumber = screen.getByLabelText('Numer kontaktowy');
    const personalDataPosition = screen.getByLabelText('Pozycja');

    fireEvent.change(personalDataName, { target: { value: 'testName' } });
    fireEvent.change(personalDataEmail, { target: { value: 'test@test.com' } });
    fireEvent.change(personalDataPhoneNumber, { target: { value: '123456789' } });
    fireEvent.change(personalDataPosition, { target: { value: 'testPos' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalled();
    });
  });
});
