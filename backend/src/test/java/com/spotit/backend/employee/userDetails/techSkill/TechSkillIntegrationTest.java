package com.spotit.backend.employee.userDetails.techSkill;

import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.employee.referenceData.techSkillName.TechSkillNameUtils.createTechSkillName;
import static com.spotit.backend.employee.userAccount.UserAccountUtils.createUserAccount;
import static com.spotit.backend.employee.userDetails.techSkill.TechSkillUtils.createTechSkill;
import static com.spotit.backend.employee.userDetails.techSkill.TechSkillUtils.createTechSkillWriteDto;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.IntegrationTest;
import com.spotit.backend.employee.referenceData.techSkillName.TechSkillName;
import com.spotit.backend.employee.referenceData.techSkillName.TechSkillNameRepository;
import com.spotit.backend.employee.userAccount.UserAccount;
import com.spotit.backend.employee.userAccount.UserAccountRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class TechSkillIntegrationTest extends IntegrationTest {

    static final String TECH_SKILL_API_URL = "/api/userAccount/auth0Id1/techSkill";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    TechSkillRepository techSkillRepository;

    @Autowired
    TechSkillNameRepository techSkillNameRepository;

    UserAccount userAccount;
    TechSkillName techSkillName;

    @BeforeEach
    void setUp() {
        userAccount = createUserAccount(1);
        userAccountRepository.save(userAccount);
        techSkillName = createTechSkillName(1);
        techSkillNameRepository.save(techSkillName);
    }

    @Test
    void shouldReturnListOfUserTechSkills() throws Exception {
        // given
        var techSkillsToCreate = List.of(
                TechSkill.builder().skillLevel(1).techSkillName(createTechSkillName(2)).userAccount(userAccount)
                        .build(),
                TechSkill.builder().skillLevel(2).techSkillName(createTechSkillName(3)).userAccount(userAccount)
                        .build(),
                TechSkill.builder().skillLevel(3).techSkillName(createTechSkillName(4)).userAccount(userAccount)
                        .build());
        techSkillRepository.saveAll(techSkillsToCreate);

        // when
        var result = mockMvc.perform(get(TECH_SKILL_API_URL).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(techSkillsToCreate.size()));
    }

    @Test
    void shouldReturnTechSkillById() throws Exception {
        // given
        var techSkillsToCreate = List.of(
                TechSkill.builder().skillLevel(1).techSkillName(createTechSkillName(2)).userAccount(userAccount)
                        .build(),
                TechSkill.builder().skillLevel(2).techSkillName(createTechSkillName(3)).userAccount(userAccount)
                        .build(),
                TechSkill.builder().skillLevel(3).techSkillName(createTechSkillName(4)).userAccount(userAccount)
                        .build());
        var createdTechSkills = techSkillRepository.saveAll(techSkillsToCreate);
        var wantedTechSkill = createdTechSkills.get(1);

        // when
        var result = mockMvc.perform(get(TECH_SKILL_API_URL + "/" + wantedTechSkill.getId()).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wantedTechSkill.getId()))
                .andExpect(jsonPath("$.skillLevel").value(wantedTechSkill.getSkillLevel()));
    }

    @Test
    void shouldReturnErrorWhenTechSkillNotFound() throws Exception {
        // given
        var techSkillId = 1;

        // when
        var result = mockMvc.perform(get(TECH_SKILL_API_URL + "/" + techSkillId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(techSkillId)));
    }

    @Test
    void shouldReturnCreatedTechSkillWithExistingTechSkillName() throws Exception {
        // given
        var techSkillToCreate = TechSkillWriteDto.builder()
                .skillLevel(7)
                .techSkillName(techSkillName.getName())
                .build();

        // when
        var result = mockMvc.perform(post(TECH_SKILL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(techSkillToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.skillLevel").value(techSkillToCreate.skillLevel()))
                .andExpect(jsonPath("$.techSkillName").value(techSkillToCreate.techSkillName()));
    }

    @Test
    void shouldReturnCreatedTechSkillWithoutExistingTechSkillName() throws Exception {
        // given
        var techSkillToCreate = createTechSkillWriteDto(1);

        // when
        var result = mockMvc.perform(post(TECH_SKILL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(techSkillToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.skillLevel").value(techSkillToCreate.skillLevel()))
                .andExpect(jsonPath("$.techSkillName").value(techSkillToCreate.techSkillName()));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingTechSkill() throws Exception {
        // given
        var techSkillWithTooLongSkillName = TechSkillWriteDto.builder()
                .skillLevel(7)
                .techSkillName("name".repeat(100))
                .build();

        // when
        var result = mockMvc.perform(post(TECH_SKILL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(techSkillWithTooLongSkillName)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedTechSkill() throws Exception {
        // given
        var techSkillToCreate = createTechSkill(1);
        techSkillToCreate.setUserAccount(userAccount);
        techSkillToCreate.setTechSkillName(techSkillName);
        var existingTechSkill = techSkillRepository.save(techSkillToCreate);
        var modifiedTechSkill = createTechSkillWriteDto(2);

        // when
        var result = mockMvc.perform(put(TECH_SKILL_API_URL + "/" + existingTechSkill.getId())
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedTechSkill)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingTechSkill.getId()))
                .andExpect(jsonPath("$.skillLevel").value(modifiedTechSkill.skillLevel()));
    }

    @Test
    void shouldReturnErrorWhenEditedTechSkillNotFound() throws Exception {
        // given
        var techSkillId = 1;
        var modifiedTechSkill = createTechSkillWriteDto(2);

        // when
        var result = mockMvc.perform(put(TECH_SKILL_API_URL + "/" + techSkillId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedTechSkill)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(techSkillId)));
    }

    @Test
    void shouldDeleteTechSkill() throws Exception {
        // given
        var techSkillToCreate = createTechSkill(1);
        techSkillToCreate.setUserAccount(userAccount);
        techSkillToCreate.setTechSkillName(techSkillName);
        var techSkillToDelete = techSkillRepository.save(techSkillToCreate);

        // when
        var result = mockMvc.perform(delete(TECH_SKILL_API_URL + "/" + techSkillToDelete.getId()).with(jwt()));

        // then
        assertFalse(techSkillRepository.existsById(techSkillToDelete.getId()));

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(techSkillToDelete.getId()))));
    }

    @Test
    void shouldReturnErrorWhenDeletedTechSkillNotFound() throws Exception {
        // given
        var techSkillId = 1;

        // when
        var result = mockMvc.perform(delete(TECH_SKILL_API_URL + "/" + techSkillId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(techSkillId)));
    }
}
