import { render, screen, act, fireEvent, cleanup, waitFor } from '@testing-library/react';
import { useField } from 'formik';
import { Input } from '.';

jest.mock('formik');

describe('Input', () => {
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
      id: 'testId',
      name: 'testName'
    };

    act(() => render(<Input {...mockProps} />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Renders a label and Input component', () => {
    const label = screen.getByText('testLabel');
    expect(label).toBeInTheDocument();

    const labeledElement = screen.getByLabelText('testLabel');
    expect(labeledElement).toBeInTheDocument();
  });

  test('Input field working', async () => {
    const labeledElement = screen.getByLabelText('testLabel');
    fireEvent.change(labeledElement, { target: { value: 'TestValue' } });

    await waitFor(() => {
      expect(labeledElement).toHaveValue('TestValue');
    });
  });

  test('Input errors working', () => {
    const error = screen.getByText('Error');
    expect(error).toBeInTheDocument();
  });
});
