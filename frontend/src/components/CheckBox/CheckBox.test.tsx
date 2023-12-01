import { render, screen, act, fireEvent, cleanup, waitFor } from '@testing-library/react';
import { CheckBox } from './';
import { useField } from 'formik';

jest.mock('formik');

describe('CheckBox', () => {
  beforeEach(() => {
    const mockField = {
      checked: false,
      onChange: jest.fn()
    };
    (useField as jest.Mock).mockReturnValue([mockField]);

    const mockProps = {
      label: 'testLabel',
      id: 'testId',
      name: 'testName',
      checked: true
    };

    act(() => render(<CheckBox {...mockProps} />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Renders a label and CheckBox component', () => {
    const label = screen.getByText('testLabel');
    expect(label).toBeInTheDocument();

    const labeledElement = screen.getByLabelText('testLabel');
    expect(labeledElement).toBeInTheDocument();
  });

  test('CheckBox default checked working', () => {
    const labeledElement = screen.getByLabelText('testLabel');
    expect(labeledElement).toBeChecked();
  });

  test('CheckBox field working', async () => {
    const labeledElement = screen.getByLabelText('testLabel');
    fireEvent.change(labeledElement, { target: { checked: false } });

    await waitFor(() => {
      expect(labeledElement).not.toBeChecked();
    });
  });
});
