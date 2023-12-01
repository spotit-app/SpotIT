import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { ForeignLang } from './ForeignLang';

import { showModal } from '../../../utils';

jest.mock('../../../utils/showModal', () => ({
  showModal: jest.fn()
}));

const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});

describe('ForeignLang Page Component', () => {
  beforeEach(() => {
    act(() => render(<ForeignLang />));
  });

  afterEach(() => {
    cleanup();
  });

  test('ForeignLang Page renders correctly', () => {
    const pageTitle = screen.getByText('Języki obce');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    const foreignLangList = screen.getByRole('list');
    expect(foreignLangList).toBeInTheDocument();
  });

  test('Button triggers formToggle function', async () => {
    const addButton = screen.getByRole('button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('ForeignLang Form submits with bad data', async () => {
    const foreingLangName = screen.getByLabelText('Nazwa języka');
    const foreingLangLevel = screen.getByLabelText('Poziom');

    fireEvent.change(foreingLangName, { target: { value: '' } });
    fireEvent.change(foreingLangLevel, { target: { value: '' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).not.toHaveBeenCalled();
    });
  });

  test('ForeignLang Form submits with valid data', async () => {
    const foreingLangName = screen.getByLabelText('Nazwa języka');
    const foreingLangLevel = screen.getByLabelText('Poziom');

    fireEvent.change(foreingLangName, { target: { value: 'Język1' } });
    fireEvent.change(foreingLangLevel, { target: { value: 'A1' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalled();
    });
  });
});
