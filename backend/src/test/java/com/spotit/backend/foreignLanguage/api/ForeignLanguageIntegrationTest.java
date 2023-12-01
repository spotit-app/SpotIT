package com.spotit.backend.foreignLanguage.api;

import static com.spotit.backend.testUtils.ForeignLanguageNameUtils.createForeignLanguageName;
import static com.spotit.backend.testUtils.ForeignLanguageUtils.createForeignLanguage;
import static com.spotit.backend.testUtils.ForeignLanguageUtils.createWriteForeignLanguageDto;
import static com.spotit.backend.testUtils.ForeignLanguageUtils.generateForeignLanguageList;
import static com.spotit.backend.testUtils.GeneralUtils.getDeletedEntityMessage;
import static com.spotit.backend.testUtils.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.testUtils.GeneralUtils.getErrorCreatingEntityMessage;
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
import com.spotit.backend.foreignLanguage.dto.WriteForeignLanguageDto;
import com.spotit.backend.foreignLanguage.model.ForeignLanguageName;
import com.spotit.backend.foreignLanguage.repository.ForeignLanguageNameRepository;
import com.spotit.backend.foreignLanguage.repository.ForeignLanguageRepository;
import com.spotit.backend.userAccount.model.UserAccount;
import com.spotit.backend.userAccount.repository.UserAccountRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class ForeignLanguageIntegrationTest {

    static final String FOREIGN_LANGUAGE_API_URL = "/api/userAccount/auth0Id1/foreignLanguage";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    ForeignLanguageRepository foreignLanguageRepository;

    @Autowired
    ForeignLanguageNameRepository foreignLanguageNameRepository;

    UserAccount userAccount;
    ForeignLanguageName foreignLanguageName;

    @BeforeEach
    void setUp() {
        userAccount = createUserAccount(1);
        userAccountRepository.save(userAccount);
        foreignLanguageName = createForeignLanguageName(1);
        foreignLanguageNameRepository.save(foreignLanguageName);
    }

    @Test
    void shouldReturnListOfUserForeignLanguages() throws Exception {
        // given
        var foreignLanguagesToCreate = generateForeignLanguageList(3);
        foreignLanguagesToCreate.stream().forEach(ForeignLanguage -> {
            ForeignLanguage.setUserAccount(userAccount);
            ForeignLanguage.setForeignLanguageName(foreignLanguageName);
        });
        foreignLanguageRepository.saveAll(foreignLanguagesToCreate);

        // when
        var result = mockMvc.perform(get(FOREIGN_LANGUAGE_API_URL).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(foreignLanguagesToCreate.size()));
    }

    @Test
    void shouldReturnForeignLanguageById() throws Exception {
        // given
        var foreignLanguagesToCreate = generateForeignLanguageList(3);
        foreignLanguagesToCreate.stream().forEach(foreignLanguage -> {
            foreignLanguage.setUserAccount(userAccount);
            foreignLanguage.setForeignLanguageName(foreignLanguageName);
        });
        var createdForeignLanguages = foreignLanguageRepository.saveAll(foreignLanguagesToCreate);
        var wantedForeignLanguage = createdForeignLanguages.get(1);

        // when
        var result = mockMvc.perform(get(FOREIGN_LANGUAGE_API_URL + "/" + wantedForeignLanguage.getId()).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wantedForeignLanguage.getId()))
                .andExpect(jsonPath("$.languageLevel").value(wantedForeignLanguage.getLanguageLevel()));
    }

    @Test
    void shouldReturnErrorWhenForeignLanguageNotFound() throws Exception {
        // given
        var foreignLanguageId = 1;

        // when
        var result = mockMvc.perform(get(FOREIGN_LANGUAGE_API_URL + "/" + foreignLanguageId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(foreignLanguageId)));
    }

    @Test
    void shouldReturnCreatedForeignLanguageWithExistingForeignLanguageName() throws Exception {
        // given
        var foreignLanguageToCreate = WriteForeignLanguageDto.builder()
                .languageLevel("")
                .foreignLanguageNameId(foreignLanguageName.getId())
                .build();

        // when
        var result = mockMvc.perform(post(FOREIGN_LANGUAGE_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(foreignLanguageToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.languageLevel").value(foreignLanguageToCreate.languageLevel()))
                .andExpect(jsonPath("$.foreignLanguageName").value(foreignLanguageName.getName()));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingForeignLanguage() throws Exception {
        // given
        var foreignLanguageWithTooLongSkillName = WriteForeignLanguageDto.builder()
                .languageLevel("too_long_language_level")
                .foreignLanguageNameId(foreignLanguageName.getId())
                .build();

        // when
        var result = mockMvc.perform(post(FOREIGN_LANGUAGE_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(foreignLanguageWithTooLongSkillName)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedForeignLanguage() throws Exception {
        // given
        var foreignLanguageToCreate = createForeignLanguage(1);
        foreignLanguageToCreate.setUserAccount(userAccount);
        foreignLanguageToCreate.setForeignLanguageName(foreignLanguageName);
        var existingForeignLanguage = foreignLanguageRepository.save(foreignLanguageToCreate);
        var modifiedForeignLanguage = createWriteForeignLanguageDto(2);

        // when
        var result = mockMvc.perform(put(FOREIGN_LANGUAGE_API_URL + "/" + existingForeignLanguage.getId())
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedForeignLanguage)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingForeignLanguage.getId()))
                .andExpect(jsonPath("$.languageLevel").value(modifiedForeignLanguage.languageLevel()));
    }

    @Test
    void shouldReturnErrorWhenEditedForeignLanguageNotFound() throws Exception {
        // given
        var foreignLanguageId = 1;
        var modifiedForeignLanguage = createWriteForeignLanguageDto(2);

        // when
        var result = mockMvc.perform(put(FOREIGN_LANGUAGE_API_URL + "/" + foreignLanguageId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedForeignLanguage)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(foreignLanguageId)));
    }

    @Test
    void shouldDeleteForeignLanguage() throws Exception {
        // given
        var foreignLanguageToCreate = createForeignLanguage(1);
        foreignLanguageToCreate.setUserAccount(userAccount);
        foreignLanguageToCreate.setForeignLanguageName(foreignLanguageName);
        var foreignLanguageToDelete = foreignLanguageRepository.save(foreignLanguageToCreate);

        // when
        var result = mockMvc.perform(delete(FOREIGN_LANGUAGE_API_URL + "/" + foreignLanguageToDelete.getId())
                .with(jwt()));

        // then
        assertFalse(foreignLanguageRepository.existsById(foreignLanguageToDelete.getId()));

        result.andExpect(status().isOk())
                .andExpect(content().string(getDeletedEntityMessage(foreignLanguageToDelete.getId())));
    }

    @Test
    void shouldReturnErrorWhenDeletedForeignLanguageNotFound() throws Exception {
        // given
        var foreignLanguageId = 1;

        // when
        var result = mockMvc.perform(delete(FOREIGN_LANGUAGE_API_URL + "/" + foreignLanguageId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(foreignLanguageId)));
    }
}
