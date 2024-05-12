package com.spotit.backend.domain.employee.employeeDetails.softSkill;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailController;
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillName;
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameService;

@CrossOrigin
@RestController
@RequestMapping("/api/userAccount/{auth0Id}/softSkill")
public class SoftSkillController
        extends AbstractUserDetailController<SoftSkill, Integer, SoftSkillReadDto, SoftSkillWriteDto> {

    private final SoftSkillNameService softSkillNameService;

    public SoftSkillController(
            SoftSkillService softSkillService,
            SoftSkillNameService softSkillNameService,
            SoftSkillMapper softSkillMapper) {
        super(softSkillService, softSkillMapper);
        this.softSkillNameService = softSkillNameService;
    }

    @Override
    @PostMapping
    public SoftSkillReadDto createEntityOfUser(
            @PathVariable String auth0Id,
            @RequestBody SoftSkillWriteDto writeDto) {

        Optional<SoftSkillName> softSkillName = softSkillNameService
                .getByName(writeDto.softSkillName());

        SoftSkillName foundSoftSkillName = softSkillName
                .orElseGet(() -> SoftSkillName
                        .builder()
                        .name(writeDto.softSkillName())
                        .custom(true)
                        .build());

        SoftSkill softSkillToCreate = SoftSkill.builder()
                .softSkillName(foundSoftSkillName)
                .skillLevel(writeDto.skillLevel())
                .build();

        return mapper.toReadDto(service.create(auth0Id, softSkillToCreate));
    }
}
