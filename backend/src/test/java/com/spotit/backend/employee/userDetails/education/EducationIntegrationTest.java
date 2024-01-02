package com.spotit.backend.employee.userDetails.education;

import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.employee.referenceData.educationLevel.EducationLevelUtils.createEducationLevel;
import static com.spotit.backend.employee.userAccount.UserAccountUtils.createUserAccount;
import static com.spotit.backend.employee.userDetails.education.EducationUtils.createEducation;
import static com.spotit.backend.employee.userDetails.education.EducationUtils.createEducationWriteDto;
import static com.spotit.backend.employee.userDetails.education.EducationUtils.generateEducationList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.IntegrationTest;
import com.spotit.backend.employee.referenceData.educationLevel.EducationLevel;
import com.spotit.backend.employee.referenceData.educationLevel.EducationLevelRepository;
import com.spotit.backend.employee.userAccount.UserAccount;
import com.spotit.backend.employee.userAccount.UserAccountRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class EducationIntegrationTest extends IntegrationTest {

    static final String EDUCATION_API_URL = "/api/userAccount/auth0Id1/education";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    EducationLevelRepository educationLevelRepository;

    UserAccount userAccount;
    EducationLevel educationLevel;

    @BeforeEach
    void setUp() {
        userAccount = createUserAccount(1);
        userAccountRepository.save(userAccount);
        educationLevel = createEducationLevel(1);
        educationLevelRepository.save(educationLevel);
    }

    @Test
    void shouldReturnListOfUserEducation() throws Exception {
        // given
        var educationToCreate = List.of(
                Education.builder().schoolName("SchoolName")
                        .faculty("faculty")
                        .startDate(LocalDate.parse("2023-12-13"))
                        .endDate(LocalDate.parse("2023-12-14"))
                        .educationLevel(createEducationLevel(2))
                        .userAccount(userAccount)
                        .build(),
                Education.builder().schoolName("SchoolName")
                        .faculty("faculty")
                        .startDate(LocalDate.parse("2023-12-13"))
                        .endDate(LocalDate.parse("2023-12-14"))
                        .educationLevel(createEducationLevel(3))
                        .userAccount(userAccount)
                        .build(),
                Education.builder().schoolName("SchoolName")
                        .faculty("faculty")
                        .startDate(LocalDate.parse("2023-12-13"))
                        .endDate(LocalDate.parse("2023-12-14"))
                        .educationLevel(createEducationLevel(4))
                        .userAccount(userAccount)
                        .build());
        educationRepository.saveAll(educationToCreate);

        // when
        var result = mockMvc.perform(get(EDUCATION_API_URL).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(educationToCreate.size()));
    }

    @Test
    void shouldReturnEducationById() throws Exception {
        // given
        var educationToCreate = generateEducationList(3);
        educationToCreate.stream().forEach(education -> {
            education.setUserAccount(userAccount);
            education.setEducationLevel(educationLevel);
        });
        var createdEducation = educationRepository.saveAll(educationToCreate);
        var wantedEducation = createdEducation.get(1);
        // when
        var result = mockMvc.perform(get(EDUCATION_API_URL + "/" + wantedEducation.getId()).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wantedEducation.getId()))
                .andExpect(jsonPath("$.schoolName").value(wantedEducation.getSchoolName()))
                .andExpect(jsonPath("$.faculty").value(wantedEducation.getFaculty()))
                .andExpect(jsonPath("$.startDate").value(wantedEducation.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(wantedEducation.getEndDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenEducationNotFound() throws Exception {
        // given
        var educationId = 1;

        // when
        var result = mockMvc.perform(get(EDUCATION_API_URL + "/" + educationId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(educationId)));
    }

    @Test
    void shouldReturnCreatedEducationWithExistingEducationLevel() throws Exception {
        // given
        var educationToCreate = EducationWriteDto.builder()
                .educationLevel(educationLevel.getName())
                .schoolName("SchoolName")
                .faculty("faculty")
                .startDate(LocalDate.parse("2023-12-13"))
                .endDate(LocalDate.parse("2023-12-14"))
                .build();

        // when
        var result = mockMvc.perform(post(EDUCATION_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.educationLevel").value(educationToCreate.educationLevel()))
                .andExpect(jsonPath("$.schoolName").value(educationToCreate.schoolName()))
                .andExpect(jsonPath("$.faculty").value(educationToCreate.faculty()))
                .andExpect(jsonPath("$.startDate").value(educationToCreate.startDate().toString()))
                .andExpect(jsonPath("$.endDate").value(educationToCreate.endDate().toString()));
    }

    @Test
    void shouldReturnCreatedEducationWithoutExistingEducationlevel() throws Exception {
        // given
        var educationToCreate = createEducationWriteDto(1);

        // when
        var result = mockMvc.perform(post(EDUCATION_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.educationLevel").value(educationToCreate.educationLevel()))
                .andExpect(jsonPath("$.schoolName").value(educationToCreate.schoolName()))
                .andExpect(jsonPath("$.faculty").value(educationToCreate.faculty()))
                .andExpect(jsonPath("$.startDate").value(educationToCreate.startDate().toString()))
                .andExpect(jsonPath("$.endDate").value(educationToCreate.endDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenCreatingEducation() throws Exception {
        // given
        var educationWithBadDate = EducationWriteDto.builder()
                .educationLevel("EducationLevel")
                .schoolName("schoolName")
                .faculty("faculty")
                .startDate(null)
                .endDate(null)
                .build();

        // when
        var result = mockMvc.perform(post(EDUCATION_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(educationWithBadDate)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedEducation() throws Exception {
        // given
        var educationToCreate = createEducation(1);
        educationToCreate.setUserAccount(userAccount);
        educationToCreate.setEducationLevel(educationLevel);
        var existingEducation = educationRepository.save(educationToCreate);
        var modifiedEducation = createEducationWriteDto(2);

        // when
        var result = mockMvc.perform(put(EDUCATION_API_URL + "/" + existingEducation.getId())
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedEducation)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingEducation.getId()))
                .andExpect(jsonPath("$.schoolName").value(modifiedEducation.schoolName()))
                .andExpect(jsonPath("$.faculty").value(modifiedEducation.faculty()))
                .andExpect(jsonPath("$.startDate").value(modifiedEducation.startDate().toString()))
                .andExpect(jsonPath("$.endDate").value(modifiedEducation.endDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenEditedEducationNotFound() throws Exception {
        // given
        var educationId = 1;
        var modifiedEducation = createEducationWriteDto(2);

        // when
        var result = mockMvc.perform(put(EDUCATION_API_URL + "/" + educationId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedEducation)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(educationId)));
    }

    @Test
    void shouldDeleteEducation() throws Exception {
        // given
        var educationToCreate = createEducation(1);
        educationToCreate.setUserAccount(userAccount);
        educationToCreate.setEducationLevel(educationLevel);
        var educationToDelete = educationRepository.save(educationToCreate);

        // when
        var result = mockMvc.perform(delete(EDUCATION_API_URL + "/" + educationToDelete.getId()).with(jwt()));

        // then
        assertFalse(educationRepository.existsById(educationToDelete.getId()));

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(educationToDelete.getId()))));
    }

    @Test
    void shouldReturnErrorWhenDeletedEducationNotFound() throws Exception {
        // given
        var educationId = 1;

        // when
        var result = mockMvc.perform(delete(EDUCATION_API_URL + "/" + educationId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(educationId)));
    }
}
