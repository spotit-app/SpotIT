import { ChangeEvent, useRef } from 'react';
import { useCompanies } from 'hooks';
import { useParams } from 'react-router-dom';
import { Field, FormikHelpers, FormikProps } from 'formik';
import { Button, MyCompanyJobOffers, Input, Loading, MainForm } from 'components';
import validationSchema from './CompanyValidation';
import icons from 'assets/icons';
import { successToast, errorToast } from 'utils';

interface CompanyForm {
  name: string;
  nip: string;
  regon: string;
  websiteUrl: string;
  country: string;
  zipCode: string;
  city: string;
  street: string;
  profilePicture: File | string;
}

function MyCompany() {
  const { id } = useParams<{ id: string }>();
  const { getCompany, updateCompany } = useCompanies();
  const { data: company, isPending: companyIsPending } = getCompany(+id!);
  const profilePictureRef = useRef<HTMLInputElement>(null);

  const initialValues: CompanyForm = {
    name: company?.name || '',
    nip: company?.nip || '',
    regon: company?.regon || '',
    websiteUrl: company?.websiteUrl || '',
    country: company?.address.country || '',
    zipCode: company?.address.zipCode || '',
    city: company?.address.city || '',
    street: company?.address.street || '',
    profilePicture: ''
  };

  const onSubmit = async (values: CompanyForm, { setSubmitting }: FormikHelpers<CompanyForm>) => {
    const updatedCompany: FormData = new FormData();
    const writeDto = {
      name: values.name,
      nip: values.nip,
      regon: values.regon,
      websiteUrl: values.websiteUrl,
      address: {
        country: values.country,
        zipCode: values.zipCode,
        city: values.city,
        street: values.street
      }
    };
    updatedCompany.append(
      'writeDto',
      new Blob([JSON.stringify(writeDto)], { type: 'application/json' })
    );
    updatedCompany.append('profilePicture', values.profilePicture as Blob);
    setSubmitting(true);
    try {
      await updateCompany.mutateAsync({ id: +id!, formData: updatedCompany });
      successToast();
    } catch (error) {
      errorToast();
    }
    setSubmitting(false);

    if (profilePictureRef.current) {
      profilePictureRef.current.value = '';
    }
  };

  return companyIsPending ? (
    <Loading />
  ) : (
    <div>
      <MainForm<CompanyForm>
        title="Dane firmy"
        initialValues={initialValues}
        validationSchema={validationSchema}
        onSubmit={onSubmit}
      >
        <div className="lg:flex flex-wrap">
          <div className="lg:w-1/2 lg:p-2">
            <Input label="Nazwa" name="name" id="name" type="text" />
          </div>
          <div className="lg:w-1/2 lg:p-2">
            <Input label="NIP" name="nip" id="nip" type="number" />
          </div>
          <div className="lg:w-1/2 lg:p-2">
            <Input label="REGON" name="regon" id="regon" type="number" />
          </div>
          <div className="lg:w-1/2 lg:p-2">
            <Input label="Strona internetowa" name="websiteUrl" id="websiteUrl" type="text" />
          </div>
          <div className="lg:w-1/2 lg:p-2">
            <Input label="Kraj" name="country" id="country" type="text" />
          </div>
          <div className="lg:w-1/2 lg:p-2">
            <Input label="Kod pocztowy" name="zipCode" id="zipCode" type="text" />
          </div>
          <div className="lg:w-1/2 lg:p-2">
            <Input label="Miasto" name="city" id="city" type="text" />
          </div>
          <div className="lg:w-1/2 lg:p-2">
            <Input label="Ulica" name="street" id="street" type="text" />
          </div>
          <div className="lg:w-1/2 lg:p-2 flex items-center">
            {company?.logoUrl ? (
              <img src={company.logoUrl} alt="Profile Picture" className="w-20 h-20 rounded-full" />
            ) : (
              <div className="w-20 h-20 bg-red flex justify-center items-center">
                <icons.BsBuildings size="45" className="text-primary" data-testid="building-icon" />
              </div>
            )}
            <div className="ml-3">
              <label htmlFor="profilePicture" className="block text-sm font-medium leading-6 mb-1">
                Zmień zdjęcie
              </label>
              <Field name="profilePicture">
                {({ form }: { form: FormikProps<CompanyForm> }) => (
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
          <div className="flex items-end justify-end lg:w-1/2 lg:px-2 mt-3 lg:mt-0">
            <Button type="submit" disabled={updateCompany.isPending}>
              Zapisz
            </Button>
          </div>
        </div>
      </MainForm>

      <div className="divider divider-primary" />

      <MyCompanyJobOffers />
    </div>
  );
}

export default MyCompany;
