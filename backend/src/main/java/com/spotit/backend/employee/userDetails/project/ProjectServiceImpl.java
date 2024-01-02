package com.spotit.backend.employee.userDetails.project;

import org.springframework.stereotype.Service;

import com.spotit.backend.employee.userAccount.UserAccountService;
import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailServiceImpl;

@Service
public class ProjectServiceImpl
        extends AbstractUserDetailServiceImpl<Project, Integer>
        implements ProjectService {

    public ProjectServiceImpl(
            ProjectRepository projectRepository,
            UserAccountService userAccountService) {
        super(projectRepository, userAccountService);
    }
}
