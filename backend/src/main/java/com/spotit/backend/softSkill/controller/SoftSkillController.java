package com.spotit.backend.softSkill.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.abstraction.controller.AbstractUserDetailController;
import com.spotit.backend.softSkill.dto.ReadSoftSkillDto;
import com.spotit.backend.softSkill.dto.WriteSoftSkillDto;
import com.spotit.backend.softSkill.mapper.SoftSkillMapper;
import com.spotit.backend.softSkill.model.SoftSkill;
import com.spotit.backend.softSkill.model.SoftSkillName;
import com.spotit.backend.softSkill.service.SoftSkillNameService;
import com.spotit.backend.softSkill.service.SoftSkillService;

@RestController
@RequestMapping("/api/userAccount/{auth0Id}/softSkill")
public class SoftSkillController
        extends AbstractUserDetailController<SoftSkill, Integer, ReadSoftSkillDto, WriteSoftSkillDto> {

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
    public ReadSoftSkillDto createEntityOfUser(
            @PathVariable String auth0Id,
            @RequestBody WriteSoftSkillDto writeDto) {

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
