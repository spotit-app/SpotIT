import { Field, FormikHelpers, FormikProps } from 'formik';
import { ChangeEvent, useRef, useState } from 'react';
import { Input, Button, MainForm, Loading, CheckBox } from 'components';
import validationSchema from './PersonalDataValidation';
import { useUser } from 'hooks';
import { successToast, errorToast } from 'utils';

interface PersonalDataFormTypes {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  profilePicture: File | string;
  position: string;
}

function PersonalData() {
  const { userPersonalData, userDataIsPending, updateUser } = useUser();
  const profilePictureRef = useRef<HTMLInputElement>(null);
  const [isChecked, setIsChecked] = useState(userPersonalData?.isOpen || false);

  const initialValues = {
    firstName: userPersonalData?.firstName || '',
    lastName: userPersonalData?.lastName || '',
    email: userPersonalData?.email || '',
    phoneNumber: userPersonalData?.phoneNumber || '',
    profilePicture: '',
    position: userPersonalData?.position || '',
    isOpen: userPersonalData?.isOpen || false
  };

  const onSubmit = async (
    values: PersonalDataFormTypes,
    { setSubmitting }: FormikHelpers<PersonalDataFormTypes>
  ) => {
    const updatedUser: FormData = new FormData();
    updatedUser.append(
      'userData',
      new Blob([JSON.stringify(values)], { type: 'application/json' })
    );
    updatedUser.append('profilePicture', values.profilePicture as Blob);
    setSubmitting(true);
    try {
      await updateUser.mutateAsync(updatedUser);
      successToast();
    } catch (error) {
      errorToast();
    }
    setSubmitting(false);

    if (profilePictureRef.current) {
      profilePictureRef.current.value = '';
    }
  };

  return userDataIsPending ? (
    <Loading />
  ) : (
    <div className="block w-full">
      <div className="flex felx-col w-full">
        <MainForm<PersonalDataFormTypes>
          title="Dane osobowe"
          initialValues={initialValues}
          validationSchema={validationSchema}
          onSubmit={onSubmit}
        >
          <div className="lg:flex flex-wrap">
            <div className="lg:w-1/2 lg:p-2">
              <Input label="Imię" name="firstName" id="firstName" type="text" />
            </div>
            <div className="lg:w-1/2 lg:p-2">
              <Input label="Nazwisko" name="lastName" id="lastName" type="text" />
            </div>
            <div className="lg:w-1/2 lg:p-2">
              <Input label="Email" name="email" id="email" type="email" />
            </div>
            <div className="lg:w-1/2 lg:p-2">
              <Input
                label="Numer kontaktowy"
                name="phoneNumber"
                id="phoneNumber"
                type="tel"
                inputMode="tel"
              />
            </div>
            <div className="lg:w-1/2 lg:p-2 flex items-center">
              <Input label="Pozycja" name="position" id="position" type="text" />
            </div>
            <div className="lg:w-1/2 lg:p-2">
              <label htmlFor="profilePicture" className="block text-sm font-medium leading-6 mb-1">
                Zmień zdjęcie
              </label>
              <Field name="profilePicture">
                {({ form }: { form: FormikProps<PersonalDataFormTypes> }) => (
                  <input
                    ref={profilePictureRef}
                    className="file-input file-input-bordered w-full"
                    aria-describedby="user_avatar_help"
                    id="profilePicture"
                    type="file"
                    accept="image/png, image/jpeg, image/jpg"
                    onChange={(event: ChangeEvent<HTMLInputElement>) => {
                      if (event.currentTarget.files) {
                        form.setFieldValue('profilePicture', event.currentTarget.files[0]);
                      }
                    }}
                  />
                )}
              </Field>
            </div>
          </div>
          <div className="w-full my-2 lg:p-2 flex items-center">
            <CheckBox
              label="Szukasz pracy?"
              name="isOpen"
              id="isOpen"
              checked={isChecked}
              customOnChange={(e: ChangeEvent<HTMLInputElement>) => {
                setIsChecked(e.target.checked);
              }}
            />
          </div>
          <div className="lg:p-2">
            <Button type="submit" className="ml-auto block" disabled={updateUser.isPending}>
              Zapisz
            </Button>
          </div>
        </MainForm>
      </div>
    </div>
  );
}

export default PersonalData;
