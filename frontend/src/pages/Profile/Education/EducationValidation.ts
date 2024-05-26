import * as Yup from 'yup';
import { REQUIRED_MESSAGE, TOO_LONG_MESSAGE } from 'appConstants';

const educationValidationSchema = Yup.object({
  educationLevel: Yup.string().required(REQUIRED_MESSAGE),
  customEducationLevel: Yup.string().when('educationLevel', (educationLevel, schema) => {
    if (educationLevel.toString() === 'Inny') return schema.required(REQUIRED_MESSAGE);
    return schema;
  }),
  schoolName: Yup.string().required(REQUIRED_MESSAGE).max(50, TOO_LONG_MESSAGE),
  faculty: Yup.string().max(50, TOO_LONG_MESSAGE),
  startDate: Yup.date().required(REQUIRED_MESSAGE),
  isChecked: Yup.boolean(),
  endDate: Yup.date().when('isChecked', (isChecked, schema) => {
    if (isChecked.toString() === 'true') return schema.required(REQUIRED_MESSAGE);
    return schema;
  })
});

export default educationValidationSchema;
