import { render, screen, act, cleanup, fireEvent, waitFor } from '@testing-library/react';
import { FilterStatusButton } from '.';

const mockedClick = jest.fn();

describe('FilterStatusButton', () => {
  beforeEach(() => {
    const mockProps = {
      entity: 'Test text',
      isChecked: false,
      onChange: mockedClick
    };

    act(() => render(<FilterStatusButton {...mockProps} />));
  });

  afterEach(() => {
    cleanup();
  });

  test('Renders button with content', () => {
    const button = screen.getByText('Test text');
    expect(button).toBeInTheDocument();
  });

  test('Change working properly', async () => {
    const change = screen.getByText('Test text');
    fireEvent.click(change);

    await waitFor(() => {
      expect(mockedClick).toHaveBeenCalled();
    });
  });
});
