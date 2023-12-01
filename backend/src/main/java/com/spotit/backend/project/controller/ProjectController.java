package com.spotit.backend.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.abstraction.controller.AbstractUserDetailController;
import com.spotit.backend.project.dto.ReadProjectDto;
import com.spotit.backend.project.dto.WriteProjectDto;
import com.spotit.backend.project.mapper.ProjectMapper;
import com.spotit.backend.project.model.Project;
import com.spotit.backend.project.service.ProjectService;

@RestController
@RequestMapping("/api/userAccount/{auth0Id}/project")
public class ProjectController
        extends AbstractUserDetailController<Project, Integer, ReadProjectDto, WriteProjectDto> {

    public ProjectController(ProjectService projectService, ProjectMapper projectMapper) {
        super(projectService, projectMapper);
    }
}
