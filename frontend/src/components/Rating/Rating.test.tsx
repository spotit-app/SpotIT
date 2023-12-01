import { render, screen, act, cleanup, fireEvent, waitFor } from '@testing-library/react';
import { Rating } from './';
import { useField } from 'formik';

jest.mock('formik');

describe('Rating', () => {
  beforeEach(() => {
    const mockMeta = {
      touched: true,
      error: 'Error',
      initialError: '',
      initialTouched: false,
      initialValue: '',
      value: ''
    };
    const mockField = {
      checked: false,
      onChange: jest.fn()
    };
    (useField as jest.Mock).mockReturnValue([mockField, mockMeta]);

    const mockProps = {
      label: 'testLabel',
      initialValue: 0,
      name: 'testName',
      id: 'testId'
    };

    act(() => render(<Rating {...mockProps} />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Renders rating component with stars', () => {
    const label = screen.getByText('testLabel');
    expect(label).toBeInTheDocument();

    const labeledElement = screen.getByLabelText('testLabel');
    expect(labeledElement).toBeInTheDocument();

    const starInputs = screen.getAllByRole('radio');
    expect(starInputs.length).toBe(6);
  });

  test('Default value star is hidden', () => {
    const starNr0 = screen.getByLabelText('testLabel');
    expect(starNr0).toBeInTheDocument();
    expect(starNr0).toHaveAttribute('class', 'hidden');
  });

  test('Rating stars change values working', async () => {
    const starNr4 = screen.getByTestId('testId-3');
    fireEvent.click(starNr4);

    await waitFor(() => {
      expect(starNr4).toBeChecked();
    });
  });

  test('Rating errors working', () => {
    const error = screen.getByText('Error');
    expect(error).toBeInTheDocument();
  });
});
