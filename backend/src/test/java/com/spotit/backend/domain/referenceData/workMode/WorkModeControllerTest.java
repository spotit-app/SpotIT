package com.spotit.backend.domain.referenceData.workMode;

import static com.spotit.backend.abstraction.GeneralUtils.createAdminMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.domain.referenceData.workMode.WorkModeUtils.createWorkModeReadDto;
import static com.spotit.backend.domain.referenceData.workMode.WorkModeUtils.createWorkModeWriteDto;
import static com.spotit.backend.domain.referenceData.workMode.WorkModeUtils.generateWorkModeList;
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

@WebMvcTest(WorkModeController.class)
@Import(SecurityConfig.class)
class WorkModeControllerTest {

    static final String WORK_MODE_API_URL = "/api/workMode";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkModeService workModeService;

    @MockBean
    WorkModeMapper workModeMapper;

    @Test
    void shouldReturnListOfWorkModes() throws Exception {
        // given
        when(workModeService.getAll())
                .thenReturn(generateWorkModeList(3));
        when(workModeMapper.toReadDto(any()))
                .thenReturn(createWorkModeReadDto(1));

        // when
        var result = mockMvc.perform(get(WORK_MODE_API_URL).with(jwt()));

        // then
        verify(workModeService, times(1)).getAll();
        verify(workModeMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedWorkMode() throws Exception {
        // given
        var workModeToCreate = createWorkModeWriteDto(1);
        var createdWorkMode = createWorkModeReadDto(1);

        when(workModeMapper.toReadDto(any()))
                .thenReturn(createdWorkMode);

        // when
        var result = mockMvc.perform(post(WORK_MODE_API_URL)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workModeToCreate)));

        // then
        verify(workModeMapper, times(1)).toReadDto(any());
        verify(workModeService, times(1)).create(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdWorkMode.id()))
                .andExpect(jsonPath("$.name").value(createdWorkMode.name()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateorkMode() throws Exception {
        // given
        var workModeToCreate = createWorkModeWriteDto(1);
        var creatingWorkModeError = new ErrorCreatingEntityException();

        when(workModeService.create(any()))
                .thenThrow(creatingWorkModeError);

        // when
        var result = mockMvc.perform(post(WORK_MODE_API_URL)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workModeToCreate)));

        // then
        verify(workModeService, times(1)).create(any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingWorkModeError.getMessage()));
    }

    @Test
    void shouldReturnWorkModeById() throws Exception {
        // given
        var workModeId = 1;
        var workMode = createWorkModeReadDto(workModeId);

        when(workModeMapper.toReadDto(any()))
                .thenReturn(workMode);

        // when
        var result = mockMvc.perform(get(WORK_MODE_API_URL + "/" + workModeId)
                .with(jwt()));

        // then
        verify(workModeService, times(1)).getById(workModeId);
        verify(workModeMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(workMode.id()))
                .andExpect(jsonPath("$.name").value(workMode.name()));
    }

    @Test
    void shouldReturnErrorWhenWorkModeNotFound() throws Exception {
        // given
        var workModeId = 1;
        var workModeNotFoundError = new EntityNotFoundException(workModeId);

        when(workModeService.getById(workModeId))
                .thenThrow(workModeNotFoundError);

        // when
        var result = mockMvc.perform(get(WORK_MODE_API_URL + "/" + workModeId)
                .with(jwt()));

        // then
        verify(workModeService, times(1)).getById(workModeId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(workModeNotFoundError.getMessage()));
    }

    @Test
    void shouldReturnModifiedWorkMode() throws Exception {
        // given
        var workModeId = 1;
        var workModeToModify = createWorkModeWriteDto(workModeId);
        var modifiedWorkMode = createWorkModeReadDto(workModeId);

        when(workModeMapper.toReadDto(any()))
                .thenReturn(modifiedWorkMode);

        // when
        var result = mockMvc.perform(put(WORK_MODE_API_URL + "/" + workModeId)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workModeToModify)));

        // then
        verify(workModeService, times(1)).update(any(), any());
        verify(workModeMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedWorkMode.id()))
                .andExpect(jsonPath("$.name").value(modifiedWorkMode.name()));
    }

    @Test
    void shouldReturnErrorWhenEditedUserNotFound() throws Exception {
        // given
        var workModeId = 1;
        var workModeToModify = createWorkModeWriteDto(workModeId);
        var workModeNotFoundError = new EntityNotFoundException(workModeId);

        when(workModeService.update(any(), any()))
                .thenThrow(workModeNotFoundError);

        // when
        var result = mockMvc.perform(put(WORK_MODE_API_URL + "/" + workModeId)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workModeToModify)));

        // then
        verify(workModeService, times(1)).update(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(workModeNotFoundError.getMessage()));
    }

    @Test
    void shouldDeletWworkMode() throws Exception {
        // given
        var workModeId = 1;

        // when
        var result = mockMvc.perform(delete(WORK_MODE_API_URL + "/" + workModeId)
                .with(createAdminMockJwt()));

        // then
        verify(workModeService, times(1)).delete(workModeId);

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(workModeId))));
    }

    @Test
    void shouldReturnErrorWhenDeletedWorkModeNotFound() throws Exception {
        // given
        var workModeId = 1;
        var workModeNotFoundError = new EntityNotFoundException(workModeId);

        doThrow(workModeNotFoundError).when(workModeService).delete(workModeId);

        // when
        var result = mockMvc.perform(delete(WORK_MODE_API_URL + "/" + workModeId)
                .with(createAdminMockJwt()));

        // then
        verify(workModeService, times(1)).delete(workModeId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(workModeNotFoundError.getMessage()));
    }
}
