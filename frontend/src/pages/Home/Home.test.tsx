import { render, screen } from '@testing-library/react';
import Home from '.';

test('Renders SpotIT', () => {
  render(<Home />);
  const header = screen.getByText('Spot IT');
  expect(header).toBeInTheDocument();
});
