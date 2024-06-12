import { toast } from 'react-toastify';

export const errorToast = (message = 'Błąd: Operacja nie powiodła się!') =>
  toast.error(message, {
    autoClose: 3000,
    bodyClassName: 'text-error'
  });

export const successToast = (message = 'Sukces: Operacja zakończona pomyślnie!') =>
  toast.success(message, {
    autoClose: 3000,
    bodyClassName: 'text-success'
  });
