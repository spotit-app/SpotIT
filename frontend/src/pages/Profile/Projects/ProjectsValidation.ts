import * as Yup from 'yup';
import { REQUIRED_MESSAGE, URL_REGEX } from '../../../constants';

const projectsValidationSchema = Yup.object({
  projectName: Yup.string().required(REQUIRED_MESSAGE),
  description: Yup.string().required(REQUIRED_MESSAGE),
  projectUrl: Yup.string().matches(URL_REGEX, 'Podaj poprawny link')
});

export default projectsValidationSchema;