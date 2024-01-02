import { render, screen, act, cleanup } from '@testing-library/react';
import { PopUp } from '.';

describe('PopUp', () => {
  beforeEach(() => {
    const mockProps = {
      title: 'testTitle'
    };

    act(() =>
      render(
        <PopUp {...mockProps}>
          <span>Sample Content</span>
        </PopUp>
      )
    );
  });

  afterEach(() => {
    cleanup();
  });

  test('Renders a PopUp with title and children', () => {
    const title = screen.getByText('testTitle');
    expect(title).toBeInTheDocument();

    const children = screen.getByText('Sample Content');
    expect(children).toBeInTheDocument();
  });
});
