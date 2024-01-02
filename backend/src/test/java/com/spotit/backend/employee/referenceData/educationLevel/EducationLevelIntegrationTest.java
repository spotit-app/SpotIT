package com.spotit.backend.employee.referenceData.educationLevel;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.employee.referenceData.educationLevel.EducationLevelUtils.createEducationLevel;
import static com.spotit.backend.employee.referenceData.educationLevel.EducationLevelUtils.createEducationLevelWriteDto;
import static com.spotit.backend.employee.referenceData.educationLevel.EducationLevelUtils.generateEducationLevelList;
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
class EducationLevelIntegrationTest extends IntegrationTest {

    static final String EDUCATION_LEVEL_API_URL = "/api/educationLevel";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EducationLevelRepository educationLevelRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturnAllEducationLevels() throws Exception {
        // given
        var createdEducationLevels = generateEducationLevelList(3);
        educationLevelRepository.saveAll(createdEducationLevels);

        // when
        var result = mockMvc.perform(get(EDUCATION_LEVEL_API_URL).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(createdEducationLevels.size()));
    }

    @Test
    void shouldReturnEducationLevelById() throws Exception {
        // given
        var createdEducationLevels = generateEducationLevelList(3);
        educationLevelRepository.saveAll(createdEducationLevels);

        // when
        var result = mockMvc.perform(get(EDUCATION_LEVEL_API_URL + "/" + createdEducationLevels.get(1).getId())
                .with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdEducationLevels.get(1).getId()));
    }

    @Test
    void shouldReturnErrorWhenEducationLevelNotFound() throws Exception {
        // given
        var educationLevelId = 1;

        // when
        var result = mockMvc.perform(get(EDUCATION_LEVEL_API_URL + "/" + educationLevelId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(educationLevelId)));
    }

    @Test
    void shouldReturnCreatedEducationLevel() throws Exception {
        // given
        var educationLevelToCreate = createEducationLevelWriteDto(1);

        // when
        var result = mockMvc.perform(post(EDUCATION_LEVEL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationLevelToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(educationLevelToCreate.name()));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingEducationLevel() throws Exception {
        // given
        var educationLevelToCreate = EducationLevelWriteDto.builder().build();

        // when
        var result = mockMvc.perform(post(EDUCATION_LEVEL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationLevelToCreate)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedEducationLevel() throws Exception {
        // given
        var existingEducationLevel = createEducationLevel(1);
        var modifiedEducationLevel = EducationLevelWriteDto.builder()
                .name("Name 2")
                .build();
        educationLevelRepository.save(existingEducationLevel);

        // when
        var result = mockMvc.perform(put(EDUCATION_LEVEL_API_URL + "/" + existingEducationLevel.getId())
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedEducationLevel)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(modifiedEducationLevel.name()));
    }

    @Test
    void shouldReturnErrorWhenEditedEducationLevelNotFound() throws Exception {
        // given
        var educationLevelId = 1;
        var educationLevelToModify = createEducationLevelWriteDto(1);

        // when
        var result = mockMvc.perform(put(EDUCATION_LEVEL_API_URL + "/" + educationLevelId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationLevelToModify)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(educationLevelId)));
    }

    @Test
    void shouldDeleteEducationLevel() throws Exception {
        // given
        var educationLevelToDelete = createEducationLevel(1);
        educationLevelRepository.save(educationLevelToDelete);

        // when
        var result = mockMvc.perform(
                delete(EDUCATION_LEVEL_API_URL + "/" + educationLevelToDelete.getId()).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().string(educationLevelToDelete.getId().toString()));

        assertFalse(educationLevelRepository.existsById(educationLevelToDelete.getId()));
    }

    @Test
    void shouldReturnErrorWhenDeletedEducationLevelNotFound() throws Exception {
        // given
        var educationLevelId = 1;

        // when
        var result = mockMvc.perform(delete(EDUCATION_LEVEL_API_URL + "/" + educationLevelId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(educationLevelId)));
    }
}
