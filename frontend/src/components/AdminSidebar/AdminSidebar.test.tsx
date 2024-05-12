import { act, render, screen } from '@testing-library/react';
import { AdminSidebar } from '.';
import { RouterProvider } from 'providers';

describe('AdminSidebar', () => {
  test('renders correctly', () => {
    act(() =>
      render(
        <RouterProvider>
          <AdminSidebar />
        </RouterProvider>
      )
    );

    expect(screen.getByText('Edukacja')).toBeInTheDocument();
    expect(screen.getByText('Umiejętności techniczne')).toBeInTheDocument();
    expect(screen.getByText('Umiejętności miękkie')).toBeInTheDocument();
    expect(screen.getByText('Języki obce')).toBeInTheDocument();
    expect(screen.getByText('Poziom doświadczenia')).toBeInTheDocument();
    expect(screen.getByText('Tryby pracy')).toBeInTheDocument();
  });
});
