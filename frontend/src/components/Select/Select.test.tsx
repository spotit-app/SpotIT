import { render, screen, act, cleanup, fireEvent, waitFor } from '@testing-library/react';
import { Select } from './';
import { useField } from 'formik';

jest.mock('formik');

describe('Select', () => {
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
      name: 'testName',
      placeholder: 'testPlaceholder',
      options: ['testOption1', 'testOption2']
    };

    act(() => render(<Select {...mockProps} />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Renders a label and Select component with placeholder', () => {
    const label = screen.getByText('testLabel');
    expect(label).toBeInTheDocument();

    const placeholder = screen.getByText('testPlaceholder');
    expect(placeholder).toBeInTheDocument();

    const labeledElement = screen.getByLabelText('testLabel');
    expect(labeledElement).toBeInTheDocument();
  });

  test('Select field working', async () => {
    const labeledElement = screen.getByLabelText('testLabel');
    fireEvent.change(labeledElement, { target: { value: 'testOption1' } });

    await waitFor(() => {
      expect(labeledElement).toHaveValue('testOption1');
    });
  });

  test('Select field only lets to choose from the options list', async () => {
    const labeledElement = screen.getByLabelText('testLabel');
    fireEvent.change(labeledElement, { target: { value: 'testOption3' } });

    await waitFor(() => {
      expect(labeledElement).not.toHaveValue('testOption3');
    });
  });

  test('Select errors working', () => {
    const error = screen.getByText('Error');
    expect(error).toBeInTheDocument();
  });
});
