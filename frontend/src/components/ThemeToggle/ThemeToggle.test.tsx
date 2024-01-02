import { screen, render, fireEvent } from '@testing-library/react';
import { LIGHT_THEME, DARK_THEME } from 'appConstants';
import { ThemeToggle } from '.';

describe('ThemeToggle', () => {
  test('renders ThemeToggle component', () => {
    render(<ThemeToggle />);
    const themeToggle = screen.getByTestId('theme-toggle');
    expect(themeToggle).toBeInTheDocument();

    fireEvent.click(themeToggle);
    expect(localStorage.getItem('theme')).toBe(DARK_THEME);

    fireEvent.click(themeToggle);
    expect(localStorage.getItem('theme')).toBe(LIGHT_THEME);
  });
});
