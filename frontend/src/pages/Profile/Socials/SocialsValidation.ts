import * as Yup from 'yup';
import { REQUIRED_MESSAGE, URL_REGEX } from '../../../constants';

const socialsValidationSchema = Yup.object({
  socialsName: Yup.string().required(REQUIRED_MESSAGE),
  socialsLink: Yup.string().matches(URL_REGEX, 'Podaj poprawny link').required(REQUIRED_MESSAGE)
});

export default socialsValidationSchema;
