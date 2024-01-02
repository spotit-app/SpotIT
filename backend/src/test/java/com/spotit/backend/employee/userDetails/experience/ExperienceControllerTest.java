package com.spotit.backend.employee.userDetails.experience;

import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.employee.userDetails.experience.ExperienceUtils.createExperienceReadDto;
import static com.spotit.backend.employee.userDetails.experience.ExperienceUtils.createExperienceWriteDto;
import static com.spotit.backend.employee.userDetails.experience.ExperienceUtils.generateExperienceList;
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

@WebMvcTest(ExperienceController.class)
@Import(SecurityConfig.class)
class ExperienceControllerTest {

    static final String EXPERIENCE_API_URL = "/api/userAccount/auth0Id1/experience";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ExperienceService experienceService;

    @MockBean
    ExperienceMapper experienceMapper;

    @Test
    void shouldReturnListOfUserExperiences() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";

        when(experienceService.getAllByUserAccountAuth0Id(userAuth0Id))
                .thenReturn(generateExperienceList(3));
        when(experienceMapper.toReadDto(any()))
                .thenReturn(createExperienceReadDto(1));

        // when
        var result = mockMvc.perform(get(EXPERIENCE_API_URL).with(jwt()));

        // then
        verify(experienceService, times(1)).getAllByUserAccountAuth0Id(userAuth0Id);
        verify(experienceMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedExperience() throws Exception {
        // given
        var experienceToCreate = createExperienceWriteDto(1);
        var createdExperience = createExperienceReadDto(1);

        when(experienceMapper.toReadDto(any()))
                .thenReturn(createdExperience);

        // when
        var result = mockMvc.perform(post(EXPERIENCE_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(experienceToCreate)));

        // then
        verify(experienceMapper, times(1)).fromWriteDto(any());
        verify(experienceService, times(1)).create(any(), any());
        verify(experienceMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdExperience.id()))
                .andExpect(jsonPath("$.companyName").value(createdExperience.companyName()))
                .andExpect(jsonPath("$.position").value(createdExperience.position()))
                .andExpect(jsonPath("$.startDate").value(createdExperience.startDate().toString()))
                .andExpect(jsonPath("$.endDate").value(createdExperience.endDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateExperience() throws Exception {
        // given
        var experienceToCreate = createExperienceWriteDto(1);
        var creatingExperienceException = new ErrorCreatingEntityException();

        when(experienceService.create(any(), any()))
                .thenThrow(creatingExperienceException);

        // when
        var result = mockMvc.perform(post(EXPERIENCE_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(experienceToCreate)));

        // then
        verify(experienceMapper, times(1)).fromWriteDto(experienceToCreate);
        verify(experienceService, times(1)).create(any(), any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingExperienceException.getMessage()));
    }

    @Test
    void shouldReturnExperienceById() throws Exception {
        // given
        var experienceId = 1;
        var experienceToFind = createExperienceReadDto(1);

        when(experienceMapper.toReadDto(any()))
                .thenReturn(experienceToFind);

        // when
        var result = mockMvc.perform(get(EXPERIENCE_API_URL + "/" + experienceId).with(jwt()));

        // then
        verify(experienceService, times(1)).getById(experienceId);
        verify(experienceMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(experienceToFind.id()))
                .andExpect(jsonPath("$.companyName").value(experienceToFind.companyName()))
                .andExpect(jsonPath("$.position").value(experienceToFind.position()))
                .andExpect(jsonPath("$.startDate").value(experienceToFind.startDate().toString()))
                .andExpect(jsonPath("$.endDate").value(experienceToFind.endDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenExperienceNotFound() throws Exception {
        // given
        var experienceId = 1;
        var experienceNotFoundException = new EntityNotFoundException(experienceId);

        when(experienceService.getById(experienceId))
                .thenThrow(experienceNotFoundException);

        // when
        var result = mockMvc.perform(get(EXPERIENCE_API_URL + "/" + experienceId).with(jwt()));

        // then
        verify(experienceService, times(1)).getById(experienceId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(experienceNotFoundException.getMessage()));
    }

    @Test
    void shouldReturnModifiedExperience() throws Exception {
        // given
        var experienceId = 1;
        var experienceToModify = createExperienceWriteDto(1);
        var modifiedExperience = createExperienceReadDto(1);

        when(experienceMapper.toReadDto(any())).thenReturn(modifiedExperience);

        // when
        var result = mockMvc.perform(put(EXPERIENCE_API_URL + "/" + experienceId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(experienceToModify)));

        // then
        verify(experienceMapper, times(1)).fromWriteDto(any());
        verify(experienceService, times(1)).update(any(), any());
        verify(experienceMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedExperience.id()))
                .andExpect(jsonPath("$.companyName").value(modifiedExperience.companyName()))
                .andExpect(jsonPath("$.position").value(modifiedExperience.position()))
                .andExpect(jsonPath("$.startDate").value(modifiedExperience.startDate().toString()))
                .andExpect(jsonPath("$.endDate").value(modifiedExperience.endDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenEditedExperienceNotFound() throws Exception {
        // given
        var experienceId = 1;
        var experienceToModify = createExperienceWriteDto(1);
        var experienceNotFoundException = new EntityNotFoundException(experienceId);

        when(experienceService.update(any(), any()))
                .thenThrow(experienceNotFoundException);

        // when
        var result = mockMvc.perform(put(EXPERIENCE_API_URL + "/" + experienceId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(experienceToModify)));

        // then
        verify(experienceMapper, times(1)).fromWriteDto(any());
        verify(experienceService, times(1)).update(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(experienceNotFoundException.getMessage()));
    }

    @Test
    void shouldDeleteExperience() throws Exception {
        // given
        var experienceId = 1;

        // when
        var result = mockMvc.perform(delete(EXPERIENCE_API_URL + "/" + experienceId).with(jwt()));

        // then
        verify(experienceService, times(1)).delete(experienceId);

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(experienceId))));
    }

    @Test
    void shouldReturnErrorWhenDeletedExperienceNotFound() throws Exception {
        // given
        var experienceId = 1;
        var experienceNotFoundException = new EntityNotFoundException(experienceId);

        doThrow(experienceNotFoundException).when(experienceService).delete(experienceId);

        // when
        var result = mockMvc.perform(delete(EXPERIENCE_API_URL + "/" + experienceId).with(jwt()));

        // then
        verify(experienceService, times(1)).delete(experienceId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(experienceNotFoundException.getMessage()));
    }
}
