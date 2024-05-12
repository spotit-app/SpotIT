package com.spotit.backend.domain.referenceData.foreignLanguageName;

import static com.spotit.backend.abstraction.GeneralUtils.createAdminMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageNameUtils.createForeignLanguageName;
import static com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageNameUtils.generateForeignLanguageNameList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.IntegrationTest;
import com.spotit.backend.storage.StorageService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class ForeignLanguageNameIntegrationTest extends IntegrationTest {

    static final String FOREIGN_LANGUAGE_NAME_API = "/api/foreignLanguageName";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ForeignLanguageNameRepository foreignLanguageNameRepository;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StorageService storageService;

    @Test
    void shouldReturnAllForeignLanguageNames() throws Exception {
        // given
        var createdForeignLanguageNames = generateForeignLanguageNameList(3);
        foreignLanguageNameRepository.saveAll(createdForeignLanguageNames);

        // when
        var result = mockMvc.perform(get(FOREIGN_LANGUAGE_NAME_API).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(createdForeignLanguageNames.size()));
    }

    @Test
    void shouldReturnForeignLanguageNameById() throws Exception {
        // given
        var createdForeignLanguageNames = generateForeignLanguageNameList(3);
        foreignLanguageNameRepository.saveAll(createdForeignLanguageNames);

        // when
        var result = mockMvc.perform(get(FOREIGN_LANGUAGE_NAME_API + "/" + createdForeignLanguageNames.get(1).getId())
                .with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdForeignLanguageNames.get(1).getId()));
    }

    @Test
    void shouldReturnErrorWhenForeignLanguageNameNotFound() throws Exception {
        // given
        var foreignLanguageNameId = 1;

        // when
        var result = mockMvc.perform(get(FOREIGN_LANGUAGE_NAME_API + "/" + foreignLanguageNameId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(foreignLanguageNameId)));
    }

    @Test
    void shouldReturnCreatedForeignLanguageName() throws Exception {
        // given
        var foreignLanguageNameToCreateName = new MockMultipartFile("name", "Name 1".getBytes());
        var foreignLanguageNameToCreateFlag = new MockMultipartFile("flag", "flag".getBytes());
        when(storageService.uploadFile(any(), any(), any())).thenReturn("http://flag.url");

        // when
        var result = mockMvc.perform(multipart(FOREIGN_LANGUAGE_NAME_API)
                .file(foreignLanguageNameToCreateName)
                .file(foreignLanguageNameToCreateFlag)
                .with(createAdminMockJwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Name 1"))
                .andExpect(jsonPath("$.flagUrl").value("http://flag.url"));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingForeignLanguageName() throws Exception {
        // given
        var foreignLanguageNameToCreateName = new MockMultipartFile("name", "tooLongName".repeat(50).getBytes());
        var foreignLanguageNameToCreateFlag = new MockMultipartFile("flag", "flag".getBytes());
        when(storageService.uploadFile(any(), any(), any())).thenReturn("http://flag.url");

        // when
        var result = mockMvc.perform(multipart(FOREIGN_LANGUAGE_NAME_API)
                .file(foreignLanguageNameToCreateName)
                .file(foreignLanguageNameToCreateFlag)
                .with(createAdminMockJwt()));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedForeignLanguageName() throws Exception {
        // given
        var existingForeignLanguageName = createForeignLanguageName(1);
        var modifiedForeignLanguageName = new MockMultipartFile("name", "newName".getBytes());
        foreignLanguageNameRepository.save(existingForeignLanguageName);

        // when
        var result = mockMvc.perform(
                multipart(PUT, FOREIGN_LANGUAGE_NAME_API + "/" + existingForeignLanguageName.getId())
                        .file(modifiedForeignLanguageName)
                        .with(createAdminMockJwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("newName"))
                .andExpect(jsonPath("$.flagUrl").value(existingForeignLanguageName.getFlagUrl()));
    }

    @Test
    void shouldReturnErrorWhenEditedForeignLanguageNameNotFound() throws Exception {
        // given
        var foreignLanguageNameId = 1;
        var foreignLanguageNameToModifyName = new MockMultipartFile("name", "newName".getBytes());

        // when
        var result = mockMvc.perform(multipart(PUT, FOREIGN_LANGUAGE_NAME_API + "/" + foreignLanguageNameId)
                .file(foreignLanguageNameToModifyName)
                .with(createAdminMockJwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(foreignLanguageNameId)));
    }

    @Test
    void shouldDeleteForeignLanguageName() throws Exception {
        // given
        var foreignLanguageNameToDelete = createForeignLanguageName(1);
        foreignLanguageNameRepository.save(foreignLanguageNameToDelete);

        // when
        var result = mockMvc.perform(delete(FOREIGN_LANGUAGE_NAME_API + "/" + foreignLanguageNameToDelete.getId())
                .with(createAdminMockJwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(foreignLanguageNameToDelete.getId()))));

        assertFalse(foreignLanguageNameRepository.existsById(foreignLanguageNameToDelete.getId()));
    }

    @Test
    void shouldReturnErrorWhenDeletedForeignLanguageNameNotFound() throws Exception {
        // given
        var foreignLanguageNameId = 1;

        // when
        var result = mockMvc.perform(delete(FOREIGN_LANGUAGE_NAME_API + "/" + foreignLanguageNameId).with(
                createAdminMockJwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(foreignLanguageNameId)));
    }
}
