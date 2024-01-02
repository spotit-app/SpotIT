import { useState } from 'react';
import { Button, Loading, NoContent, PopUp, ProfileContainer, ProjectCard } from 'components';
import { useProjects } from 'hooks/useProjects';
import { ProjectsForm } from './ProjectsForm';
import { ReadProject } from 'types/profile';
import { showModal } from 'utils';

function Projects() {
  const [projectToEdit, setProjectToEdit] = useState<ReadProject | undefined>(undefined);

  const editProject = (project: ReadProject) => {
    setProjectToEdit(project);
    showModal();
  };

  const { projects, projectsIsPending, deleteProject } = useProjects();
  const projectElements = projects?.map((project: ReadProject) => (
    <ProjectCard key={project.id} {...project}>
      <div className="mr-2">
        <Button onClick={() => editProject(project)}>Edytuj</Button>
      </div>

      <Button onClick={() => deleteProject.mutate(project.id)}>Usuń</Button>
    </ProjectCard>
  ));

  const content =
    projectsIsPending || deleteProject.isPending ? (
      <Loading />
    ) : projectElements?.length !== 0 ? (
      projectElements
    ) : (
      <NoContent />
    );

  return (
    <ProfileContainer
      title="Projekty"
      addText="Dodaj projekt"
      onAdd={() => setProjectToEdit(undefined)}
    >
      {content}

      <PopUp title="Dodaj projekt">
        <ProjectsForm projectToEdit={projectToEdit} />
      </PopUp>
    </ProfileContainer>
  );
}

export default Projects;
