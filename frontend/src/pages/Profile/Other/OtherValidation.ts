import * as Yup from 'yup';

const otherValidationSchema = Yup.object({
  description: Yup.string(),
  cvClause: Yup.string()
});

export default otherValidationSchema;
