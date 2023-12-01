import PropTypes from 'prop-types';
import { PropsWithChildren } from 'react';

interface PopUpProps extends PropsWithChildren {
  title: string;
}

function PopUp({ children, title }: PopUpProps) {
  return (
    <dialog id="modal" className="modal" data-testid="modal">
      <div className="modal-box">
        <form method="dialog">
          <h1 className="flex justify-center text-xl font-bold">{title}</h1>
          <button type="submit" className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">
            ✕
          </button>
        </form>
        {children}
      </div>
    </dialog>
  );
}

PopUp.propTypes = {
  children: PropTypes.node.isRequired,
  title: PropTypes.string.isRequired
};

export { PopUp };
