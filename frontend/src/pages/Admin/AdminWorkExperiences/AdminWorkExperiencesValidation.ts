import * as Yup from 'yup';
import { REQUIRED_MESSAGE, TOO_LONG_MESSAGE } from 'appConstants';

const adminWorkExperiencesValidationSchema = Yup.object({
  name: Yup.string().required(REQUIRED_MESSAGE).max(20, TOO_LONG_MESSAGE)
});

export default adminWorkExperiencesValidationSchema;
