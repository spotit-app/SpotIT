import { render, screen, act, cleanup } from '@testing-library/react';
import { Button } from '.';

describe('Button', () => {
  beforeEach(() => {
    const type = 'submit';
    const children = 'Click';
    act(() => render(<Button type={type}>{children}</Button>));
  });

  afterEach(() => {
    cleanup();
  });

  test('Renders button with content', () => {
    const button = screen.getByText('Click');
    expect(button).toBeInTheDocument();
  });

  test('Button has correct type attribute', () => {
    const button = screen.getByText('Click');
    expect(button).toHaveAttribute('type', 'submit');
  });
});
