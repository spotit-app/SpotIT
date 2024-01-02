import PropTypes from 'prop-types';
import { ReactNode } from 'react';

interface ProjectCardProps {
  children: ReactNode;
  name: string;
  description: string;
  projectUrl: string;
}

function ProjectCard({ children, name, description, projectUrl }: ProjectCardProps) {
  return (
    <div className="flex flex-col justify-between border-b-2 border-l-2 border-primary my-4 p-3">
      <div className="flex flex-col w-full">
        <div className="text-2xl text-center text-primary font-bold">{name}</div>
        <label className="mt-3 text-primary font-bold text-lg">Opis</label>
        <div className="flex my-2">{description}</div>
        <label className="mt-3 text-primary font-bold text-lg">Link do projektu</label>
        <a href={projectUrl} target="_blank" rel="noreferrer" className="font-bold flex break-all">
          {projectUrl}
        </a>
      </div>
      <div className="flex justify-end">{children}</div>
    </div>
  );
}

export { ProjectCard };

ProjectCard.propTypes = {
  children: PropTypes.node.isRequired,
  name: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  projectUrl: PropTypes.string.isRequired
};
