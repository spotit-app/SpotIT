package com.spotit.backend.domain.employee.employeeDetails.social;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailController;

@CrossOrigin
@RestController
@RequestMapping("/api/userAccount/{auth0Id}/social")
public class SocialController
        extends AbstractUserDetailController<Social, Integer, SocialReadDto, SocialWriteDto> {

    public SocialController(SocialService socialService, SocialMapper socialMapper) {
        super(socialService, socialMapper);
    }
}
