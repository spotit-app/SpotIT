package com.spotit.backend.domain.employee.employeeDetails.experience;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailController;

@CrossOrigin
@RestController
@RequestMapping("/api/userAccount/{auth0Id}/experience")
public class ExperienceController
        extends AbstractUserDetailController<Experience, Integer, ExperienceReadDto, ExperienceWriteDto> {

    public ExperienceController(ExperienceService experienceService, ExperienceMapper experienceMapper) {
        super(experienceService, experienceMapper);
    }
}
