import PropTypes from 'prop-types';

function Badge({ name }: { name: string }) {
  return (
    <div className="badge badge-primary mr-2 text-lg px-4 py-3 font-semibold my-1">{name}</div>
  );
}

export { Badge };

Badge.propTypes = {
  name: PropTypes.string.isRequired
};
