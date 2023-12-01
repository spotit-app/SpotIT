import { render, fireEvent, act, cleanup, screen, waitFor } from '@testing-library/react';
import { FormikHelpers } from 'formik';
import * as Yup from 'yup';
import { Button } from '../Button';
import { Input } from '../Input';
import { PopUpForm } from './';

describe('PopUpForm', () => {
  const mockFn = jest.fn((x) => x);

  beforeEach(() => {
    const initialValues = { name: '', email: '' };
    const validationSchema = Yup.object({
      name: Yup.string().required('Name is required'),
      email: Yup.string().email('Invalid email').required('Email is required')
    });
    const onSubmit = (
      values: typeof initialValues,
      actions: FormikHelpers<typeof initialValues>
    ) => {
      setTimeout(() => {
        mockFn(values);
        actions.setSubmitting(false);
      }, 400);
    };

    const mockProps = {
      initialValues,
      validationSchema,
      onSubmit
    };

    act(() =>
      render(
        <PopUpForm<typeof initialValues> {...mockProps}>
          <Input label="Name" id="name" name="name" type="text" />
          <Input label="Email" id="email" name="email" type="email" />
          <Button type="submit">Submit</Button>
        </PopUpForm>
      )
    );
  });

  afterEach(() => {
    cleanup();
  });

  test('ValidationSchema working', async () => {
    const nameField = screen.getByLabelText('Name');
    fireEvent.change(nameField, { target: { value: '' } });

    const emailField = screen.getByLabelText('Email');
    fireEvent.change(emailField, { target: { value: 'Bad email' } });

    const submitButton = screen.getByText('Submit');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(mockFn).not.toHaveBeenCalled();
    });
  });

  test('Form with given fields submitted correctly', async () => {
    const nameField = screen.getByLabelText('Name');
    fireEvent.change(nameField, { target: { value: 'John Doe' } });

    const emailField = screen.getByLabelText('Email');
    fireEvent.change(emailField, { target: { value: 'john@example.com' } });

    const submitButton = screen.getByText('Submit');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(mockFn).toHaveBeenCalledWith({ name: 'John Doe', email: 'john@example.com' });
    });
  });
});
