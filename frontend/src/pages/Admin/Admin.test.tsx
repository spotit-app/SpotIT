import { act, render, screen } from '@testing-library/react';
import Admin from '.';
import { RouterProvider } from '@/providers';

describe('Admin', () => {
  test('renders correctly', () => {
    act(() =>
      render(
        <RouterProvider>
          <Admin />
        </RouterProvider>
      )
    );

    expect(screen.getByText('Panel administracyjny')).toBeInTheDocument();
  });
});
