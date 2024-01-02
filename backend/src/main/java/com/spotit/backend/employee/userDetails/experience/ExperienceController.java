package com.spotit.backend.employee.userDetails.experience;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailController;

@RestController
@RequestMapping("/api/userAccount/{auth0Id}/experience")
public class ExperienceController
        extends AbstractUserDetailController<Experience, Integer, ExperienceReadDto, ExperienceWriteDto> {

    public ExperienceController(ExperienceService experienceService, ExperienceMapper experienceMapper) {
        super(experienceService, experienceMapper);
    }
}
