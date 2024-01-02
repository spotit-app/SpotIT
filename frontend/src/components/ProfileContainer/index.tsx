import PropTypes from 'prop-types';
import { ReactNode } from 'react';
import { Button } from 'components';
import { showModal } from 'utils';
import icons from 'assets/icons';

interface ProfileContainerProps {
  title: string;
  addText: string;
  children: ReactNode;
  onAdd?: () => void;
}

function ProfileContainer({ children, title, addText, onAdd }: ProfileContainerProps) {
  const handleAdd = () => {
    onAdd && onAdd();
    showModal();
  };

  return (
    <div className="flex flex-col w-full p-5 relative h-screen">
      <h1 className="text-center text-xl font-bold leading-7 my-3">{title}</h1>
      <div className="flex items-center mb-7">
        <h2 className="text-lg font-bold mr-2">{addText}</h2>
        <Button onClick={handleAdd} role="button" data-testid="add-button">
          <icons.IoAdd size="30" />
        </Button>
      </div>
      {children}
    </div>
  );
}

export { ProfileContainer };

ProfileContainer.propTypes = {
  title: PropTypes.string.isRequired,
  addText: PropTypes.string.isRequired,
  onAdd: PropTypes.func,
  children: PropTypes.node.isRequired
};
