import * as Yup from 'yup';
import { REQUIRED_MESSAGE, TOO_LONG_MESSAGE } from 'appConstants';

const companiesValidationSchema = Yup.object({
  name: Yup.string().required(REQUIRED_MESSAGE).max(100, TOO_LONG_MESSAGE),
  nip: Yup.number()
    .required(REQUIRED_MESSAGE)
    .test('len', 'Długość NIP musi wynosić 10 cyfr', (val) => val?.toString().length === 10)
    .positive('NIP musi być liczbą dodatnią'),
  regon: Yup.number()
    .required(REQUIRED_MESSAGE)
    .test('len', 'Długość REGON musi wynosić 9 cyfr', (val) => val?.toString().length === 9)
    .positive('NIP musi być liczbą dodatnią'),
  websiteUrl: Yup.string().required(REQUIRED_MESSAGE).max(255, TOO_LONG_MESSAGE),
  country: Yup.string().required(REQUIRED_MESSAGE).max(100, TOO_LONG_MESSAGE),
  zipCode: Yup.string().required(REQUIRED_MESSAGE).max(10, TOO_LONG_MESSAGE),
  city: Yup.string().required(REQUIRED_MESSAGE).max(50, TOO_LONG_MESSAGE),
  street: Yup.string().required(REQUIRED_MESSAGE).max(100, TOO_LONG_MESSAGE)
});

export default companiesValidationSchema;
