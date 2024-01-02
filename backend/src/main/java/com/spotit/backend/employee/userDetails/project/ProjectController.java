package com.spotit.backend.employee.userDetails.project;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailController;

@RestController
@RequestMapping("/api/userAccount/{auth0Id}/project")
public class ProjectController
        extends AbstractUserDetailController<Project, Integer, ProjectReadDto, ProjectWriteDto> {

    public ProjectController(ProjectService projectService, ProjectMapper projectMapper) {
        super(projectService, projectMapper);
    }
}
