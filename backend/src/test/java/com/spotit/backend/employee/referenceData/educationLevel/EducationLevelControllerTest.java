package com.spotit.backend.employee.referenceData.educationLevel;

import static com.spotit.backend.employee.referenceData.educationLevel.EducationLevelUtils.createEducationLevelReadDto;
import static com.spotit.backend.employee.referenceData.educationLevel.EducationLevelUtils.createEducationLevelWriteDto;
import static com.spotit.backend.employee.referenceData.educationLevel.EducationLevelUtils.generateEducationLevelList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.config.SecurityConfig;
import com.spotit.backend.employee.abstraction.EntityNotFoundException;
import com.spotit.backend.employee.abstraction.ErrorCreatingEntityException;

@WebMvcTest(EducationLevelController.class)
@Import(SecurityConfig.class)
class EducationLevelControllerTest {

    private static final String EDUCATION_LEVEL_API_URL = "/api/educationLevel";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EducationLevelService educationLevelService;

    @MockBean
    EducationLevelMapper educationLevelMapper;

    @Test
    void shouldReturnListOfEducationLevels() throws Exception {
        // given
        when(educationLevelService.getAll())
                .thenReturn(generateEducationLevelList(3));
        when(educationLevelMapper.toReadDto(any()))
                .thenReturn(createEducationLevelReadDto(1));

        // when
        var result = mockMvc.perform(get(EDUCATION_LEVEL_API_URL).with(jwt()));

        // then
        verify(educationLevelService, times(1)).getAll();
        verify(educationLevelMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedEducationLevel() throws Exception {
        // given
        var educationLevelToCreate = createEducationLevelWriteDto(1);
        var createdEducationLevel = createEducationLevelReadDto(1);

        when(educationLevelMapper.toReadDto(any()))
                .thenReturn(createdEducationLevel);

        // when
        var result = mockMvc.perform(post(EDUCATION_LEVEL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationLevelToCreate)));

        // then
        verify(educationLevelMapper, times(1)).toReadDto(any());
        verify(educationLevelService, times(1)).create(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdEducationLevel.id()))
                .andExpect(jsonPath("$.name").value(createdEducationLevel.name()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateEducationLevel() throws Exception {
        // given
        var educationLevelToCreate = createEducationLevelWriteDto(1);
        var creatingEducationLevelError = new ErrorCreatingEntityException();

        when(educationLevelService.create(any()))
                .thenThrow(creatingEducationLevelError);

        // when
        var result = mockMvc.perform(post(EDUCATION_LEVEL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationLevelToCreate)));

        // then
        verify(educationLevelService, times(1)).create(any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingEducationLevelError.getMessage()));
    }

    @Test
    void shouldReturnEducationLevelById() throws Exception {
        // given
        var educationLevelId = 1;
        var educationLevel = createEducationLevelReadDto(educationLevelId);

        when(educationLevelMapper.toReadDto(any()))
                .thenReturn(educationLevel);

        // when
        var result = mockMvc.perform(get(EDUCATION_LEVEL_API_URL + "/" + educationLevelId)
                .with(jwt()));

        // then
        verify(educationLevelService, times(1)).getById(educationLevelId);
        verify(educationLevelMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(educationLevel.id()))
                .andExpect(jsonPath("$.name").value(educationLevel.name()));
    }

    @Test
    void shouldReturnErrorWhenEducationLevelNotFound() throws Exception {
        // given
        var educationLevelId = 1;
        var educationLevelNotFoundError = new EntityNotFoundException(educationLevelId);

        when(educationLevelService.getById(educationLevelId))
                .thenThrow(educationLevelNotFoundError);

        // when
        var result = mockMvc.perform(get(EDUCATION_LEVEL_API_URL + "/" + educationLevelId)
                .with(jwt()));

        // then
        verify(educationLevelService, times(1)).getById(educationLevelId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(educationLevelNotFoundError.getMessage()));
    }

    @Test
    void shouldReturnModifiedEducationLevel() throws Exception {
        // given
        var educationLevelId = 1;
        var educationLevelToModify = createEducationLevelWriteDto(educationLevelId);
        var modifiedEducationLevel = createEducationLevelReadDto(educationLevelId);

        when(educationLevelMapper.toReadDto(any()))
                .thenReturn(modifiedEducationLevel);

        // when
        var result = mockMvc.perform(put(EDUCATION_LEVEL_API_URL + "/" + educationLevelId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationLevelToModify)));

        // then
        verify(educationLevelService, times(1)).update(any(), any());
        verify(educationLevelMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedEducationLevel.id()))
                .andExpect(jsonPath("$.name").value(modifiedEducationLevel.name()));
    }

    @Test
    void shouldReturnErrorWhenEditedUserNotFound() throws Exception {
        // given
        var educationLevelId = 1;
        var educationLevelToModify = createEducationLevelWriteDto(educationLevelId);
        var educationLevelNotFoundError = new EntityNotFoundException(educationLevelId);

        when(educationLevelService.update(any(), any()))
                .thenThrow(educationLevelNotFoundError);

        // when
        var result = mockMvc.perform(put(EDUCATION_LEVEL_API_URL + "/" + educationLevelId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationLevelToModify)));

        // then
        verify(educationLevelService, times(1)).update(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(educationLevelNotFoundError.getMessage()));
    }

    @Test
    void shouldDeleteEducationLevel() throws Exception {
        // given
        var educationLevelId = 1;

        // when
        var result = mockMvc.perform(delete(EDUCATION_LEVEL_API_URL + "/" + educationLevelId)
                .with(jwt()));

        // then
        verify(educationLevelService, times(1)).delete(educationLevelId);

        result.andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(educationLevelId)));
    }

    @Test
    void shouldReturnErrorWhenDeletedEducationLevelNotFound() throws Exception {
        // given
        var educationLevelId = 1;
        var educationLevelNotFoundError = new EntityNotFoundException(educationLevelId);

        doThrow(educationLevelNotFoundError).when(educationLevelService).delete(educationLevelId);

        // when
        var result = mockMvc.perform(delete(EDUCATION_LEVEL_API_URL + "/" + educationLevelId)
                .with(jwt()));

        // then
        verify(educationLevelService, times(1)).delete(educationLevelId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(educationLevelNotFoundError.getMessage()));
    }
}
