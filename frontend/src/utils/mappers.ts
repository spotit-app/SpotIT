export const mapToNames = (array: { name: string }[]) => {
  return array.map((element) => element.name);
};

export const mapToIds = (array: { id: number }[]) => {
  return array.map((element) => element.id);
};
