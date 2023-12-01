import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { SoftSkills } from './SoftSkills';
import { showModal } from '../../../utils';

jest.mock('../../../utils/showModal', () => ({
  showModal: jest.fn()
}));

const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});

describe('SoftSkills Page Component', () => {
  beforeEach(() => {
    act(() => render(<SoftSkills />));
  });

  afterEach(() => {
    cleanup();
  });

  test('SoftSkills Page renders correctly', () => {
    const pageTitle = screen.getByText('Umiejętności miękkie');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    const softSkillList = screen.getByRole('list');
    expect(softSkillList).toBeInTheDocument();
  });

  test('Input customName appears after certain selected option', async () => {
    const softSkillName = screen.getByLabelText('Nazwa');
    fireEvent.change(softSkillName, { target: { value: 'Inna' } });

    await waitFor(() => {
      expect(screen.getByLabelText('Podaj swoją wartość')).toBeInTheDocument();
    });
  });

  test('Input customName is on default not visible', async () => {
    const softSkillName = screen.getByLabelText('Nazwa');
    fireEvent.change(softSkillName, { target: { value: 'Umiejętność1' } });

    await waitFor(() => {
      expect(screen.queryByLabelText('Podaj swoją wartość')).not.toBeInTheDocument();
    });
  });

  test('Button triggers formToggle function', async () => {
    const addButton = screen.getByRole('button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('SoftSkills Form submits with bad data', async () => {
    const softSkillName = screen.getByLabelText('Nazwa');
    const starNr4 = screen.getByTestId('softSkillLevel-3');

    fireEvent.change(softSkillName, { target: { value: '' } });
    fireEvent.change(starNr4, { target: { value: '' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).not.toHaveBeenCalled();
    });
  });

  test('SoftSkills Form submits with valid data', async () => {
    const softSkillName = screen.getByLabelText('Nazwa');
    const starNr4 = screen.getByTestId('softSkillLevel-3');

    fireEvent.change(softSkillName, { target: { value: 'Umiejętność1' } });
    fireEvent.click(starNr4);

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalled();
    });
  });
});
