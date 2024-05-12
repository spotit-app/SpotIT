import { Button } from 'components';
import { useNavigate } from 'react-router-dom';

function AdminWelcomePage() {
  const navigate = useNavigate();

  return (
    <div className="w-full flex flex-col items-center">
      <h1 className="text-2xl lg:text-5xl font-semibold text-center my-16">
        Witaj w panelu administracyjnym!
      </h1>
      <div className="flex flex-wrap px-2 lg:px-5">
        <div className="h-auto lg:h-60 flex flex-col justify-between border-2 rounded-lg border-primary p-5">
          <h2 className="text-xl lg:text-3xl font-bold">Dane portalu</h2>
          <p className="text-lg font-normal my-2">
            Edytuj dane, dostępne technologie, tryby pracy, wymagane języki i wiele innych.
          </p>
          <div className="text-primary text-lg uppercase">
            <Button type="button" onClick={() => navigate('/admin/edukacja')}>
              Edytuj dane
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AdminWelcomePage;
