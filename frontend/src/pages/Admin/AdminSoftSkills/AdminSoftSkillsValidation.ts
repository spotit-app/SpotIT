import * as Yup from 'yup';
import { REQUIRED_MESSAGE, TOO_LONG_MESSAGE } from 'appConstants';

const adminSoftSkillsValidationSchema = Yup.object({
  name: Yup.string().required(REQUIRED_MESSAGE).max(50, TOO_LONG_MESSAGE)
});

export default adminSoftSkillsValidationSchema;
