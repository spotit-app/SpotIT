import PropTypes from 'prop-types';
import { ReactNode } from 'react';

interface CourseCardProps {
  children?: ReactNode;
  name: string;
  finishDate: string;
}

function CourseCard({ children, name, finishDate }: CourseCardProps) {
  return (
    <div className="block md:flex justify-between border-b-2 border-l-2 border-primary my-4 p-3">
      <div className="block md:flex items-center w-full">
        <div className="text-xl w-1/3 md:mx-3 font-bold">{name}</div>
        <div className="text-primary flex font-bold">{finishDate}</div>
      </div>
      <div className="flex justify-end">{children}</div>
    </div>
  );
}

export { CourseCard };

CourseCard.propTypes = {
  children: PropTypes.node,
  name: PropTypes.string.isRequired,
  finishDate: PropTypes.string.isRequired
};
