import * as Yup from 'yup';
import { REQUIRED_MESSAGE, TOO_LONG_MESSAGE } from 'appConstants';

const jobOfferValidationSchema = Yup.object({
  name: Yup.string().required(REQUIRED_MESSAGE).max(100, TOO_LONG_MESSAGE),
  position: Yup.string().required(REQUIRED_MESSAGE).max(100, TOO_LONG_MESSAGE),
  description: Yup.string().required(REQUIRED_MESSAGE),
  minSalary: Yup.number().required(REQUIRED_MESSAGE).min(0, 'Wynagrodzenie musi być większe od 0'),
  maxSalary: Yup.number()
    .min(0, 'Wynagrodzenie musi być większe od 0')
    .test(
      'is-greater',
      'Maksymalne wynagrodzenie musi być wyższe od minimalnego',
      function (value) {
        return value! > this.parent.minSalary;
      }
    ),
  benefits: Yup.string().required(REQUIRED_MESSAGE),
  dueDate: Yup.string().required(REQUIRED_MESSAGE),
  workExperienceId: Yup.number().required(REQUIRED_MESSAGE).min(1, REQUIRED_MESSAGE),
  techSkillNames: Yup.array().of(Yup.string().required(REQUIRED_MESSAGE)).min(1, REQUIRED_MESSAGE),
  softSkillNames: Yup.array().of(Yup.string().required(REQUIRED_MESSAGE)).min(1, REQUIRED_MESSAGE),
  foreignLanguageNamesIds: Yup.array()
    .of(Yup.string().required(REQUIRED_MESSAGE))
    .min(1, REQUIRED_MESSAGE),
  workModesIds: Yup.array().of(Yup.number().required(REQUIRED_MESSAGE)).min(1, REQUIRED_MESSAGE)
});

export default jobOfferValidationSchema;
