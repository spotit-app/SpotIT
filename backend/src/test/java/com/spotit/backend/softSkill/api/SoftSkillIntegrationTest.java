package com.spotit.backend.softSkill.api;

import static com.spotit.backend.testUtils.GeneralUtils.getDeletedEntityMessage;
import static com.spotit.backend.testUtils.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.testUtils.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.testUtils.SoftSkillNameUtils.createSoftSkillName;
import static com.spotit.backend.testUtils.SoftSkillUtils.createSoftSkill;
import static com.spotit.backend.testUtils.SoftSkillUtils.createWriteSoftSkillDto;
import static com.spotit.backend.testUtils.SoftSkillUtils.generateSoftSkillList;
import static com.spotit.backend.testUtils.UserAccountUtils.createUserAccount;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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
import com.spotit.backend.softSkill.dto.WriteSoftSkillDto;
import com.spotit.backend.softSkill.model.SoftSkillName;
import com.spotit.backend.softSkill.repository.SoftSkillNameRepository;
import com.spotit.backend.softSkill.repository.SoftSkillRepository;
import com.spotit.backend.userAccount.model.UserAccount;
import com.spotit.backend.userAccount.repository.UserAccountRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class SoftSkillIntegrationTest {

    static final String SOFT_SKILL_API_URL = "/api/userAccount/auth0Id1/softSkill";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    SoftSkillRepository softSkillRepository;

    @Autowired
    SoftSkillNameRepository softSkillNameRepository;

    UserAccount userAccount;
    SoftSkillName softSkillName;

    @BeforeEach
    void setUp() {
        userAccount = createUserAccount(1);
        userAccountRepository.save(userAccount);
        softSkillName = createSoftSkillName(1);
        softSkillNameRepository.save(softSkillName);
    }

    @Test
    void shouldReturnListOfUsersoftSkills() throws Exception {
        // given
        var softSkillsToCreate = generateSoftSkillList(3);
        softSkillsToCreate.stream().forEach(softSkill -> {
            softSkill.setUserAccount(userAccount);
            softSkill.setSoftSkillName(softSkillName);
        });
        softSkillRepository.saveAll(softSkillsToCreate);

        // when
        var result = mockMvc.perform(get(SOFT_SKILL_API_URL).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(softSkillsToCreate.size()));
    }

    @Test
    void shouldReturnsoftSkillById() throws Exception {
        // given
        var softSkillsToCreate = generateSoftSkillList(3);
        softSkillsToCreate.stream().forEach(softSkill -> {
            softSkill.setUserAccount(userAccount);
            softSkill.setSoftSkillName(softSkillName);
        });
        var createdsoftSkills = softSkillRepository.saveAll(softSkillsToCreate);
        var wantedsoftSkill = createdsoftSkills.get(1);

        // when
        var result = mockMvc.perform(get(SOFT_SKILL_API_URL + "/" + wantedsoftSkill.getId()).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wantedsoftSkill.getId()))
                .andExpect(jsonPath("$.skillLevel").value(wantedsoftSkill.getSkillLevel()));
    }

    @Test
    void shouldReturnErrorWhensoftSkillNotFound() throws Exception {
        // given
        var softSkillId = 1;

        // when
        var result = mockMvc.perform(get(SOFT_SKILL_API_URL + "/" + softSkillId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(softSkillId)));
    }

    @Test
    void shouldReturnCreatedsoftSkillWithExistingsoftSkillName() throws Exception {
        // given
        var softSkillToCreate = WriteSoftSkillDto.builder()
                .skillLevel(7)
                .softSkillName(softSkillName.getName())
                .build();

        // when
        var result = mockMvc.perform(post(SOFT_SKILL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.skillLevel").value(softSkillToCreate.skillLevel()))
                .andExpect(jsonPath("$.softSkillName").value(softSkillToCreate.softSkillName()));
    }

    @Test
    void shouldReturnCreatedsoftSkillWithoutExistingsoftSkillName() throws Exception {
        // given
        var softSkillToCreate = createWriteSoftSkillDto(1);

        // when
        var result = mockMvc.perform(post(SOFT_SKILL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.skillLevel").value(softSkillToCreate.skillLevel()))
                .andExpect(jsonPath("$.softSkillName").value(softSkillToCreate.softSkillName()));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingsoftSkill() throws Exception {
        // given
        var softSkillWithTooLongSkillName = WriteSoftSkillDto.builder()
                .skillLevel(7)
                .softSkillName("name".repeat(100))
                .build();

        // when
        var result = mockMvc.perform(post(SOFT_SKILL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(softSkillWithTooLongSkillName)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedsoftSkill() throws Exception {
        // given
        var softSkillToCreate = createSoftSkill(1);
        softSkillToCreate.setUserAccount(userAccount);
        softSkillToCreate.setSoftSkillName(softSkillName);
        var existingsoftSkill = softSkillRepository.save(softSkillToCreate);
        var modifiedsoftSkill = createWriteSoftSkillDto(2);

        // when
        var result = mockMvc.perform(put(SOFT_SKILL_API_URL + "/" + existingsoftSkill.getId())
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedsoftSkill)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingsoftSkill.getId()))
                .andExpect(jsonPath("$.skillLevel").value(modifiedsoftSkill.skillLevel()));
    }

    @Test
    void shouldReturnErrorWhenEditedsoftSkillNotFound() throws Exception {
        // given
        var softSkillId = 1;
        var modifiedsoftSkill = createWriteSoftSkillDto(2);

        // when
        var result = mockMvc.perform(put(SOFT_SKILL_API_URL + "/" + softSkillId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedsoftSkill)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(softSkillId)));
    }

    @Test
    void shouldDeletesoftSkill() throws Exception {
        // given
        var softSkillToCreate = createSoftSkill(1);
        softSkillToCreate.setUserAccount(userAccount);
        softSkillToCreate.setSoftSkillName(softSkillName);
        var softSkillToDelete = softSkillRepository.save(softSkillToCreate);

        // when
        var result = mockMvc.perform(delete(SOFT_SKILL_API_URL + "/" + softSkillToDelete.getId()).with(jwt()));

        // then
        assertFalse(softSkillRepository.existsById(softSkillToDelete.getId()));

        result.andExpect(status().isOk())
                .andExpect(content().string(getDeletedEntityMessage(softSkillToDelete.getId())));
    }

    @Test
    void shouldReturnErrorWhenDeletedsoftSkillNotFound() throws Exception {
        // given
        var softSkillId = 1;

        // when
        var result = mockMvc.perform(delete(SOFT_SKILL_API_URL + "/" + softSkillId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(softSkillId)));
    }
}
