import * as Yup from 'yup';
import { REQUIRED_MESSAGE, TOO_LONG_MESSAGE, URL_REGEX } from 'appConstants';

const projectsValidationSchema = Yup.object({
  name: Yup.string().required(REQUIRED_MESSAGE).max(100, TOO_LONG_MESSAGE),
  description: Yup.string().required(REQUIRED_MESSAGE),
  projectUrl: Yup.string().matches(URL_REGEX, 'Podaj poprawny link').max(255, TOO_LONG_MESSAGE)
});

export default projectsValidationSchema;
