import { MODAL } from 'appConstants';

const closeModal = () => {
  const modal = document.getElementById(MODAL) as HTMLDialogElement;
  modal.close();
};

const showModal = () => {
  const modal = document.getElementById(MODAL) as HTMLDialogElement;
  modal.showModal();
};

export { showModal, closeModal };
