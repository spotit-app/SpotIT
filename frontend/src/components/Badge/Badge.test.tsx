import { act, render, screen } from '@testing-library/react';
import { Badge } from '.';

describe('Badge', () => {
  test('renders correctly', () => {
    act(() => render(<Badge name="Test" />));

    expect(screen.getByText('Test')).toBeInTheDocument();
  });
});
