import { Field, FormikHelpers } from 'formik';
import icons from '../../../assets/icons/iconImports';
import validationSchema from './PersonalDataValidation';
import { Input, Button, MainForm } from '../../../components';

interface PersonalDataFormTypes {
  name: string;
  email: string;
  phoneNumber: string;
  profilePicture: string;
  position: string;
}

function PersonalData() {
  const initialValues = {
    name: '',
    email: '',
    phoneNumber: '',
    profilePicture: '',
    position: ''
  };

  const onSubmit = (
    values: PersonalDataFormTypes,
    { setSubmitting }: FormikHelpers<PersonalDataFormTypes>
  ) => {
    setTimeout(() => {
      window.alert(JSON.stringify(values));
      setSubmitting(false);
    }, 400);
  };

  return (
    <MainForm<PersonalDataFormTypes>
      title="Dane osobowe"
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Imię i nazwisko" name="name" id="name" type="text" />
      <Input label="Email" name="email" id="email" type="email" />
      <Input
        label="Numer kontaktowy"
        name="phoneNumber"
        id="phoneNumber"
        type="tel"
        inputMode="tel"
      />
      <Input label="Pozycja" name="position" id="position" type="text" />
      <div className="sm:col-span-4">
        <label htmlFor="profilePicture" className="block text-sm font-medium leading-6">
          Twoje zdjęcie
        </label>
        <div className="mt-2 flex items-center gap-x-3">
          <div className="avatar">
            <div className="w-24 rounded-full ring ring-primary ring-offset-base-100 ring-offset-1">
              <icons.BsFillPersonFill
                className="ml-5 mt-4 h-14 w-14 text-gray-300"
                aria-hidden="true"
              />
            </div>
          </div>
          <Field
            className="block-inline w-full text-sm border border-gray-300 rounded-lg cursor-pointer bg-base-100 focus:outline-none"
            aria-describedby="user_avatar_help"
            name="profilePicture"
            id="profilePicture"
            type="file"
          />
        </div>
        <div className="mt-2 text-sm text-gray-500 dark:text-gray-300" id="user_avatar_help">
          Zapisane zdjęcie będzie widoczne na twoim profilu
        </div>
      </div>
      <Button type="submit">Zapisz</Button>
    </MainForm>
  );
}

export { PersonalData };
