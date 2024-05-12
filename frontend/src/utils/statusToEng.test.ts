import { translateStatus } from './statusToEng';

describe('Returns correct equivalent', () => {
  test('should return', () => {
    expect(translateStatus('Dostarczono')).toEqual('DELIVERED');
    expect(translateStatus('Zaakceptowano')).toEqual('ACCEPTED');
    expect(translateStatus('Odrzucono')).toEqual('REJECTED');
    expect(translateStatus('inne')).toEqual('');
  });
});
