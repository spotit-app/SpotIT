import * as Yup from 'yup';
import { REQUIRED_MESSAGE } from '../../../constants';

const softSkillsValidationSchema = Yup.object({
  softSkillName: Yup.string().required(REQUIRED_MESSAGE),
  customSoftSkillName: Yup.string().when('softSkillName', (softSkillName, schema) => {
    if (softSkillName.toString() === 'Inna') return schema.required(REQUIRED_MESSAGE);
    return schema;
  }),
  softSkillLevel: Yup.number().min(1, REQUIRED_MESSAGE)
});

export default softSkillsValidationSchema;
