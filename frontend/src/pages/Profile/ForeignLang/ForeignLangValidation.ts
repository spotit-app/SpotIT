import * as Yup from 'yup';
import { REQUIRED_MESSAGE } from '../../../constants';

const foreignLangValidation = Yup.object({
  langName: Yup.string().required(REQUIRED_MESSAGE),
  langLevel: Yup.string().required(REQUIRED_MESSAGE)
});

export default foreignLangValidation;
