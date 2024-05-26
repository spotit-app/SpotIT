import { closeModal, mapToIds, mapToNames, successToast, errorToast } from 'utils';
import { FormikHelpers } from 'formik';
import { Button, Input, PopUpForm, TextArea } from '..';
import jobOfferValidationSchema from './JobOfferValidation';
import { FormikReactSelect } from '..';
import {
  useTechSkills,
  useSoftSkills,
  useForeignLanguages,
  useWorkModes,
  useWorkExperiences,
  useJobOffers
} from 'hooks';
import { useParams } from 'react-router-dom';
import { ReadJobOffer, WriteJobOffer } from 'types/company';

interface JobOfferFormProps {
  jobOfferToEdit?: ReadJobOffer;
}

function JobOfferForm({ jobOfferToEdit }: JobOfferFormProps) {
  const { id } = useParams<{ id: string }>();

  const { createJobOffer, updateJobOffer } = useJobOffers(+id!);
  const { techSkillNames } = useTechSkills();
  const { softSkillNames } = useSoftSkills();
  const { foreignLanguageNames } = useForeignLanguages();
  const { workModes } = useWorkModes();
  const { workExperiences } = useWorkExperiences();

  const techSkillOptions = techSkillNames?.map((techSkillName) => ({
    label: techSkillName.name,
    value: techSkillName.name
  }));
  const softSkillOptions = softSkillNames?.map((softSkillName) => ({
    label: softSkillName.name,
    value: softSkillName.name
  }));
  const foreignLanguageOptions = foreignLanguageNames?.map((foreignLanguageName) => ({
    label: foreignLanguageName.name,
    value: foreignLanguageName.id
  }));
  const workModeOptions = workModes?.map((workMode) => ({
    label: workMode.name,
    value: workMode.id
  }));
  const workExperienceOptions = workExperiences?.map((workExperience) => ({
    label: workExperience.name,
    value: workExperience.id
  }));

  function getWorkExperienceId(workExperienceName: string) {
    return (
      workExperiences?.find((workExperience) => workExperience.name === workExperienceName)?.id || 0
    );
  }

  const initialValues: WriteJobOffer = {
    name: jobOfferToEdit?.name || '',
    position: jobOfferToEdit?.position || '',
    description: jobOfferToEdit?.description || '',
    minSalary: jobOfferToEdit?.minSalary || 0,
    maxSalary: jobOfferToEdit?.maxSalary || 0,
    benefits: jobOfferToEdit?.benefits || '',
    dueDate: jobOfferToEdit?.dueDate || '',
    workExperienceId: getWorkExperienceId(jobOfferToEdit?.workExperienceName || ''),
    techSkillNames: mapToNames(jobOfferToEdit?.techSkillNames || []),
    softSkillNames: mapToNames(jobOfferToEdit?.softSkillNames || []),
    foreignLanguageNamesIds: mapToIds(jobOfferToEdit?.foreignLanguageNames || []),
    workModesIds: mapToIds(jobOfferToEdit?.workModes || [])
  };

  const onSubmit = async (
    values: WriteJobOffer,
    { setSubmitting, resetForm }: FormikHelpers<WriteJobOffer>
  ) => {
    setSubmitting(true);
    try {
      if (jobOfferToEdit) {
        await updateJobOffer.mutateAsync({ id: jobOfferToEdit.id, ...values });
      } else {
        await createJobOffer.mutateAsync(values);
      }
      successToast();
    } catch (error) {
      errorToast();
    }

    setSubmitting(false);
    resetForm();
    closeModal();
  };

  return (
    <PopUpForm<WriteJobOffer>
      initialValues={initialValues}
      validationSchema={jobOfferValidationSchema}
      onSubmit={onSubmit}
    >
      <Input name="name" label="Nazwa" type="text" id="jobOfferName" />
      <Input name="position" label="Stanowisko" type="text" id="jobOfferPosition" />
      <FormikReactSelect
        name="workExperienceId"
        label="Doświadczenie"
        options={workExperienceOptions}
      />
      <TextArea name="description" label="Opis" type="text" id="jobOfferDescription" />
      <Input
        name="minSalary"
        label="Minimalne wynagrodzenie"
        type="number"
        id="jobOfferMinSalary"
      />
      <Input
        name="maxSalary"
        label="Maksymalne wynagrodzenie"
        type="number"
        id="jobOfferMaxSalary"
      />
      <TextArea name="benefits" label="Benefity" type="text" id="jobOfferBenefits" />
      <Input name="dueDate" label="Data ważności" type="date" id="jobOfferDueDate" />
      <FormikReactSelect
        name="techSkillNames"
        label="Umiejętności techniczne"
        isMulti={true}
        options={techSkillOptions}
      />
      <FormikReactSelect
        name="softSkillNames"
        label="Umiejętności miękkie"
        isMulti={true}
        options={softSkillOptions}
      />
      <FormikReactSelect
        name="foreignLanguageNamesIds"
        label="Języki obce"
        isMulti={true}
        options={foreignLanguageOptions}
      />
      <FormikReactSelect
        name="workModesIds"
        label="Tryby pracy"
        isMulti={true}
        options={workModeOptions}
      />

      <Button type="submit" disabled={createJobOffer.isPending || updateJobOffer.isPending}>
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { JobOfferForm };
