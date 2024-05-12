package com.spotit.backend.domain.employee.employeeDetails.softSkill;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.employee.employeeDetails.softSkill.SoftSkillUtils.createSoftSkill;
import static com.spotit.backend.domain.employee.employeeDetails.softSkill.SoftSkillUtils.createSoftSkillWriteDto;
import static com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameUtils.createSoftSkillName;
import static com.spotit.backend.domain.userAccount.UserAccountUtils.createUserAccount;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillName;
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameRepository;
import com.spotit.backend.domain.userAccount.UserAccount;
import com.spotit.backend.domain.userAccount.UserAccountRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class SoftSkillIntegrationTest extends IntegrationTest {

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
        var softSkillsToCreate = List.of(
                SoftSkill.builder().skillLevel(1).softSkillName(createSoftSkillName(2))
                        .userAccount(userAccount)
                        .build(),
                SoftSkill.builder().skillLevel(2).softSkillName(createSoftSkillName(3))
                        .userAccount(userAccount)
                        .build(),
                SoftSkill.builder().skillLevel(3).softSkillName(createSoftSkillName(4))
                        .userAccount(userAccount)
                        .build());
        softSkillRepository.saveAll(softSkillsToCreate);

        // when
        var result = mockMvc.perform(get(SOFT_SKILL_API_URL).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(softSkillsToCreate.size()));
    }

    @Test
    void shouldReturnsoftSkillById() throws Exception {
        // given
        var softSkillsToCreate = List.of(
                SoftSkill.builder().skillLevel(1).softSkillName(createSoftSkillName(2))
                        .userAccount(userAccount)
                        .build(),
                SoftSkill.builder().skillLevel(2).softSkillName(createSoftSkillName(3))
                        .userAccount(userAccount)
                        .build(),
                SoftSkill.builder().skillLevel(3).softSkillName(createSoftSkillName(4))
                        .userAccount(userAccount)
                        .build());
        var createdsoftSkills = softSkillRepository.saveAll(softSkillsToCreate);
        var wantedsoftSkill = createdsoftSkills.get(1);

        // when
        var result = mockMvc
                .perform(get(SOFT_SKILL_API_URL + "/" + wantedsoftSkill.getId()).with(createMockJwt("auth0Id1")));

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
        var result = mockMvc.perform(get(SOFT_SKILL_API_URL + "/" + softSkillId).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(softSkillId)));
    }

    @Test
    void shouldReturnCreatedsoftSkillWithExistingsoftSkillName() throws Exception {
        // given
        var softSkillToCreate = SoftSkillWriteDto.builder()
                .skillLevel(7)
                .softSkillName(softSkillName.getName())
                .build();

        // when
        var result = mockMvc.perform(post(SOFT_SKILL_API_URL)
                .with(createMockJwt("auth0Id1"))
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
        var softSkillToCreate = createSoftSkillWriteDto(1);

        // when
        var result = mockMvc.perform(post(SOFT_SKILL_API_URL)
                .with(createMockJwt("auth0Id1"))
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
        var softSkillWithTooLongSkillName = SoftSkillWriteDto.builder()
                .skillLevel(7)
                .softSkillName("name".repeat(100))
                .build();

        // when
        var result = mockMvc.perform(post(SOFT_SKILL_API_URL)
                .with(createMockJwt("auth0Id1"))
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
        var modifiedsoftSkill = createSoftSkillWriteDto(2);

        // when
        var result = mockMvc.perform(put(SOFT_SKILL_API_URL + "/" + existingsoftSkill.getId())
                .with(createMockJwt("auth0Id1"))
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
        var modifiedsoftSkill = createSoftSkillWriteDto(2);

        // when
        var result = mockMvc.perform(put(SOFT_SKILL_API_URL + "/" + softSkillId)
                .with(createMockJwt("auth0Id1"))
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
        var result = mockMvc
                .perform(delete(SOFT_SKILL_API_URL + "/" + softSkillToDelete.getId()).with(createMockJwt("auth0Id1")));

        // then
        assertFalse(softSkillRepository.existsById(softSkillToDelete.getId()));

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(softSkillToDelete.getId()))));
    }

    @Test
    void shouldReturnErrorWhenDeletedsoftSkillNotFound() throws Exception {
        // given
        var softSkillId = 1;

        // when
        var result = mockMvc.perform(delete(SOFT_SKILL_API_URL + "/" + softSkillId).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(softSkillId)));
    }
}
