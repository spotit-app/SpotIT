import * as Yup from 'yup';
import { REQUIRED_MESSAGE } from '../../../constants';

const interestsValidationSchema = Yup.object({
  interestName: Yup.string().required(REQUIRED_MESSAGE)
});

export default interestsValidationSchema;
