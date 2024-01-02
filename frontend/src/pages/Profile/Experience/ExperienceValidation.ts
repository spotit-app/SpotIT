import * as Yup from 'yup';
import { REQUIRED_MESSAGE } from 'appConstants';

const experienceValidationSchema = Yup.object({
  companyName: Yup.string().required(REQUIRED_MESSAGE),
  position: Yup.string().required(REQUIRED_MESSAGE),
  startDate: Yup.date().required(REQUIRED_MESSAGE),
  isChecked: Yup.boolean(),
  endDate: Yup.date().when('isChecked', (isChecked, schema) => {
    if (isChecked.toString() === 'true') return schema.required(REQUIRED_MESSAGE);
    return schema;
  })
});

export default experienceValidationSchema;
