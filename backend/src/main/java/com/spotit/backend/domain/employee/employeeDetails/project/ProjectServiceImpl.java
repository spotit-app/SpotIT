package com.spotit.backend.domain.employee.employeeDetails.project;

import org.springframework.stereotype.Service;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailServiceImpl;
import com.spotit.backend.domain.userAccount.UserAccountService;

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
