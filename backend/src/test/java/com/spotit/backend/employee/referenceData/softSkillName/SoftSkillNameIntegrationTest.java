package com.spotit.backend.employee.referenceData.softSkillName;

import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.employee.referenceData.softSkillName.SoftSkillNameUtils.createSoftSkillName;
import static com.spotit.backend.employee.referenceData.softSkillName.SoftSkillNameUtils.createSoftSkillNameWriteDto;
import static com.spotit.backend.employee.referenceData.softSkillName.SoftSkillNameUtils.generateSoftSkillNameList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.IntegrationTest;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class SoftSkillNameIntegrationTest extends IntegrationTest {

    static final String SOFT_SKILL_NAME_API_URL = "/api/softSkillName";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SoftSkillNameRepository softSkillNameRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturnAllSoftSkillNames() throws Exception {
        // given
        var createdSoftSkillNames = generateSoftSkillNameList(3);
        softSkillNameRepository.saveAll(createdSoftSkillNames);

        // when
        var result = mockMvc.perform(get(SOFT_SKILL_NAME_API_URL).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(createdSoftSkillNames.size()));
    }

    @Test
    void shouldReturnSoftSkillNameById() throws Exception {
        // given
        var createdSoftSkillNames = generateSoftSkillNameList(3);
        softSkillNameRepository.saveAll(createdSoftSkillNames);

        // when
        var result = mockMvc.perform(get(SOFT_SKILL_NAME_API_URL + "/" + createdSoftSkillNames.get(1).getId())
                .with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdSoftSkillNames.get(1).getId()));
    }

    @Test
    void shouldReturnErrorWhenSoftSkillNameNotFound() throws Exception {
        // given
        var softSkillNameId = 1;

        // when
        var result = mockMvc.perform(get(SOFT_SKILL_NAME_API_URL + "/" + softSkillNameId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(softSkillNameId)));
    }

    @Test
    void shouldReturnCreatedSoftSkillName() throws Exception {
        // given
        var softSkillNameToCreate = createSoftSkillNameWriteDto(1);

        // when
        var result = mockMvc.perform(post(SOFT_SKILL_NAME_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillNameToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(softSkillNameToCreate.name()));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingSoftSkillName() throws Exception {
        // given
        var softSkillNameToCreate = SoftSkillNameWriteDto.builder().build();

        // when
        var result = mockMvc.perform(post(SOFT_SKILL_NAME_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillNameToCreate)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedSoftSkillName() throws Exception {
        // given
        var existingSoftSkillName = createSoftSkillName(1);
        var modifiedSoftSkillName = SoftSkillNameWriteDto.builder()
                .name("Name 2")
                .build();
        softSkillNameRepository.save(existingSoftSkillName);

        // when
        var result = mockMvc.perform(put(SOFT_SKILL_NAME_API_URL + "/" + existingSoftSkillName.getId())
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedSoftSkillName)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(modifiedSoftSkillName.name()));
    }

    @Test
    void shouldReturnErrorWhenEditedSoftSkillNameNotFound() throws Exception {
        // given
        var softSkillNameId = 1;
        var softSkillNameToModify = createSoftSkillNameWriteDto(1);

        // when
        var result = mockMvc.perform(put(SOFT_SKILL_NAME_API_URL + "/" + softSkillNameId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillNameToModify)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(softSkillNameId)));
    }

    @Test
    void shouldDeleteSoftSkillName() throws Exception {
        // given
        var softSkillNameToDelete = createSoftSkillName(1);
        softSkillNameRepository.save(softSkillNameToDelete);

        // when
        var result = mockMvc.perform(delete(SOFT_SKILL_NAME_API_URL + "/" + softSkillNameToDelete.getId()).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(softSkillNameToDelete.getId()))));

        assertFalse(softSkillNameRepository.existsById(softSkillNameToDelete.getId()));
    }

    @Test
    void shouldReturnErrorWhenDeletedSoftSkillNameNotFound() throws Exception {
        // given
        var softSkillNameId = 1;

        // when
        var result = mockMvc.perform(delete(SOFT_SKILL_NAME_API_URL + "/" + softSkillNameId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(softSkillNameId)));
    }
}
