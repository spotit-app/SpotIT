package com.spotit.backend.softSkill.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.abstraction.controller.AbstractController;
import com.spotit.backend.softSkill.dto.ReadSoftSkillNameDto;
import com.spotit.backend.softSkill.dto.WriteSoftSkillNameDto;
import com.spotit.backend.softSkill.mapper.SoftSkillNameMapper;
import com.spotit.backend.softSkill.model.SoftSkillName;
import com.spotit.backend.softSkill.service.SoftSkillNameService;

@RestController
@RequestMapping("/api/softSkillName")
public class SoftSkillNameController
        extends AbstractController<SoftSkillName, Integer, ReadSoftSkillNameDto, WriteSoftSkillNameDto> {

    private final SoftSkillNameService softSkillNameService;

    public SoftSkillNameController(
            SoftSkillNameService softSkillNameService,
            SoftSkillNameMapper softSkillNameMapper) {
        super(softSkillNameService, softSkillNameMapper);
        this.softSkillNameService = softSkillNameService;
    }

    @GetMapping
    public List<ReadSoftSkillNameDto> getAllSoftSkillNames() {
        return softSkillNameService.getAll()
                .stream()
                .map(mapper::toReadDto)
                .toList();
    }

    @PutMapping("/{id}")
    public ReadSoftSkillNameDto updateEntityById(
            @PathVariable Integer id,
            @RequestBody WriteSoftSkillNameDto writeDto) {
        SoftSkillName entityToUpdate = mapper.fromWriteDto(writeDto);
        SoftSkillName editedEntity = service.update(id, entityToUpdate);

        return mapper.toReadDto(editedEntity);
    }

    @PostMapping
    public ReadSoftSkillNameDto createSoftSkillName(@RequestBody WriteSoftSkillNameDto writeSoftSkillNameDto) {
        SoftSkillName softSkillName = SoftSkillName.builder()
                .name(writeSoftSkillNameDto.name())
                .custom(false)
                .build();

        return mapper.toReadDto(softSkillNameService.create(softSkillName));
    }
}
