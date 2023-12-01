package com.spotit.backend.techSkill.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.abstraction.controller.AbstractUserDetailController;
import com.spotit.backend.techSkill.dto.ReadTechSkillDto;
import com.spotit.backend.techSkill.dto.WriteTechSkillDto;
import com.spotit.backend.techSkill.mapper.TechSkillMapper;
import com.spotit.backend.techSkill.model.TechSkill;
import com.spotit.backend.techSkill.model.TechSkillName;
import com.spotit.backend.techSkill.service.TechSkillNameService;
import com.spotit.backend.techSkill.service.TechSkillService;

@RestController
@RequestMapping("/api/userAccount/{auth0Id}/techSkill")
public class TechSkillController
        extends AbstractUserDetailController<TechSkill, Integer, ReadTechSkillDto, WriteTechSkillDto> {

    private final TechSkillNameService techSkillNameService;

    public TechSkillController(
            TechSkillService techSkillService,
            TechSkillNameService techSkillNameService,
            TechSkillMapper techSkillMapper) {
        super(techSkillService, techSkillMapper);
        this.techSkillNameService = techSkillNameService;
    }

    @Override
    @PostMapping
    public ReadTechSkillDto createEntityOfUser(
            @PathVariable String auth0Id,
            @RequestBody WriteTechSkillDto writeDto) {

        Optional<TechSkillName> techSkillName = techSkillNameService
                .getByName(writeDto.techSkillName());

        TechSkillName foundTechSkillName = techSkillName
                .orElseGet(() -> TechSkillName
                        .builder()
                        .name(writeDto.techSkillName())
                        .custom(true)
                        .build());

        TechSkill techSkillToCreate = TechSkill.builder()
                .techSkillName(foundTechSkillName)
                .skillLevel(writeDto.skillLevel())
                .build();

        return mapper.toReadDto(service.create(auth0Id, techSkillToCreate));
    }
}
