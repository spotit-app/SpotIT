package com.spotit.backend.domain.referenceData.workExperience;

import static com.spotit.backend.abstraction.GeneralUtils.createAdminMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.domain.referenceData.workExperience.WorkExperienceUtils.createWorkExperienceReadDto;
import static com.spotit.backend.domain.referenceData.workExperience.WorkExperienceUtils.createWorkExperienceWriteDto;
import static com.spotit.backend.domain.referenceData.workExperience.WorkExperienceUtils.generateWorkExperienceList;
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
import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.config.security.SecurityConfig;

@WebMvcTest(WorkExperienceController.class)
@Import(SecurityConfig.class)
class WorkExperienceControllerTest {

    static final String WORK_EXPERIENCE_API_URL = "/api/workExperience";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkExperienceService workExperienceService;

    @MockBean
    WorkExperienceMapper workExperienceMapper;

    @Test
    void shouldReturnListOfWorkExperiences() throws Exception {
        // given
        when(workExperienceService.getAll())
                .thenReturn(generateWorkExperienceList(3));
        when(workExperienceMapper.toReadDto(any()))
                .thenReturn(createWorkExperienceReadDto(1));

        // when
        var result = mockMvc.perform(get(WORK_EXPERIENCE_API_URL).with(jwt()));

        // then
        verify(workExperienceService, times(1)).getAll();
        verify(workExperienceMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedWorkExperience() throws Exception {
        // given
        var workExperienceToCreate = createWorkExperienceWriteDto(1);
        var createdWorkExperience = createWorkExperienceReadDto(1);

        when(workExperienceMapper.toReadDto(any()))
                .thenReturn(createdWorkExperience);

        // when
        var result = mockMvc.perform(post(WORK_EXPERIENCE_API_URL)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workExperienceToCreate)));

        // then
        verify(workExperienceMapper, times(1)).toReadDto(any());
        verify(workExperienceService, times(1)).create(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdWorkExperience.id()))
                .andExpect(jsonPath("$.name").value(createdWorkExperience.name()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateWorkExperience() throws Exception {
        // given
        var workExperienceToCreate = createWorkExperienceWriteDto(1);
        var creatingWorkExperienceError = new ErrorCreatingEntityException();

        when(workExperienceService.create(any()))
                .thenThrow(creatingWorkExperienceError);

        // when
        var result = mockMvc.perform(post(WORK_EXPERIENCE_API_URL)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workExperienceToCreate)));

        // then
        verify(workExperienceService, times(1)).create(any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingWorkExperienceError.getMessage()));
    }

    @Test
    void shouldReturnWorkExperienceById() throws Exception {
        // given
        var workExperienceId = 1;
        var workExperience = createWorkExperienceReadDto(workExperienceId);

        when(workExperienceMapper.toReadDto(any()))
                .thenReturn(workExperience);

        // when
        var result = mockMvc.perform(get(WORK_EXPERIENCE_API_URL + "/" + workExperienceId)
                .with(jwt()));

        // then
        verify(workExperienceService, times(1)).getById(workExperienceId);
        verify(workExperienceMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(workExperience.id()))
                .andExpect(jsonPath("$.name").value(workExperience.name()));
    }

    @Test
    void shouldReturnErrorWhenWorkExperienceNotFound() throws Exception {
        // given
        var workExperienceId = 1;
        var workExperienceNotFoundError = new EntityNotFoundException(workExperienceId);

        when(workExperienceService.getById(workExperienceId))
                .thenThrow(workExperienceNotFoundError);

        // when
        var result = mockMvc.perform(get(WORK_EXPERIENCE_API_URL + "/" + workExperienceId)
                .with(jwt()));

        // then
        verify(workExperienceService, times(1)).getById(workExperienceId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(workExperienceNotFoundError.getMessage()));
    }

    @Test
    void shouldReturnModifiedWorkExperience() throws Exception {
        // given
        var workExperienceId = 1;
        var workExperienceToModify = createWorkExperienceWriteDto(workExperienceId);
        var modifiedWorkExperience = createWorkExperienceReadDto(workExperienceId);

        when(workExperienceMapper.toReadDto(any()))
                .thenReturn(modifiedWorkExperience);

        // when
        var result = mockMvc.perform(put(WORK_EXPERIENCE_API_URL + "/" + workExperienceId)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workExperienceToModify)));

        // then
        verify(workExperienceService, times(1)).update(any(), any());
        verify(workExperienceMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedWorkExperience.id()))
                .andExpect(jsonPath("$.name").value(modifiedWorkExperience.name()));
    }

    @Test
    void shouldReturnErrorWhenEditedUserNotFound() throws Exception {
        // given
        var workExperienceId = 1;
        var workExperienceToModify = createWorkExperienceWriteDto(workExperienceId);
        var workExperienceNotFoundError = new EntityNotFoundException(workExperienceId);

        when(workExperienceService.update(any(), any()))
                .thenThrow(workExperienceNotFoundError);

        // when
        var result = mockMvc.perform(put(WORK_EXPERIENCE_API_URL + "/" + workExperienceId)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workExperienceToModify)));

        // then
        verify(workExperienceService, times(1)).update(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(workExperienceNotFoundError.getMessage()));
    }

    @Test
    void shouldDeleteWorkExperience() throws Exception {
        // given
        var workExperienceId = 1;

        // when
        var result = mockMvc.perform(delete(WORK_EXPERIENCE_API_URL + "/" + workExperienceId)
                .with(createAdminMockJwt()));

        // then
        verify(workExperienceService, times(1)).delete(workExperienceId);

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(workExperienceId))));
    }

    @Test
    void shouldReturnErrorWhenDeletedWorkExperienceNotFound() throws Exception {
        // given
        var workExperienceId = 1;
        var workExperienceNotFoundError = new EntityNotFoundException(workExperienceId);

        doThrow(workExperienceNotFoundError).when(workExperienceService).delete(workExperienceId);

        // when
        var result = mockMvc.perform(delete(WORK_EXPERIENCE_API_URL + "/" + workExperienceId)
                .with(createAdminMockJwt()));

        // then
        verify(workExperienceService, times(1)).delete(workExperienceId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(workExperienceNotFoundError.getMessage()));
    }
}
