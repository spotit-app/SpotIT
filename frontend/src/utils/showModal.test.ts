import { showModal } from './';

describe('showModal utility function', () => {
  let originalGetElementById: typeof document.getElementById;
  let mockDialogElement: Partial<HTMLDialogElement>;

  beforeEach(() => {
    originalGetElementById = document.getElementById;
    mockDialogElement = { showModal: jest.fn() };
    document.getElementById = jest.fn().mockReturnValue(mockDialogElement);
  });

  afterEach(() => {
    document.getElementById = originalGetElementById;
  });

  test('should call showModal on modal element', () => {
    showModal();
    expect(document.getElementById).toHaveBeenCalledWith('modal');
    expect(mockDialogElement?.showModal).toHaveBeenCalled();
  });
});
