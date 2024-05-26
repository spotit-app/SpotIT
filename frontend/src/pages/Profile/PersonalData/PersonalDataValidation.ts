import * as Yup from 'yup';
import { REQUIRED_MESSAGE, PHONE_NUMBER_REGEX, TOO_LONG_MESSAGE } from 'appConstants';

const personalDataValidationSchema = Yup.object({
  firstName: Yup.string().required(REQUIRED_MESSAGE).max(50, TOO_LONG_MESSAGE),
  lastName: Yup.string().required(REQUIRED_MESSAGE).max(50, TOO_LONG_MESSAGE),
  email: Yup.string()
    .email('Podaj poprawny email')
    .required(REQUIRED_MESSAGE)
    .max(100, TOO_LONG_MESSAGE),
  phoneNumber: Yup.string()
    .matches(PHONE_NUMBER_REGEX, 'Niepoprawny numer telefonu')
    .max(17, TOO_LONG_MESSAGE),
  profilePicture: Yup.string(),
  position: Yup.string().min(2, 'Podaj poprawną pozycję').max(50, TOO_LONG_MESSAGE),
  isOpen: Yup.bool()
});

export default personalDataValidationSchema;
