package com.spotit.backend.domain.employee.employeeDetails.education;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailController;
import com.spotit.backend.domain.referenceData.educationLevel.EducationLevel;
import com.spotit.backend.domain.referenceData.educationLevel.EducationLevelService;

@CrossOrigin
@RestController
@RequestMapping("/api/userAccount/{auth0Id}/education")
public class EducationController extends
        AbstractUserDetailController<Education, Integer, EducationReadDto, EducationWriteDto> {

    private final EducationLevelService educationLevelService;

    public EducationController(
            EducationService educationService,
            EducationLevelService educationLevelService,
            EducationMapper educationMapper) {
        super(educationService, educationMapper);
        this.educationLevelService = educationLevelService;
    }

    @Override
    @PostMapping
    public EducationReadDto createEntityOfUser(
            @PathVariable String auth0Id,
            @RequestBody EducationWriteDto writeDto) {

        Optional<EducationLevel> educationLevel = educationLevelService
                .getByName(writeDto.educationLevel());

        EducationLevel foundEducationLevel = educationLevel
                .orElseGet(() -> EducationLevel
                        .builder()
                        .name(writeDto.educationLevel())
                        .custom(true)
                        .build());

        Education educationToCreate = Education.builder()
                .educationLevel(foundEducationLevel)
                .schoolName(writeDto.schoolName())
                .faculty(writeDto.faculty())
                .startDate(writeDto.startDate())
                .endDate(writeDto.endDate())
                .build();

        return mapper.toReadDto(
                service.create(auth0Id, educationToCreate));
    }
}
