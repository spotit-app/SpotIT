import { act, render, screen, fireEvent } from '@testing-library/react';
import AdminWelcomePage from '.';

const mockedUsedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockedUsedNavigate
}));

describe('AdminWelcomePage', () => {
  beforeEach(() => {
    act(() => render(<AdminWelcomePage />));
  });

  test('renders correctly', () => {
    expect(screen.getByText('Witaj w panelu administracyjnym!')).toBeInTheDocument();
  });

  test('redirects to edit panel correctly', () => {
    const editPanelButton = screen.getByText('Edytuj dane');
    fireEvent.click(editPanelButton);

    expect(mockedUsedNavigate).toHaveBeenCalled();
  });
});
