import { Formik, Form, FormikHelpers, FormikValues } from 'formik';
import { PropsWithChildren } from 'react';
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
      enableReinitialize={true}
    >
      <Form>
        <div className="flex flex-col text-base justify-center mt-3">{children}</div>
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
