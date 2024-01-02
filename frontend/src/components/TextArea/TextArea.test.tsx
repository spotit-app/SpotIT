import { render, screen, act, fireEvent, cleanup, waitFor } from '@testing-library/react';
import { useField } from 'formik';
import { TextArea } from '.';

jest.mock('formik');

describe('TextArea', () => {
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

    act(() => render(<TextArea {...mockProps} />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Renders a label and TextArea component', () => {
    const label = screen.getByText('testLabel');
    expect(label).toBeInTheDocument();

    const labeledElement = screen.getByLabelText('testLabel');
    expect(labeledElement).toBeInTheDocument();
  });

  test('TextArea field working', async () => {
    const labeledElement = screen.getByLabelText('testLabel');
    fireEvent.change(labeledElement, { target: { value: 'TestValue' } });

    await waitFor(() => {
      expect(labeledElement).toHaveValue('TestValue');
    });
  });

  test('TextArea errors working', () => {
    const error = screen.getByText('Error');
    expect(error).toBeInTheDocument();
  });
});
