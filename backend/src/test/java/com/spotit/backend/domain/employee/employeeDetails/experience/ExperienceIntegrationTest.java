package com.spotit.backend.domain.employee.employeeDetails.experience;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.employee.employeeDetails.experience.ExperienceUtils.createExperience;
import static com.spotit.backend.domain.employee.employeeDetails.experience.ExperienceUtils.createExperienceWriteDto;
import static com.spotit.backend.domain.employee.employeeDetails.experience.ExperienceUtils.generateExperienceList;
import static com.spotit.backend.domain.userAccount.UserAccountUtils.createUserAccount;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.IntegrationTest;
import com.spotit.backend.domain.userAccount.UserAccount;
import com.spotit.backend.domain.userAccount.UserAccountRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class ExperienceIntegrationTest extends IntegrationTest {

    static final String EXPERIENCE_API_URL = "/api/userAccount/auth0Id1/experience";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    ExperienceRepository experienceRepository;

    UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccount = createUserAccount(1);
        userAccountRepository.save(userAccount);
    }

    @Test
    void shouldReturnListOfUserExperiences() throws Exception {
        // given
        var experienceToCreate = generateExperienceList(3);
        experienceToCreate.forEach(social -> social.setUserAccount(userAccount));
        experienceRepository.saveAll(experienceToCreate);

        // when
        var result = mockMvc.perform(get(EXPERIENCE_API_URL).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnExperienceById() throws Exception {
        // given
        var experienceToCreate = generateExperienceList(3);
        experienceToCreate.forEach(social -> social.setUserAccount(userAccount));
        var createdExperiences = experienceRepository.saveAll(experienceToCreate);
        var wantedExperience = createdExperiences.get(1);

        // when
        var result = mockMvc
                .perform(get(EXPERIENCE_API_URL + "/" + wantedExperience.getId()).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wantedExperience.getId()))
                .andExpect(jsonPath("$.companyName").value(wantedExperience.getCompanyName()))
                .andExpect(jsonPath("$.position").value(wantedExperience.getPosition()))
                .andExpect(jsonPath("$.startDate").value(wantedExperience.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(wantedExperience.getEndDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenExperienceNotFound() throws Exception {
        // given
        var experienceId = 1;

        // when
        var result = mockMvc.perform(get(EXPERIENCE_API_URL + "/" + experienceId).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(experienceId)));
    }

    @Test
    void shouldReturnCreatedExperience() throws Exception {
        // given
        var experienceToCreate = createExperienceWriteDto(1);

        // when
        var result = mockMvc.perform(post(EXPERIENCE_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(experienceToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value(experienceToCreate.companyName()))
                .andExpect(jsonPath("$.position").value(experienceToCreate.position()))
                .andExpect(jsonPath("$.startDate").value(experienceToCreate.startDate().toString()))
                .andExpect(jsonPath("$.endDate").value(experienceToCreate.endDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenCreatingExperience() throws Exception {
        // given
        var experienceWithBadDate = ExperienceWriteDto.builder()
                .companyName("CompanyName")
                .position("url".repeat(100))
                .startDate(null)
                .endDate(null)
                .build();

        // when
        var result = mockMvc.perform(post(EXPERIENCE_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(experienceWithBadDate)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedExperience() throws Exception {
        // given
        var experienceToCreate = createExperience(1);
        experienceToCreate.setUserAccount(userAccount);
        var existingExperience = experienceRepository.save(experienceToCreate);
        var modifiedExperience = createExperienceWriteDto(2);

        // when
        var result = mockMvc.perform(put(EXPERIENCE_API_URL + "/" + existingExperience.getId())
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedExperience)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingExperience.getId()))
                .andExpect(jsonPath("$.companyName").value(experienceToCreate.getCompanyName()))
                .andExpect(jsonPath("$.position").value(experienceToCreate.getPosition()))
                .andExpect(jsonPath("$.startDate").value(experienceToCreate.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(experienceToCreate.getEndDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenEditedExperienceNotFound() throws Exception {
        // given
        var experienceId = 1;
        var modifiedExperience = createExperienceWriteDto(2);

        // when
        var result = mockMvc.perform(put(EXPERIENCE_API_URL + "/" + experienceId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedExperience)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(experienceId)));
    }

    @Test
    void shouldDeleteExperience() throws Exception {
        // given
        var experienceToCreate = createExperience(1);
        experienceToCreate.setUserAccount(userAccount);
        var experienceToDelete = experienceRepository.save(experienceToCreate);

        // when
        var result = mockMvc
                .perform(delete(EXPERIENCE_API_URL + "/" + experienceToDelete.getId()).with(createMockJwt("auth0Id1")));

        // then
        assertFalse(experienceRepository.existsById(experienceToDelete.getId()));

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(experienceToDelete.getId()))));
    }

    @Test
    void shouldReturnErrorWhenDeletedExperienceNotFound() throws Exception {
        // given
        var experienceId = 1;

        // when
        var result = mockMvc.perform(delete(EXPERIENCE_API_URL + "/" + experienceId).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(experienceId)));
    }
}
