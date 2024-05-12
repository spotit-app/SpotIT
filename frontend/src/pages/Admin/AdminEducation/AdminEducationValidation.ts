import * as Yup from 'yup';
import { REQUIRED_MESSAGE } from 'appConstants';

const adminEducationValidationSchema = Yup.object({
  name: Yup.string().required(REQUIRED_MESSAGE)
});

export default adminEducationValidationSchema;
