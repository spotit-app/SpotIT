import { Portfolio } from 'types/profile';
import { useUser, usePortfolio } from 'hooks';
import { useNavigate } from 'react-router-dom';
import { Button, Loading } from 'components';

function WelcomePage() {
  const { userName } = useUser();
  const { portfolioUrl, createPortfolio, updatePortfolio, portfolioUrlIsPending } = usePortfolio();
  const navigate = useNavigate();

  const createOrUpdatePortfolio = async () => {
    if (portfolioUrl) {
      await updatePortfolio.mutateAsync();
      navigate(`/portfolio/${portfolioUrl}`);
    } else {
      const createdPortfolio: Portfolio = await createPortfolio.mutateAsync();
      navigate(`/portfolio/${createdPortfolio.portfolioUrl}`);
    }
  };

  return portfolioUrlIsPending ? (
    <Loading />
  ) : (
    <div className="w-full flex flex-col items-center">
      <h1 className="text-2xl lg:text-5xl font-semibold text-center my-16">
        Witaj, {userName.split(' ')[0]}!
      </h1>
      <div className="flex flex-wrap px-2 lg:px-5">
        <div className="w-full lg:w-1/2 p-3">
          <div className="h-auto lg:h-72 flex flex-col justify-between border-2 rounded-lg border-primary p-5">
            <h2 className="text-xl lg:text-3xl font-bold">Informacje profilowe</h2>
            <p className="text-lg font-normal my-2">
              Edytuj swoje dane osobowe, dołączaj projekty, kursy, umiejętności i wiele innych.
            </p>
            <div className="text-primary text-lg uppercase">
              <Button type="button" onClick={() => navigate('/profil/dane-osobowe')}>
                Edytuj informacje profilowe
              </Button>
            </div>
          </div>
        </div>
        <div className="w-full lg:w-1/2  p-3">
          <div className="h-auto lg:h-72 flex flex-col justify-between border-2 rounded-lg  border-primary p-5">
            <h2 className="text-xl lg:text-3xl font-bold">Porfolio</h2>
            <p className="text-lg font-normal my-2">
              Wygeneruj portfolio zawierające wszystkie wypełnione przez ciebie dane, którym możesz
              podzielić się z całym światem.
            </p>
            <div className="flex flex-wrap text-primary text-lg uppercase">
              {portfolioUrl && (
                <Button
                  type="button"
                  className="mr-2"
                  onClick={() => navigate(`/portfolio/${portfolioUrl}`)}
                >
                  Podgląd portfolio
                </Button>
              )}
              <Button
                type="button"
                disabled={createPortfolio.isPending || updatePortfolio.isPending}
                onClick={() => createOrUpdatePortfolio()}
              >
                {portfolioUrl ? 'Zaktualizuj portfolio' : 'Wygeneruj portfolio'}
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default WelcomePage;
