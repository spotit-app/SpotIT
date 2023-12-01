import { PropsWithChildren } from 'react';
import { Formik, Form, FormikHelpers, FormikValues } from 'formik';
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
    >
      <Form className="w-9/12" data-testid="form">
        <div className="flex text-base justify-center mt-3 space-y-12">
          <div className="w-9/12 border-b border-gray-900/10 pb-12">
            <h1 className="flex justify-center text-lg font-bold leading-7">{title}</h1>
            <div className="mt-5 grid grid-cols-1 gap-x-6 gap-y-4 sm:grid-cols-6">{children}</div>
          </div>
        </div>
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
