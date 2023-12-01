import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { TechSkills } from './TechSkills';
import { showModal } from '../../../utils';

jest.mock('../../../utils/showModal', () => ({
  showModal: jest.fn()
}));

const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});

describe('TechSkills Page Component', () => {
  beforeEach(() => {
    act(() => render(<TechSkills />));
  });

  afterEach(() => {
    cleanup();
  });

  test('TechSkills Page renders correctly', () => {
    const pageTitle = screen.getByText('Umiejętności techniczne');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    const techSkillList = screen.getByRole('list');
    expect(techSkillList).toBeInTheDocument();
  });

  test('Button triggers formToggle function', async () => {
    const addButton = screen.getByRole('button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('Input customName appears after certain selected option', async () => {
    const techSkillName = screen.getByLabelText('Nazwa');
    fireEvent.change(techSkillName, { target: { value: 'Inna' } });

    await waitFor(() => {
      expect(screen.getByLabelText('Podaj swoją wartość')).toBeInTheDocument();
    });
  });

  test('Input customName is on default not visible', async () => {
    const techSkillName = screen.getByLabelText('Nazwa');
    fireEvent.change(techSkillName, { target: { value: 'Umiejętność1' } });

    await waitFor(() => {
      expect(screen.queryByLabelText('Podaj swoją wartość')).not.toBeInTheDocument();
    });
  });

  test('TechSkills Form submits with bad data', async () => {
    const techSkillName = screen.getByLabelText('Nazwa');
    fireEvent.change(techSkillName, { target: { value: '' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).not.toHaveBeenCalled();
    });
  });

  test('TechSkills Form submits with valid data', async () => {
    const techSkillName = screen.getByLabelText('Nazwa');
    const starNr4 = screen.getByTestId('techSkillLevel-3');

    fireEvent.change(techSkillName, { target: { value: 'Umiejętność1' } });
    fireEvent.click(starNr4);

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalled();
    });
  });
});
