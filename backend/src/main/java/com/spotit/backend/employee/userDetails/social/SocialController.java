package com.spotit.backend.employee.userDetails.social;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailController;

@RestController
@RequestMapping("/api/userAccount/{auth0Id}/social")
public class SocialController
        extends AbstractUserDetailController<Social, Integer, SocialReadDto, SocialWriteDto> {

    public SocialController(SocialService socialService, SocialMapper socialMapper) {
        super(socialService, socialMapper);
    }
}
