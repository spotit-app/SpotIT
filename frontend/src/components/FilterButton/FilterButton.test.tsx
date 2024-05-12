import { render, screen, act, cleanup, fireEvent, waitFor } from '@testing-library/react';
import { FilterButton } from '.';

const mockedClick = jest.fn();

describe('FilterButton', () => {
  beforeEach(() => {
    const mockProps = {
      entity: {
        id: 1,
        name: 'Test name',
        flagUrl: 'Test url'
      },
      isChecked: false,
      onChange: mockedClick
    };

    act(() => render(<FilterButton {...mockProps} />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Renders button with content', () => {
    const button = screen.getByText('Test name');
    expect(button).toBeInTheDocument();
  });

  test('Change working properly', async () => {
    const change = screen.getByText('Test name');
    fireEvent.click(change);

    await waitFor(() => {
      expect(mockedClick).toHaveBeenCalled();
    });
  });
});
