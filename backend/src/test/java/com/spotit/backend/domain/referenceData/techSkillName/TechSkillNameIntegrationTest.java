package com.spotit.backend.domain.referenceData.techSkillName;

import static com.spotit.backend.abstraction.GeneralUtils.createAdminMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameUtils.createTechSkillName;
import static com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameUtils.generateTechSkillNameList;
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
class TechSkillNameIntegrationTest extends IntegrationTest {

    static final String TECH_SKILL_NAME_API_URL = "/api/techSkillName";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TechSkillNameRepository techSkillNameRepository;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StorageService storageService;

    @Test
    void shouldReturnAllTechSkillNames() throws Exception {
        // given
        var createdTechSkillNames = generateTechSkillNameList(3);
        techSkillNameRepository.saveAll(createdTechSkillNames);

        // when
        var result = mockMvc.perform(get(TECH_SKILL_NAME_API_URL).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(createdTechSkillNames.size()));
    }

    @Test
    void shouldReturnTechSkillNameById() throws Exception {
        // given
        var createdTechSkillNames = generateTechSkillNameList(3);
        techSkillNameRepository.saveAll(createdTechSkillNames);

        // when
        var result = mockMvc
                .perform(get(TECH_SKILL_NAME_API_URL + "/" + createdTechSkillNames.get(1).getId()).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdTechSkillNames.get(1).getId()));
    }

    @Test
    void shouldReturnErrorWhenTechSkillNameNotFound() throws Exception {
        // given
        var techSkillNameId = 1;

        // when
        var result = mockMvc.perform(get(TECH_SKILL_NAME_API_URL + "/" + techSkillNameId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(techSkillNameId)));
    }

    @Test
    void shouldReturnCreatedTechSkillName() throws Exception {
        // given
        var techSkillNameToCreateName = new MockMultipartFile("name", "Name 1".getBytes());
        var techSkillNameToCreateLogo = new MockMultipartFile("logo", "logo".getBytes());
        when(storageService.uploadFile(any(), any(), any())).thenReturn("http://logo.url");

        // when
        var result = mockMvc.perform(multipart(TECH_SKILL_NAME_API_URL)
                .file(techSkillNameToCreateName)
                .file(techSkillNameToCreateLogo)
                .with(createAdminMockJwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Name 1"))
                .andExpect(jsonPath("$.logoUrl").value("http://logo.url"));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingTechSkillName() throws Exception {
        // given
        var techSkillNameToCreateName = new MockMultipartFile("name", "tooLongName".repeat(50).getBytes());
        var techSkillNameToCreateLogo = new MockMultipartFile("logo", "logo".getBytes());
        when(storageService.uploadFile(any(), any(), any())).thenReturn("http://logo.url");

        // when
        var result = mockMvc.perform(multipart(TECH_SKILL_NAME_API_URL)
                .file(techSkillNameToCreateName)
                .file(techSkillNameToCreateLogo)
                .with(createAdminMockJwt()));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedTechSkillName() throws Exception {
        // given
        var existingTechSkillName = createTechSkillName(1);
        var modifiedTechSkillNameName = new MockMultipartFile("name", "newName".getBytes());
        techSkillNameRepository.save(existingTechSkillName);

        // when
        var result = mockMvc.perform(
                multipart(PUT, TECH_SKILL_NAME_API_URL + "/" + existingTechSkillName.getId())
                        .file(modifiedTechSkillNameName)
                        .with(createAdminMockJwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("newName"))
                .andExpect(jsonPath("$.logoUrl").value(existingTechSkillName.getLogoUrl()));
    }

    @Test
    void shouldReturnErrorWhenEditedTechSkillNameNotFound() throws Exception {
        // given
        var techSkillNameId = 1;
        var techSkillNameToModifyName = new MockMultipartFile("name", "newName".getBytes());

        // when
        var result = mockMvc.perform(multipart(PUT, TECH_SKILL_NAME_API_URL + "/" + techSkillNameId)
                .file(techSkillNameToModifyName)
                .with(createAdminMockJwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(techSkillNameId)));
    }

    @Test
    void shouldDeleteTechSkillName() throws Exception {
        // given
        var techSkillNameToDelete = createTechSkillName(1);
        techSkillNameRepository.save(techSkillNameToDelete);

        // when
        var result = mockMvc.perform(delete(TECH_SKILL_NAME_API_URL + "/" + techSkillNameToDelete.getId())
                .with(createAdminMockJwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(techSkillNameToDelete.getId()))));

        assertFalse(techSkillNameRepository.existsById(techSkillNameToDelete.getId()));
    }

    @Test
    void shouldReturnErrorWhenDeletedTechSkillNameNotFound() throws Exception {
        // given
        var techSkillNameId = 1;

        // when
        var result = mockMvc.perform(delete(TECH_SKILL_NAME_API_URL + "/" + techSkillNameId).with(
                createAdminMockJwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(techSkillNameId)));
    }
}
