package com.spotit.backend.domain.employee.employeeDetails.education;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.domain.employee.employeeDetails.education.EducationUtils.createEducationReadDto;
import static com.spotit.backend.domain.employee.employeeDetails.education.EducationUtils.createEducationWriteDto;
import static com.spotit.backend.domain.employee.employeeDetails.education.EducationUtils.generateEducationList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

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
import com.spotit.backend.domain.referenceData.educationLevel.EducationLevelService;

@WebMvcTest(EducationController.class)
@Import(SecurityConfig.class)
class EducationControllerTest {

    static final String EDUCATION_API_URL = "/api/userAccount/auth0Id1/education";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EducationService educationService;

    @MockBean
    EducationLevelService educationLevelService;

    @MockBean
    EducationMapper educationMapper;

    @Test
    void shouldReturnListOfEducation() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";

        when(educationService.getAllByUserAccountAuth0Id(userAuth0Id))
                .thenReturn(generateEducationList(3));
        when(educationMapper.toReadDto(any()))
                .thenReturn(createEducationReadDto(1));

        // when
        var result = mockMvc.perform(get(EDUCATION_API_URL).with(createMockJwt("auth0Id1")));

        // then
        verify(educationService, times(1)).getAllByUserAccountAuth0Id(userAuth0Id);
        verify(educationMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedEducation() throws Exception {
        // given
        var educationToCreate = createEducationWriteDto(1);
        var createdEducation = createEducationReadDto(1);

        when(educationMapper.toReadDto(any()))
                .thenReturn(createdEducation);

        // when
        var result = mockMvc.perform(post(EDUCATION_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationToCreate)));

        // then
        verify(educationMapper, times(1)).toReadDto(any());
        verify(educationService, times(1)).create(any(), any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdEducation.id()))
                .andExpect(jsonPath("$.educationLevel").value(createdEducation.educationLevel()))
                .andExpect(jsonPath("$.schoolName").value(createdEducation.schoolName()))
                .andExpect(jsonPath("$.faculty").value(createdEducation.faculty()))
                .andExpect(jsonPath("$.startDate").value(createdEducation.startDate().toString()))
                .andExpect(jsonPath("$.endDate").value(createdEducation.endDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateEducation() throws Exception {
        // given
        var educationToCreate = createEducationWriteDto(1);
        var creatingEducationError = new ErrorCreatingEntityException();

        when(educationService.create(any(), any()))
                .thenThrow(creatingEducationError);

        // when
        var result = mockMvc.perform(post(EDUCATION_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationToCreate)));

        // then
        verify(educationService, times(1)).create(any(), any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingEducationError.getMessage()));
    }

    @Test
    void shouldReturnEducationById() throws Exception {
        // given
        var educationId = 1;
        var education = createEducationReadDto(educationId);

        when(educationMapper.toReadDto(any()))
                .thenReturn(education);

        // when
        var result = mockMvc.perform(get(EDUCATION_API_URL + "/" + educationId)
                .with(createMockJwt("auth0Id1")));

        // then
        verify(educationService, times(1)).getById(educationId);
        verify(educationMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(education.id()))
                .andExpect(jsonPath("$.educationLevel").value(education.educationLevel()))
                .andExpect(jsonPath("$.schoolName").value(education.schoolName()))
                .andExpect(jsonPath("$.faculty").value(education.faculty()))
                .andExpect(jsonPath("$.startDate").value(education.startDate().toString()))
                .andExpect(jsonPath("$.endDate").value(education.endDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenEducationNotFound() throws Exception {
        // given
        var educationId = 1;
        var educationNotFoundError = new EntityNotFoundException(educationId);

        when(educationService.getById(educationId))
                .thenThrow(educationNotFoundError);

        // when
        var result = mockMvc.perform(get(EDUCATION_API_URL + "/" + educationId)
                .with(createMockJwt("auth0Id1")));

        // then
        verify(educationService, times(1)).getById(educationId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(educationNotFoundError.getMessage()));
    }

    @Test
    void shouldReturnModifiedEducation() throws Exception {
        // given
        var educationId = 1;
        var modifiedEducation = createEducationReadDto(educationId);
        var educationToModify = EducationWriteDto.builder().schoolName("SchoolName")
                .faculty("faculty")
                .startDate(LocalDate.parse("2023-12-13"))
                .endDate(LocalDate.parse("2023-12-14")).build();

        when(educationMapper.toReadDto(any()))
                .thenReturn(modifiedEducation);

        // when
        var result = mockMvc.perform(put(EDUCATION_API_URL + "/" + educationId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationToModify)));

        // then
        verify(educationService, times(1)).update(any(), any());
        verify(educationMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedEducation.id()))
                .andExpect(jsonPath("$.educationLevel").value(modifiedEducation.educationLevel()))
                .andExpect(jsonPath("$.schoolName").value(modifiedEducation.schoolName()))
                .andExpect(jsonPath("$.faculty").value(modifiedEducation.faculty()))
                .andExpect(jsonPath("$.startDate").value(modifiedEducation.startDate().toString()))
                .andExpect(jsonPath("$.endDate").value(modifiedEducation.endDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenModifiedEducationNotFound() throws Exception {
        // given
        var educationId = 1;
        var educationNotFoundError = new EntityNotFoundException(educationId);
        var educationToModify = EducationWriteDto.builder().schoolName("SchoolName")
                .faculty("faculty")
                .startDate(LocalDate.parse("2023-12-13"))
                .endDate(LocalDate.parse("2023-12-14")).build();

        when(educationService.update(any(), any()))
                .thenThrow(educationNotFoundError);

        // when
        var result = mockMvc.perform(put(EDUCATION_API_URL + "/" + educationId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationToModify)));

        // then
        verify(educationService, times(1)).update(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(educationNotFoundError.getMessage()));
    }

    @Test
    void shouldDeleteEducation() throws Exception {
        // given
        var educationId = 1;

        // when
        var result = mockMvc.perform(delete(EDUCATION_API_URL + "/" + educationId)
                .with(createMockJwt("auth0Id1")));

        // then
        verify(educationService, times(1)).delete(educationId);

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(educationId))));
    }

    @Test
    void shouldReturnErrorWhenDeletedEducationNotFound() throws Exception {
        // given
        var educationId = 1;
        var educationNotFoundError = new EntityNotFoundException(educationId);

        doThrow(educationNotFoundError).when(educationService).delete(educationId);

        // when
        var result = mockMvc.perform(delete(EDUCATION_API_URL + "/" + educationId)
                .with(createMockJwt("auth0Id1")));

        // then
        verify(educationService, times(1)).delete(educationId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(educationNotFoundError.getMessage()));
    }
}
