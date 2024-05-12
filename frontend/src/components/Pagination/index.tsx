import PropTypes from 'prop-types';

interface PaginationProps {
  page: number;
  isFirst: boolean;
  isLast: boolean;
  changePage: (num: number) => void;
}

function Pagination({ page, isFirst, isLast, changePage }: PaginationProps) {
  return (
    <div className="join mt-10 justify-center">
      <button className="join-item btn" disabled={isFirst} onClick={() => changePage(-1)}>
        «
      </button>
      <button className="join-item btn">Strona {page}</button>
      <button className="join-item btn" disabled={isLast} onClick={() => changePage(1)}>
        »
      </button>
    </div>
  );
}

Pagination.propTypes = {
  page: PropTypes.number.isRequired,
  isLast: PropTypes.bool.isRequired,
  isFirst: PropTypes.bool.isRequired,
  changePage: PropTypes.func.isRequired
};

export { Pagination };
