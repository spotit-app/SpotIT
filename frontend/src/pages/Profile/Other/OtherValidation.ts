import * as Yup from 'yup';

const otherValidationSchema = Yup.object({
  otherDescription: Yup.string(),
  cvClause: Yup.string()
});

export default otherValidationSchema;
