package com.spotit.backend.domain.referenceData.softSkillName;

import static com.spotit.backend.abstraction.GeneralUtils.createAdminMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameUtils.createSoftSkillNameReadDto;
import static com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameUtils.createSoftSkillNameWriteDto;
import static com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameUtils.generateSoftSkillNameList;
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

@WebMvcTest(SoftSkillNameController.class)
@Import(SecurityConfig.class)
class SoftSkillNameControllerTest {

    private static final String TECH_SKILL_NAME_API_URL = "/api/softSkillName";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SoftSkillNameService softSkillNameService;

    @MockBean
    SoftSkillNameMapper softSkillNameMapper;

    @Test
    void shouldReturnListOfSoftSkillNames() throws Exception {
        // given
        when(softSkillNameService.getAll())
                .thenReturn(generateSoftSkillNameList(3));
        when(softSkillNameMapper.toReadDto(any()))
                .thenReturn(createSoftSkillNameReadDto(1));

        // when
        var result = mockMvc.perform(get(TECH_SKILL_NAME_API_URL).with(jwt()));

        // then
        verify(softSkillNameService, times(1)).getAll();
        verify(softSkillNameMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedSoftSkillName() throws Exception {
        // given
        var softSkillNameToCreate = createSoftSkillNameWriteDto(1);
        var createdSoftSkillName = createSoftSkillNameReadDto(1);

        when(softSkillNameMapper.toReadDto(any()))
                .thenReturn(createdSoftSkillName);

        // when
        var result = mockMvc.perform(post(TECH_SKILL_NAME_API_URL)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillNameToCreate)));

        // then
        verify(softSkillNameMapper, times(1)).toReadDto(any());
        verify(softSkillNameService, times(1)).create(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdSoftSkillName.id()))
                .andExpect(jsonPath("$.name").value(createdSoftSkillName.name()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateSoftSkillName() throws Exception {
        // given
        var softSkillNameToCreate = createSoftSkillNameWriteDto(1);
        var creatingSoftSkillNameError = new ErrorCreatingEntityException();

        when(softSkillNameService.create(any()))
                .thenThrow(creatingSoftSkillNameError);

        // when
        var result = mockMvc.perform(post(TECH_SKILL_NAME_API_URL)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillNameToCreate)));

        // then
        verify(softSkillNameService, times(1)).create(any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingSoftSkillNameError.getMessage()));
    }

    @Test
    void shouldReturnSoftSkillNameById() throws Exception {
        // given
        var softSkillNameId = 1;
        var softSkillName = createSoftSkillNameReadDto(softSkillNameId);

        when(softSkillNameMapper.toReadDto(any()))
                .thenReturn(softSkillName);

        // when
        var result = mockMvc.perform(get(TECH_SKILL_NAME_API_URL + "/" + softSkillNameId)
                .with(jwt()));

        // then
        verify(softSkillNameService, times(1)).getById(softSkillNameId);
        verify(softSkillNameMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(softSkillName.id()))
                .andExpect(jsonPath("$.name").value(softSkillName.name()));
    }

    @Test
    void shouldReturnErrorWhenSoftSkillNameNotFound() throws Exception {
        // given
        var softSkillNameId = 1;
        var softSkillNameNotFoundError = new EntityNotFoundException(softSkillNameId);

        when(softSkillNameService.getById(softSkillNameId))
                .thenThrow(softSkillNameNotFoundError);

        // when
        var result = mockMvc.perform(get(TECH_SKILL_NAME_API_URL + "/" + softSkillNameId)
                .with(jwt()));

        // then
        verify(softSkillNameService, times(1)).getById(softSkillNameId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(softSkillNameNotFoundError.getMessage()));
    }

    @Test
    void shouldReturnModifiedSoftSkillName() throws Exception {
        // given
        var softSkillNameId = 1;
        var softSkillNameToModify = createSoftSkillNameWriteDto(softSkillNameId);
        var modifiedSoftSkillName = createSoftSkillNameReadDto(softSkillNameId);

        when(softSkillNameMapper.toReadDto(any()))
                .thenReturn(modifiedSoftSkillName);

        // when
        var result = mockMvc.perform(put(TECH_SKILL_NAME_API_URL + "/" + softSkillNameId)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillNameToModify)));

        // then
        verify(softSkillNameService, times(1)).update(any(), any());
        verify(softSkillNameMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedSoftSkillName.id()))
                .andExpect(jsonPath("$.name").value(modifiedSoftSkillName.name()));
    }

    @Test
    void shouldReturnErrorWhenEditedUserNotFound() throws Exception {
        // given
        var softSkillNameId = 1;
        var softSkillNameToModify = createSoftSkillNameWriteDto(softSkillNameId);
        var softSkillNameNotFoundError = new EntityNotFoundException(softSkillNameId);

        when(softSkillNameService.update(any(), any()))
                .thenThrow(softSkillNameNotFoundError);

        // when
        var result = mockMvc.perform(put(TECH_SKILL_NAME_API_URL + "/" + softSkillNameId)
                .with(createAdminMockJwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillNameToModify)));

        // then
        verify(softSkillNameService, times(1)).update(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(softSkillNameNotFoundError.getMessage()));
    }

    @Test
    void shouldDeleteSoftSkillName() throws Exception {
        // given
        var softSkillNameId = 1;

        // when
        var result = mockMvc.perform(delete(TECH_SKILL_NAME_API_URL + "/" + softSkillNameId)
                .with(createAdminMockJwt()));

        // then
        verify(softSkillNameService, times(1)).delete(softSkillNameId);

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(softSkillNameId))));
    }

    @Test
    void shouldReturnErrorWhenDeletedSoftSkillNameNotFound() throws Exception {
        // given
        var softSkillNameId = 1;
        var softSkillNameNotFoundError = new EntityNotFoundException(softSkillNameId);

        doThrow(softSkillNameNotFoundError).when(softSkillNameService).delete(softSkillNameId);

        // when
        var result = mockMvc.perform(delete(TECH_SKILL_NAME_API_URL + "/" + softSkillNameId)
                .with(createAdminMockJwt()));

        // then
        verify(softSkillNameService, times(1)).delete(softSkillNameId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(softSkillNameNotFoundError.getMessage()));
    }
}
