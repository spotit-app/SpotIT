import * as Yup from 'yup';
import { REQUIRED_MESSAGE } from '../../../constants';

const coursesValidationSchema = Yup.object({
  courseName: Yup.string().required(REQUIRED_MESSAGE),
  courseEndDate: Yup.date().required(REQUIRED_MESSAGE)
});

export default coursesValidationSchema;
