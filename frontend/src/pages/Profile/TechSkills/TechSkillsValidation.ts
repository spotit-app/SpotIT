import * as Yup from 'yup';
import { REQUIRED_MESSAGE } from '../../../constants';

const techSkillsValidationSchema = Yup.object({
  techSkillName: Yup.string().required(REQUIRED_MESSAGE),
  customTechSkillName: Yup.string().when('techSkillName', (techSkillName, schema) => {
    if (techSkillName.toString() === 'Inna') return schema.required(REQUIRED_MESSAGE);
    return schema;
  }),
  techSkillLevel: Yup.number().min(1, REQUIRED_MESSAGE)
});

export default techSkillsValidationSchema;
