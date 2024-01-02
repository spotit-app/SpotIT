package com.spotit.backend.employee.userDetails.techSkill;

import java.util.Optional;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.employee.referenceData.techSkillName.TechSkillName;
import com.spotit.backend.employee.referenceData.techSkillName.TechSkillNameService;
import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailController;

@RestController
@RequestMapping("/api/userAccount/{auth0Id}/techSkill")
public class TechSkillController
        extends AbstractUserDetailController<TechSkill, Integer, TechSkillReadDto, TechSkillWriteDto> {

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
    public TechSkillReadDto createEntityOfUser(
            @PathVariable String auth0Id,
            @RequestBody TechSkillWriteDto writeDto) {

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
