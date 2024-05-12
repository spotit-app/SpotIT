import * as Yup from 'yup';
import { REQUIRED_MESSAGE } from 'appConstants';
import { ReadTechSkillName } from 'types/profile';

const adminTechSkillsValidationSchema = (techSkillToEdit: ReadTechSkillName | undefined) =>
  Yup.object({
    name: Yup.string().required(REQUIRED_MESSAGE),
    logo: techSkillToEdit ? Yup.string() : Yup.mixed().required(REQUIRED_MESSAGE)
  });

export default adminTechSkillsValidationSchema;
