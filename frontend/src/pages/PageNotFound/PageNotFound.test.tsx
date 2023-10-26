import { render, screen } from '@testing-library/react';
import { PageNotFound } from './PageNotFound';

describe('PageNotFound', () => {
  test('renders PageNotFound component', () => {
    render(<PageNotFound />);
    expect(screen.getByText(/404/)).toBeInTheDocument();
  });
});
