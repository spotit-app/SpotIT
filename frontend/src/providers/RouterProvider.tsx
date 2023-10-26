import PropTypes from 'prop-types';
import { PropsWithChildren } from 'react';
import { BrowserRouter } from 'react-router-dom';

function RouterProvider({ children }: PropsWithChildren) {
  return <BrowserRouter>{children}</BrowserRouter>;
}
export { RouterProvider };

RouterProvider.propTypes = {
  children: PropTypes.node.isRequired
};
