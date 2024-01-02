import icons from 'assets/icons';

function NoContent() {
  return (
    <div
      data-testid="no-content"
      className="center flex flex-col justify-center items-center text-primary"
    >
      <icons.VscEmptyWindow size="50" />
      <h2 className="font-bold">Brak elementów</h2>
    </div>
  );
}

export { NoContent };
