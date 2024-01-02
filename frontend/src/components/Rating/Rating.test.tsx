import { render, screen, act, cleanup } from '@testing-library/react';
import { useField } from 'formik';
import { Rating } from '.';

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
    const mockHelpers = {
      setValue: jest.fn()
    };
    (useField as jest.Mock).mockReturnValue([mockField, mockMeta, mockHelpers]);

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

  test('Rating errors working', () => {
    const error = screen.getByText('Error');
    expect(error).toBeInTheDocument();
  });
});
