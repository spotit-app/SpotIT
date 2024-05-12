import * as Yup from 'yup';
import { REQUIRED_MESSAGE } from 'appConstants';

const adminWorkExperiencesValidationSchema = Yup.object({
  name: Yup.string().required(REQUIRED_MESSAGE)
});

export default adminWorkExperiencesValidationSchema;
