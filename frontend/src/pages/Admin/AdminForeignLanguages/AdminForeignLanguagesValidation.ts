import * as Yup from 'yup';
import { REQUIRED_MESSAGE, TOO_LONG_MESSAGE } from 'appConstants';
import { ReadForeignLanguageName } from 'types/profile';

const adminForeignLanguagesValidationSchema = (
  foreignLanguageToEdit: ReadForeignLanguageName | undefined
) =>
  Yup.object({
    name: Yup.string().required(REQUIRED_MESSAGE).max(50, TOO_LONG_MESSAGE),
    flag: foreignLanguageToEdit ? Yup.string() : Yup.mixed().required(REQUIRED_MESSAGE)
  });

export default adminForeignLanguagesValidationSchema;
