import { closeModal, successToast, errorToast } from 'utils';
import { useTechSkills } from 'hooks';
import { Button, Input, PopUpForm } from 'components';
import adminTechSkillsValidationSchema from './AdminTechSkillsValidation';
import { ReadTechSkillName, TechSkillNamesFormType, WriteTechSkillName } from 'types/profile';
import { Field, FormikHelpers, FormikProps } from 'formik';
import PropTypes from 'prop-types';
import { ChangeEvent, useRef } from 'react';

interface AdminTechSkillsFormProps {
  techSkillToEdit?: ReadTechSkillName;
}

function AdminTechSkillsForm({ techSkillToEdit }: AdminTechSkillsFormProps) {
  const logoRef = useRef<HTMLInputElement>(null);
  const { createTechSkillName, updateTechSkillName } = useTechSkills();

  const initialValues: WriteTechSkillName = {
    name: techSkillToEdit?.name || '',
    logo: ''
  };

  const onSubmit = async (
    values: TechSkillNamesFormType,
    { setSubmitting, resetForm }: FormikHelpers<TechSkillNamesFormType>
  ) => {
    const formData = new FormData();
    formData.append('name', new Blob([values.name], { type: 'application/json' }));
    values.logo && formData.append('logo', values.logo as Blob);
    setSubmitting(true);
    try {
      if (techSkillToEdit) {
        await updateTechSkillName.mutateAsync({ id: techSkillToEdit.id, formData });
      } else {
        await createTechSkillName.mutateAsync(formData);
      }
      successToast();
    } catch (error) {
      errorToast();
    }
    setSubmitting(false);
    resetForm();
    if (logoRef.current) {
      logoRef.current.value = '';
    }
    closeModal();
  };

  return (
    <PopUpForm<TechSkillNamesFormType>
      initialValues={initialValues}
      validationSchema={adminTechSkillsValidationSchema(techSkillToEdit)}
      onSubmit={onSubmit}
    >
      <Input label="Nazwa" name="name" id="name" />
      <label htmlFor="logo" className="block text-sm font-medium leading-6 mb-1">
        Zmień zdjęcie
      </label>
      <Field name="logo">
        {({ form }: { form: FormikProps<TechSkillNamesFormType> }) => (
          <>
            <input
              ref={logoRef}
              className="file-input file-input-bordered w-full"
              aria-describedby="user_avatar_help"
              data-testid="logo"
              id="logo"
              type="file"
              accept="image/png, image/jpeg, image/jpg"
              onChange={(event: ChangeEvent<HTMLInputElement>) => {
                if (event.currentTarget.files) {
                  form.setFieldValue('logo', event.currentTarget.files[0]);
                }
              }}
            />
            <div className="text-red-500 h-6">
              {form.touched.logo && form.errors.logo && (
                <div className="error">{form.errors.logo}</div>
              )}
            </div>
          </>
        )}
      </Field>
      <Button
        type="submit"
        disabled={createTechSkillName.isPending || updateTechSkillName.isPending}
      >
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { AdminTechSkillsForm };

AdminTechSkillsForm.propTypes = {
  techSkillToEdit: PropTypes.object
};
