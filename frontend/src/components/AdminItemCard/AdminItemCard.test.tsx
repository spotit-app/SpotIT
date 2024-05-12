import { render, screen, act } from '@testing-library/react';
import { AdminItemCard } from '.';

describe('AdminItemCard', () => {
  test('renders with logo', () => {
    act(() => render(<AdminItemCard name="Test" logo="test.png" />));

    const logo = screen.getByTestId('admin-item-card-logo');
    expect(logo).toBeInTheDocument();
    expect(screen.getByText('Test')).toBeInTheDocument();
  });

  test('renders without logo', () => {
    act(() => render(<AdminItemCard name="Test" />));

    expect(screen.queryByTestId('admin-item-card-logo')).toBeNull();
    expect(screen.getByText('Test')).toBeInTheDocument();
  });

  test('renders with children', () => {
    act(() =>
      render(
        <AdminItemCard name="Test">
          <div>Child</div>
        </AdminItemCard>
      )
    );

    expect(screen.getByText('Child')).toBeInTheDocument();
  });
});
