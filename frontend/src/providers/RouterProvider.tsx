import { BrowserRouter } from 'react-router-dom';
import { PropsWithChildren } from 'react';
import PropTypes from 'prop-types';

function RouterProvider({ children }: PropsWithChildren) {
  return <BrowserRouter>{children}</BrowserRouter>;
}
export { RouterProvider };

RouterProvider.propTypes = {
  children: PropTypes.node.isRequired
};
