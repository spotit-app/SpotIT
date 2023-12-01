package com.spotit.backend.softSkill.controller;

import static com.spotit.backend.testUtils.GeneralUtils.getDeletedEntityMessage;
import static com.spotit.backend.testUtils.SoftSkillUtils.createReadSoftSkillDto;
import static com.spotit.backend.testUtils.SoftSkillUtils.createWriteSoftSkillDto;
import static com.spotit.backend.testUtils.SoftSkillUtils.generateSoftSkillList;
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
import com.spotit.backend.abstraction.exception.EntityNotFoundException;
import com.spotit.backend.abstraction.exception.ErrorCreatingEntityException;
import com.spotit.backend.config.SecurityConfig;
import com.spotit.backend.softSkill.dto.WriteSoftSkillDto;
import com.spotit.backend.softSkill.mapper.SoftSkillMapper;
import com.spotit.backend.softSkill.service.SoftSkillNameService;
import com.spotit.backend.softSkill.service.SoftSkillService;

@WebMvcTest(SoftSkillController.class)
@Import(SecurityConfig.class)
class SoftSkillControllerTest {

    static final String TECH_SKILL_API_URL = "/api/userAccount/auth0Id1/softSkill";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SoftSkillService softSkillService;

    @MockBean
    SoftSkillNameService softSkillNameService;

    @MockBean
    SoftSkillMapper softSkillMapper;

    @Test
    void shouldReturnListOfSoftSkills() throws Exception {
        //given
        var userAuth0Id = "auth0Id1";

        when(softSkillService.getAllByUserAccountAuth0Id(userAuth0Id))
            .thenReturn(generateSoftSkillList(3));
        when(softSkillMapper.toReadDto(any()))
            .thenReturn(createReadSoftSkillDto(1));

        //when
        var result = mockMvc.perform(get(TECH_SKILL_API_URL).with(jwt()));

        //then
        verify(softSkillService, times(1)).getAllByUserAccountAuth0Id(userAuth0Id);
        verify(softSkillMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedSoftSkill() throws Exception {
        // given
        var softSkillToCreate = createWriteSoftSkillDto(1);
        var createdSoftSkill = createReadSoftSkillDto(1);

        when(softSkillMapper.toReadDto(any()))
                .thenReturn(createdSoftSkill);

        // when
        var result = mockMvc.perform(post(TECH_SKILL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillToCreate)));

        // then
        verify(softSkillMapper, times(1)).toReadDto(any());
        verify(softSkillService, times(1)).create(any(), any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdSoftSkill.id()))
                .andExpect(jsonPath("$.softSkillName").value(createdSoftSkill.softSkillName()))
                .andExpect(jsonPath("$.skillLevel").value(createdSoftSkill.skillLevel()));

    }

    @Test
    void shouldReturnErrorWhenCantCreateSoftSkill() throws Exception {
        // given
        var softSkillToCreate = createWriteSoftSkillDto(1);
        var creatingSoftSkillError = new ErrorCreatingEntityException();

        when(softSkillService.create(any(), any()))
                .thenThrow(creatingSoftSkillError);

        // when
        var result = mockMvc.perform(post(TECH_SKILL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillToCreate)));

        // then
        verify(softSkillService, times(1)).create(any(), any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingSoftSkillError.getMessage()));
    }

    @Test
    void shouldReturnSoftSkillById() throws Exception {
        // given
        var softSkillId = 1;
        var softSkill = createReadSoftSkillDto(softSkillId);

        when(softSkillMapper.toReadDto(any()))
                .thenReturn(softSkill);

        // when
        var result = mockMvc.perform(get(TECH_SKILL_API_URL + "/" + softSkillId)
                .with(jwt()));

        // then
        verify(softSkillService, times(1)).getById(softSkillId);
        verify(softSkillMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(softSkill.id()))
                .andExpect(jsonPath("$.softSkillName").value(softSkill.softSkillName()))
                .andExpect(jsonPath("$.skillLevel").value(softSkill.skillLevel()));
    }

    @Test
    void shouldReturnErrorWhenSoftSkillNotFound() throws Exception {
        // given
        var softSkillId = 1;
        var softSkillNotFoundError = new EntityNotFoundException(softSkillId);

        when(softSkillService.getById(softSkillId))
                .thenThrow(softSkillNotFoundError);

        // when
        var result = mockMvc.perform(get(TECH_SKILL_API_URL + "/" + softSkillId)
                .with(jwt()));

        // then
        verify(softSkillService, times(1)).getById(softSkillId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(softSkillNotFoundError.getMessage()));
    }

    @Test
    void shouldReturnModifiedSoftSkill() throws Exception {
        // given
        var softSkillId = 1;
        var modifiedSoftSkill = createReadSoftSkillDto(softSkillId);
        var softSkillToModify = WriteSoftSkillDto.builder().skillLevel(5).build();

        when(softSkillMapper.toReadDto(any()))
                .thenReturn(modifiedSoftSkill);

        // when
        var result = mockMvc.perform(put(TECH_SKILL_API_URL + "/" + softSkillId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillToModify)));

        // then
        verify(softSkillService, times(1)).update(any(), any());
        verify(softSkillMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedSoftSkill.id()))
                .andExpect(jsonPath("$.softSkillName").value(modifiedSoftSkill.softSkillName()))
                .andExpect(jsonPath("$.skillLevel").value(modifiedSoftSkill.skillLevel()));
    }

    @Test
    void shouldReturnErrorWhenModifiedSoftSkillNotFound() throws Exception {
        // given
        var softSkillId = 1;
        var softSkillNotFoundError = new EntityNotFoundException(softSkillId);
        var softSkillToModify = WriteSoftSkillDto.builder().skillLevel(5).build();

        when(softSkillService.update(any(), any()))
                .thenThrow(softSkillNotFoundError);

        // when
        var result = mockMvc.perform(put(TECH_SKILL_API_URL + "/" + softSkillId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillToModify)));

        // then
        verify(softSkillService, times(1)).update(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(softSkillNotFoundError.getMessage()));
    }

    @Test
    void shouldDeleteSoftSkill() throws Exception {
        // given
        var softSkillId = 1;

        // when
        var result = mockMvc.perform(delete(TECH_SKILL_API_URL + "/" + softSkillId)
                .with(jwt()));

        // then
        verify(softSkillService, times(1)).delete(softSkillId);

        result.andExpect(status().isOk())
                .andExpect(content().string(getDeletedEntityMessage(softSkillId)));
    }

    @Test
    void shouldReturnErrorWhenDeletedSoftSkillNotFound() throws Exception {
        // given
        var softSkillId = 1;
        var softSkillNotFoundError = new EntityNotFoundException(softSkillId);

        doThrow(softSkillNotFoundError).when(softSkillService).delete(softSkillId);

        // when
        var result = mockMvc.perform(delete(TECH_SKILL_API_URL + "/" + softSkillId)
                .with(jwt()));

        // then
        verify(softSkillService, times(1)).delete(softSkillId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(softSkillNotFoundError.getMessage()));
    }
}
