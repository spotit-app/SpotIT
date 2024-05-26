import * as Yup from 'yup';
import { REQUIRED_MESSAGE, TOO_LONG_MESSAGE, URL_REGEX } from 'appConstants';

const socialsValidationSchema = Yup.object({
  name: Yup.string().required(REQUIRED_MESSAGE).max(30, TOO_LONG_MESSAGE),
  socialUrl: Yup.string()
    .matches(URL_REGEX, 'Podaj poprawny link')
    .required(REQUIRED_MESSAGE)
    .max(255, TOO_LONG_MESSAGE)
});

export default socialsValidationSchema;
