import * as Yup from 'yup';
import { REQUIRED_MESSAGE } from 'appConstants';

const foreignLanguageValidation = Yup.object({
  foreignLanguageNameId: Yup.number().required(REQUIRED_MESSAGE),
  languageLevel: Yup.string().required(REQUIRED_MESSAGE)
});

export default foreignLanguageValidation;
