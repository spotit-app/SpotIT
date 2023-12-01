package com.spotit.backend.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.service.AbstractServiceImpl;
import com.spotit.backend.project.model.Project;
import com.spotit.backend.project.repository.ProjectRepository;
import com.spotit.backend.userAccount.model.UserAccount;
import com.spotit.backend.userAccount.service.UserAccountService;

@Service
public class ProjectServiceImpl
        extends AbstractServiceImpl<Project, Integer>
        implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserAccountService userAccountService;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserAccountService userAccountService) {
        super(projectRepository);
        this.projectRepository = projectRepository;
        this.userAccountService = userAccountService;
    }

    @Override
    public List<Project> getAllByUserAccountAuth0Id(String userAccountAuth0Id) {
        return projectRepository.findByUserAccountAuth0Id(userAccountAuth0Id);
    }

    @Override
    public Project create(String userAccountAuth0Id, Project project) {
        UserAccount userAccount = userAccountService.getByAuth0Id(userAccountAuth0Id);
        project.setUserAccount(userAccount);

        return create(project);
    }
}
