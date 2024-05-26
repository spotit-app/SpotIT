import { closeModal, successToast, errorToast } from 'utils';
import { useForeignLanguages } from 'hooks';
import { Button, Input, PopUpForm } from 'components';
import adminForeignLanguagesValidationSchema from './AdminForeignLanguagesValidation';
import {
  ReadForeignLanguageName,
  ForeignLanguageNamesFormType,
  WriteForeignLanguageName
} from 'types/profile';
import { Field, FormikHelpers, FormikProps } from 'formik';
import PropTypes from 'prop-types';
import { ChangeEvent, useRef } from 'react';

interface AdminForeignLanguagesFormProps {
  foreignLanguageToEdit?: ReadForeignLanguageName;
}

function AdminForeignLanguagesForm({ foreignLanguageToEdit }: AdminForeignLanguagesFormProps) {
  const flagRef = useRef<HTMLInputElement>(null);
  const { createForeignLanguageName, updateForeignLanguageName } = useForeignLanguages();

  const initialValues: WriteForeignLanguageName = {
    name: foreignLanguageToEdit?.name || '',
    flag: ''
  };

  const onSubmit = async (
    values: ForeignLanguageNamesFormType,
    { setSubmitting, resetForm }: FormikHelpers<ForeignLanguageNamesFormType>
  ) => {
    const formData = new FormData();
    formData.append('name', new Blob([values.name], { type: 'application/json' }));
    values.flag && formData.append('flag', values.flag as Blob);
    setSubmitting(true);
    try {
      if (foreignLanguageToEdit) {
        await updateForeignLanguageName.mutateAsync({ id: foreignLanguageToEdit.id, formData });
      } else {
        await createForeignLanguageName.mutateAsync(formData);
      }
      successToast();
    } catch (error) {
      errorToast();
    }
    setSubmitting(false);
    resetForm();
    if (flagRef.current) {
      flagRef.current.value = '';
    }
    closeModal();
  };

  return (
    <PopUpForm<ForeignLanguageNamesFormType>
      initialValues={initialValues}
      validationSchema={adminForeignLanguagesValidationSchema(foreignLanguageToEdit)}
      onSubmit={onSubmit}
    >
      <Input label="Nazwa" name="name" id="name" />
      <label htmlFor="flag" className="block text-sm font-medium leading-6 mb-1">
        Zmień zdjęcie
      </label>
      <Field name="flag">
        {({ form }: { form: FormikProps<ForeignLanguageNamesFormType> }) => (
          <>
            <input
              ref={flagRef}
              data-testid="logo"
              className="file-input file-input-bordered w-full"
              aria-describedby="user_avatar_help"
              id="flag"
              type="file"
              accept="image/png, image/jpeg, image/jpg"
              onChange={(event: ChangeEvent<HTMLInputElement>) => {
                if (event.currentTarget.files) {
                  form.setFieldValue('flag', event.currentTarget.files[0]);
                }
              }}
            />
            <div className="text-red-500 h-6">
              {form.touched.flag && form.errors.flag && (
                <div className="error">{form.errors.flag}</div>
              )}
            </div>
          </>
        )}
      </Field>
      <Button
        type="submit"
        disabled={createForeignLanguageName.isPending || updateForeignLanguageName.isPending}
      >
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { AdminForeignLanguagesForm };

AdminForeignLanguagesForm.propTypes = {
  foreignLanguageToEdit: PropTypes.object
};
