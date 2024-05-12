import { act, render, screen, fireEvent, cleanup } from '@testing-library/react';
import Home from '.';

const mockedUsedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockedUsedNavigate
}));

describe('Home', () => {
  beforeEach(() => {
    act(() => render(<Home />));
  });

  afterEach(() => {
    jest.clearAllMocks();
    cleanup;
  });

  test('renders correctly', () => {
    expect(screen.getByText('wyszukiwarka')).toBeInTheDocument();
  });

  test('navigates to job offers page', () => {
    const button = screen.getByText('Oferty');
    fireEvent.click(button);

    expect(mockedUsedNavigate).toHaveBeenCalled();
  });

  test('navigates to employees', () => {
    const button = screen.getByText('Pracownicy');
    fireEvent.click(button);

    expect(mockedUsedNavigate).toHaveBeenCalled();
  });
});
