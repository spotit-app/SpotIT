import { render, screen } from '@testing-library/react';
import { Home } from './Home';

test('Renders SpotIT', () => {
  render(<Home />);
  const header = screen.getByText('SpotIT');
  expect(header).toBeInTheDocument();
});
