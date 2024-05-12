import { act, cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import { Pagination } from '.';

const mockedChangePage = jest.fn();

describe('Pagination', () => {
  beforeEach(() => {
    const mockProps = {
      page: 1,
      isFirst: true,
      isLast: false,
      changePage: mockedChangePage
    };

    act(() => render(<Pagination {...mockProps} />));
  });

  afterEach(() => {
    cleanup();
  });

  test('renders correctly', async () => {
    await waitFor(() => {
      expect(screen.getByText('Strona 1')).toBeInTheDocument();
    });
  });

  test('disabled on true', async () => {
    const previousPage = screen.getByText('«');
    await waitFor(() => {
      expect(previousPage).toBeDisabled();
    });
  });

  test('change page working properly', async () => {
    const nextPage = screen.getByText('»');
    fireEvent.click(nextPage);

    await waitFor(() => {
      expect(mockedChangePage).toHaveBeenCalled();
    });
  });
});
