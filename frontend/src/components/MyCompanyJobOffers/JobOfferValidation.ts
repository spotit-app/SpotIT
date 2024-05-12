import * as Yup from 'yup';
import { REQUIRED_MESSAGE } from 'appConstants';

const jobOfferValidationSchema = Yup.object({
  name: Yup.string().required(REQUIRED_MESSAGE),
  position: Yup.string().required(REQUIRED_MESSAGE),
  description: Yup.string().required(REQUIRED_MESSAGE),
  minSalary: Yup.number().required(REQUIRED_MESSAGE),
  maxSalary: Yup.number(),
  benefits: Yup.string().required(REQUIRED_MESSAGE),
  dueDate: Yup.string().required(REQUIRED_MESSAGE),
  workExperienceId: Yup.number().required(REQUIRED_MESSAGE),
  techSkillNames: Yup.array().of(Yup.string().required(REQUIRED_MESSAGE)).min(1, REQUIRED_MESSAGE),
  softSkillNames: Yup.array().of(Yup.string().required(REQUIRED_MESSAGE)).min(1, REQUIRED_MESSAGE),
  foreignLanguageNamesIds: Yup.array()
    .of(Yup.string().required(REQUIRED_MESSAGE))
    .min(1, REQUIRED_MESSAGE),
  workModesIds: Yup.array().of(Yup.number().required(REQUIRED_MESSAGE)).min(1, REQUIRED_MESSAGE)
});

export default jobOfferValidationSchema;
