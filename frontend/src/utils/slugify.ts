import slugify from 'slugify';

const toSlug = (text: string) => {
  return slugify(text, {
    lower: true,
    remove: /[*+~.()'"!:@]/g
  });
};

export { toSlug };
