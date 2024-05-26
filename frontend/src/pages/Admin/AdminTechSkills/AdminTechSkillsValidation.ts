import * as Yup from 'yup';
import { REQUIRED_MESSAGE, TOO_LONG_MESSAGE } from 'appConstants';
import { ReadTechSkillName } from 'types/profile';

const adminTechSkillsValidationSchema = (techSkillToEdit: ReadTechSkillName | undefined) =>
  Yup.object({
    name: Yup.string().required(REQUIRED_MESSAGE).max(50, TOO_LONG_MESSAGE),
    logo: techSkillToEdit ? Yup.string() : Yup.mixed().required(REQUIRED_MESSAGE)
  });

export default adminTechSkillsValidationSchema;
