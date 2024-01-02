import { Formik, Form, FormikHelpers, FormikValues } from 'formik';
import { PropsWithChildren } from 'react';
import PropTypes from 'prop-types';

interface MainFormProps<FormValues> extends PropsWithChildren {
  title: string;
  initialValues: FormValues;
  validationSchema: object;
  onSubmit: (values: FormValues, FormikHelpers: FormikHelpers<FormValues>) => void;
}

function MainForm<FormValues extends FormikValues>({
  title,
  initialValues,
  validationSchema,
  onSubmit,
  children
}: MainFormProps<FormValues>) {
  return (
    <Formik<FormValues>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
      enableReinitialize={true}
    >
      <Form className="w-full p-5" data-testid="form">
        <h1 className="text-center text-xl font-bold leading-7 my-3">{title}</h1>
        {children}
      </Form>
    </Formik>
  );
}

MainForm.propTypes = {
  title: PropTypes.string.isRequired,
  children: PropTypes.node.isRequired,
  initialValues: PropTypes.object.isRequired,
  validationSchema: PropTypes.object.isRequired,
  onSubmit: PropTypes.func.isRequired
};

export { MainForm };
