import { PropsWithChildren } from 'react';
import { Formik, Form, FormikHelpers, FormikValues } from 'formik';
import PropTypes from 'prop-types';

interface PopUpFormProps<FormValues> extends PropsWithChildren {
  initialValues: FormValues;
  validationSchema: object;
  onSubmit: (values: FormValues, FormikHelpers: FormikHelpers<FormValues>) => void;
}

function PopUpForm<FormValues extends FormikValues>({
  initialValues,
  validationSchema,
  onSubmit,
  children
}: PopUpFormProps<FormValues>) {
  return (
    <Formik<FormValues>
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
    >
      <Form>
        <div className="flex text-base justify-center mt-3 space-y-12">
          <div className="w-full border-b border-gray-900/10 pb-8">
            <div className="mt-5 grid grid-cols-1 gap-x-6 gap-y-4 sm:grid-cols-5">{children}</div>
          </div>
        </div>
      </Form>
    </Formik>
  );
}

PopUpForm.propTypes = {
  children: PropTypes.node.isRequired,
  initialValues: PropTypes.object.isRequired,
  validationSchema: PropTypes.object.isRequired,
  onSubmit: PropTypes.func.isRequired
};

export { PopUpForm };
