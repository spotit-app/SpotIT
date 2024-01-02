import { FormikHelpers } from 'formik';
import { TextArea, Button, MainForm, Loading } from 'components';
import validationSchema from './OtherValidation';
import { useUser } from 'hooks/useUser';

interface OtherFormTypes {
  description: string;
  cvClause: string;
}

function Other() {
  const { userDataOthers, userDataIsPending, updateUser } = useUser();

  const initialValues = {
    description: userDataOthers?.description || '',
    cvClause: userDataOthers?.cvClause || ''
  };

  const onSubmit = async (
    values: OtherFormTypes,
    { setSubmitting }: FormikHelpers<OtherFormTypes>
  ) => {
    setSubmitting(true);
    const updatedUser: FormData = new FormData();
    updatedUser.append(
      'userData',
      new Blob([JSON.stringify(values)], { type: 'application/json' })
    );

    setSubmitting(false);
    await updateUser.mutateAsync(updatedUser);
    setSubmitting(false);
  };

  return userDataIsPending ? (
    <Loading />
  ) : (
    <MainForm<OtherFormTypes>
      title="Inne"
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <TextArea label="Opis" name="description" id="otherDescription" rows={4} />
      <TextArea label="Klauzula informacyjna" name="cvClause" id="cvClause" rows={4} />
      <Button type="submit" disabled={updateUser.isPending} className="block ml-auto">
        Zapisz
      </Button>
    </MainForm>
  );
}

export default Other;
