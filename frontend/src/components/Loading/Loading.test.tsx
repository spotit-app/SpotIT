import { render, screen } from '@testing-library/react';
import { Loading } from '.';

describe('Loading', () => {
  test('renders Loading component', () => {
    render(<Loading />);
    expect(screen.getByTestId('loading')).toBeInTheDocument();
  });
});
