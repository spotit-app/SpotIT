import { mapToNames, mapToIds } from './mappers';

const objects = [
  { id: 1, name: 'name1' },
  { id: 2, name: 'name2' }
];

describe('Map to names', () => {
  test('should map to names', () => {
    expect(mapToNames(objects)).toEqual(['name1', 'name2']);
  });
});

describe('Map to ids', () => {
  test('should map to ids', () => {
    expect(mapToIds(objects)).toEqual([1, 2]);
  });
});
