import { FormikHelpers } from 'formik';
import { ReadForeignLanguageName, WriteForeignLanguage } from 'types/profile';
import { useForeignLanguages } from 'hooks';
import validationSchema from './ForeignLanguageValidation';
import { Button, FormikReactSelect, PopUpForm } from 'components';
import { SelectOption } from 'types/shared';
import { closeModal } from 'utils';

type Empty = '';
interface ForeignLanguageForm {
  languageLevel: string;
  foreignLanguageNameId: Empty | number;
}

function ForeignLanguageForm() {
  const { foreignLanguageNames, createForeignLanguage } = useForeignLanguages();

  const levelToSelect = ['A1', 'A2', 'B1', 'B2', 'C1', 'C2', 'OJCZYSTY'].map((level: string) => ({
    value: level,
    label: level
  }));

  const foreignLanguageNamesToSelect: SelectOption[] | undefined = foreignLanguageNames?.map(
    (foreignLanguageName: ReadForeignLanguageName) => ({
      value: foreignLanguageName.id,
      label: foreignLanguageName.name
    })
  );

  const initialValues: ForeignLanguageForm = {
    languageLevel: levelToSelect[0].value,
    foreignLanguageNameId: ''
  };

  const onSubmit = async (
    values: ForeignLanguageForm,
    { setSubmitting, resetForm }: FormikHelpers<ForeignLanguageForm>
  ) => {
    setSubmitting(true);
    await createForeignLanguage.mutateAsync(values as WriteForeignLanguage);
    setSubmitting(false);
    resetForm();
    closeModal();
  };

  return (
    <PopUpForm<ForeignLanguageForm>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <FormikReactSelect
        label="Nazwa języka"
        name="foreignLanguageNameId"
        options={[...(foreignLanguageNamesToSelect || [])]}
      />
      <FormikReactSelect label="Poziom" name="languageLevel" options={levelToSelect} />
      <Button type="submit" disabled={createForeignLanguage.isPending}>
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { ForeignLanguageForm };
