import { Button, Input, PopUpForm } from 'components';
import { FormikHelpers } from 'formik';
import { CompanyForm, WriteCompany } from 'types/company';
import { closeModal, successToast, errorToast } from 'utils';
import companiesValidationSchema from './CompaniesValidation';
import { useCompanies } from 'hooks';

function CompaniesForm() {
  const { createCompany } = useCompanies();

  const initialValues: CompanyForm = {
    name: '',
    nip: '',
    regon: '',
    websiteUrl: '',
    country: '',
    zipCode: '',
    city: '',
    street: ''
  };

  const onSubmit = async (
    values: CompanyForm,
    { setSubmitting, resetForm }: FormikHelpers<CompanyForm>
  ) => {
    const company: WriteCompany = {
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
    setSubmitting(true);
    try {
      await createCompany.mutateAsync(company);
      successToast();
    } catch (error) {
      errorToast();
    }
    setSubmitting(false);
    resetForm();
    closeModal();
  };

  return (
    <PopUpForm<CompanyForm>
      initialValues={initialValues}
      validationSchema={companiesValidationSchema}
      onSubmit={onSubmit}
    >
      <Input label="Nazwa firmy" name="name" id="companyName" type="text" />
      <Input label="NIP" name="nip" id="companyNip" type="number" />
      <Input label="REGON" name="regon" id="companyRegon" type="number" />
      <Input label="Strona internetowa" name="websiteUrl" id="companyWebsite" type="text" />
      <div className="divider">Dane adresowe</div>
      <Input label="Kraj" name="country" id="companyCountry" type="text" />
      <Input label="Kod pocztowy" name="zipCode" id="companyZipCode" type="text" />
      <Input label="Miasto" name="city" id="companyCity" type="text" />
      <Input label="Ulica" name="street" id="companyStreet" type="text" />
      <Button type="submit" disabled={createCompany.isPending}>
        Zapisz
      </Button>
    </PopUpForm>
  );
}

export { CompaniesForm };
