package com.spotit.backend.techSkill.controller;

import static com.spotit.backend.testUtils.GeneralUtils.getDeletedEntityMessage;
import static com.spotit.backend.testUtils.TechSkillUtils.createReadTechSkillDto;
import static com.spotit.backend.testUtils.TechSkillUtils.createWriteTechSkillDto;
import static com.spotit.backend.testUtils.TechSkillUtils.generateTechSkillList;
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
import com.spotit.backend.techSkill.dto.WriteTechSkillDto;
import com.spotit.backend.techSkill.mapper.TechSkillMapper;
import com.spotit.backend.techSkill.service.TechSkillNameService;
import com.spotit.backend.techSkill.service.TechSkillService;

@WebMvcTest(TechSkillController.class)
@Import(SecurityConfig.class)
class TechSkillControllerTest {

    static final String TECH_SKILL_API_URL = "/api/userAccount/auth0Id1/techSkill";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TechSkillService techSkillService;

    @MockBean
    TechSkillNameService techSkillNameService;

    @MockBean
    TechSkillMapper techSkillMapper;

    @Test
    void shouldReturnListOfTechSkills() throws Exception {
        //given
        var userAuth0Id = "auth0Id1";

        when(techSkillService.getAllByUserAccountAuth0Id(userAuth0Id))
            .thenReturn(generateTechSkillList(3));
        when(techSkillMapper.toReadDto(any()))
            .thenReturn(createReadTechSkillDto(1));

        //when
        var result = mockMvc.perform(get(TECH_SKILL_API_URL).with(jwt()));

        //then
        verify(techSkillService, times(1)).getAllByUserAccountAuth0Id(userAuth0Id);
        verify(techSkillMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedTechSkill() throws Exception {
        // given
        var techSkillToCreate = createWriteTechSkillDto(1);
        var createdTechSkill = createReadTechSkillDto(1);

        when(techSkillMapper.toReadDto(any()))
                .thenReturn(createdTechSkill);

        // when
        var result = mockMvc.perform(post(TECH_SKILL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(techSkillToCreate)));

        // then
        verify(techSkillMapper, times(1)).toReadDto(any());
        verify(techSkillService, times(1)).create(any(), any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdTechSkill.id()))
                .andExpect(jsonPath("$.techSkillName").value(createdTechSkill.techSkillName()))
                .andExpect(jsonPath("$.skillLevel").value(createdTechSkill.skillLevel()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateTechSkill() throws Exception {
        // given
        var techSkillToCreate = createWriteTechSkillDto(1);
        var creatingTechSkillError = new ErrorCreatingEntityException();

        when(techSkillService.create(any(), any()))
                .thenThrow(creatingTechSkillError);

        // when
        var result = mockMvc.perform(post(TECH_SKILL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(techSkillToCreate)));

        // then
        verify(techSkillService, times(1)).create(any(), any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingTechSkillError.getMessage()));
    }

    @Test
    void shouldReturnTechSkillById() throws Exception {
        // given
        var techSkillId = 1;
        var techSkill = createReadTechSkillDto(techSkillId);

        when(techSkillMapper.toReadDto(any()))
                .thenReturn(techSkill);

        // when
        var result = mockMvc.perform(get(TECH_SKILL_API_URL + "/" + techSkillId)
                .with(jwt()));

        // then
        verify(techSkillService, times(1)).getById(techSkillId);
        verify(techSkillMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(techSkill.id()))
                .andExpect(jsonPath("$.techSkillName").value(techSkill.techSkillName()))
                .andExpect(jsonPath("$.skillLevel").value(techSkill.skillLevel()));
    }

    @Test
    void shouldReturnErrorWhenTechSkillNotFound() throws Exception {
        // given
        var techSkillId = 1;
        var techSkillNotFoundError = new EntityNotFoundException(techSkillId);

        when(techSkillService.getById(techSkillId))
                .thenThrow(techSkillNotFoundError);

        // when
        var result = mockMvc.perform(get(TECH_SKILL_API_URL + "/" + techSkillId)
                .with(jwt()));

        // then
        verify(techSkillService, times(1)).getById(techSkillId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(techSkillNotFoundError.getMessage()));
    }

    @Test
    void shouldReturnModifiedTechSkill() throws Exception {
        // given
        var techSkillId = 1;
        var modifiedTechSkill = createReadTechSkillDto(techSkillId);
        var techSkillToModify = WriteTechSkillDto.builder()
                .skillLevel(5)
                .build();

        when(techSkillMapper.toReadDto(any()))
                .thenReturn(modifiedTechSkill);

        // when
        var result = mockMvc.perform(put(TECH_SKILL_API_URL + "/" + techSkillId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(techSkillToModify)));

        // then
        verify(techSkillService, times(1)).update(any(), any());
        verify(techSkillMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedTechSkill.id()))
                .andExpect(jsonPath("$.techSkillName").value(modifiedTechSkill.techSkillName()))
                .andExpect(jsonPath("$.skillLevel").value(modifiedTechSkill.skillLevel()));
    }

    @Test
    void shouldReturnErrorWhenModifiedTechSkillNotFound() throws Exception {
        // given
        var techSkillId = 1;
        var techSkillNotFoundError = new EntityNotFoundException(techSkillId);
        var techSkillToModify = WriteTechSkillDto.builder().skillLevel(5).build();

        when(techSkillService.update(any(), any()))
                .thenThrow(techSkillNotFoundError);

        // when
        var result = mockMvc.perform(put(TECH_SKILL_API_URL + "/" + techSkillId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(techSkillToModify)));

        // then
        verify(techSkillService, times(1)).update(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(techSkillNotFoundError.getMessage()));
    }

    @Test
    void shouldDeleteTechSkill() throws Exception {
        // given
        var techSkillId = 1;

        // when
        var result = mockMvc.perform(delete(TECH_SKILL_API_URL + "/" + techSkillId)
                .with(jwt()));

        // then
        verify(techSkillService, times(1)).delete(techSkillId);

        result.andExpect(status().isOk())
                .andExpect(content().string(getDeletedEntityMessage(techSkillId)));
    }

    @Test
    void shouldReturnErrorWhenDeletedTechSkillNotFound() throws Exception {
        // given
        var techSkillId = 1;
        var techSkillNotFoundError = new EntityNotFoundException(techSkillId);

        doThrow(techSkillNotFoundError).when(techSkillService).delete(techSkillId);

        // when
        var result = mockMvc.perform(delete(TECH_SKILL_API_URL + "/" + techSkillId)
                .with(jwt()));

        // then
        verify(techSkillService, times(1)).delete(techSkillId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(techSkillNotFoundError.getMessage()));
}
}
