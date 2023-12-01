import { render, fireEvent, act, screen, waitFor, cleanup } from '@testing-library/react';
import { Socials } from './Socials';
import { showModal } from '../../../utils';

jest.mock('../../../utils/showModal', () => ({
  showModal: jest.fn()
}));

const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});

describe('Socials Page Component', () => {
  beforeEach(() => {
    act(() => render(<Socials />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Socials Page renders correctly', () => {
    const pageTitle = screen.getByText('Konta społecznościowe');
    expect(pageTitle).toBeInTheDocument();

    const addButton = screen.getByRole('button');
    expect(addButton).toBeInTheDocument();

    const socialsList = screen.getByRole('list');
    expect(socialsList).toBeInTheDocument();
  });

  test('Button triggers formToggle function', async () => {
    const addButton = screen.getByRole('button');
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(showModal).toHaveBeenCalled();
    });
  });

  test('Socials Form submits with bad data', async () => {
    const socialName = screen.getByLabelText('Nazwa konta społecznościowego');
    const socialLink = screen.getByLabelText('Link do konta');

    fireEvent.change(socialName, { target: { value: '' } });
    fireEvent.change(socialLink, { target: { value: '' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).not.toHaveBeenCalled();
    });
  });

  test('Socials Form submits with valid data', async () => {
    const socialName = screen.getByLabelText('Nazwa konta społecznościowego');
    const socialLink = screen.getByLabelText('Link do konta');

    fireEvent.change(socialName, { target: { value: 'testName' } });
    fireEvent.change(socialLink, { target: { value: 'www.testLink.pl' } });

    const submitButton = screen.getByText('Zapisz');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalled();
    });
  });
});
