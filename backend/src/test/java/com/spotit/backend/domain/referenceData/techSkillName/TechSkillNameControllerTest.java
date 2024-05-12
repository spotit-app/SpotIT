package com.spotit.backend.domain.referenceData.techSkillName;

import static com.spotit.backend.abstraction.GeneralUtils.createAdminMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameUtils.createTechSkillNameReadDto;
import static com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameUtils.generateTechSkillNameList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.config.security.SecurityConfig;
import com.spotit.backend.storage.ErrorDeletingFileException;
import com.spotit.backend.storage.ErrorUploadingFileException;

@WebMvcTest(TechSkillNameController.class)
@Import(SecurityConfig.class)
class TechSkillNameControllerTest {

    static final String TECH_SKILL_NAME_API_URL = "/api/techSkillName";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TechSkillNameService techSkillNameService;

    @MockBean
    TechSkillNameMapper techSkillNameMapper;

    @Test
    void shouldReturnListOfTechSkillNames() throws Exception {
        // given
        when(techSkillNameService.getAll())
                .thenReturn(generateTechSkillNameList(3));
        when(techSkillNameMapper.toReadDto(any()))
                .thenReturn(createTechSkillNameReadDto(1));

        // when
        var result = mockMvc.perform(get(TECH_SKILL_NAME_API_URL).with(jwt()));

        // then
        verify(techSkillNameService, times(1)).getAll();
        verify(techSkillNameMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedTechSkillName() throws Exception {
        // given
        var techSkillNameName = new MockMultipartFile("name", "Name 1".getBytes());
        var techSkillNameLogo = new MockMultipartFile("logo", "logo".getBytes());
        var createdTechSkillName = createTechSkillNameReadDto(1);

        when(techSkillNameMapper.toReadDto(any()))
                .thenReturn(createdTechSkillName);

        // when
        var result = mockMvc.perform(multipart(TECH_SKILL_NAME_API_URL)
                .file(techSkillNameName)
                .file(techSkillNameLogo)
                .with(createAdminMockJwt()));

        // then
        verify(techSkillNameService, times(1)).create("Name 1", "logo".getBytes());
        verify(techSkillNameMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdTechSkillName.id()))
                .andExpect(jsonPath("$.name").value(createdTechSkillName.name()))
                .andExpect(jsonPath("$.logoUrl").value(createdTechSkillName.logoUrl()));
    }

    @Test
    void shouldReturnErrorWhenCantSaveTechSkillNameLogo() throws Exception {
        // given
        var techSkillNameName = new MockMultipartFile("name", "Name 1".getBytes());
        var techSkillNameLogo = new MockMultipartFile("logo", "logo".getBytes());
        var errorCreatingFile = new ErrorUploadingFileException("logo");

        when(techSkillNameService.create("Name 1", "logo".getBytes()))
                .thenThrow(errorCreatingFile);

        // when
        var result = mockMvc.perform(multipart(TECH_SKILL_NAME_API_URL)
                .file(techSkillNameName)
                .file(techSkillNameLogo)
                .with(createAdminMockJwt()));

        // then
        verify(techSkillNameService, times(1)).create("Name 1", "logo".getBytes());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(errorCreatingFile.getMessage()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateTechSkillName() throws Exception {
        // given
        var techSkillNameName = new MockMultipartFile("name", "Name 1".getBytes());
        var techSkillNameLogo = new MockMultipartFile("logo", "logo".getBytes());
        var creatingTechSkillNameError = new ErrorCreatingEntityException();

        when(techSkillNameService.create("Name 1", "logo".getBytes()))
                .thenThrow(creatingTechSkillNameError);

        // when
        var result = mockMvc.perform(multipart(TECH_SKILL_NAME_API_URL)
                .file(techSkillNameName)
                .file(techSkillNameLogo)
                .with(createAdminMockJwt()));

        // then
        verify(techSkillNameService, times(1)).create("Name 1", "logo".getBytes());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingTechSkillNameError.getMessage()));
    }

    @Test
    void shouldReturnTechSkillNameById() throws Exception {
        // given
        var techSkillNameId = 1;
        var techSkillName = createTechSkillNameReadDto(techSkillNameId);

        when(techSkillNameMapper.toReadDto(any()))
                .thenReturn(techSkillName);

        // when
        var result = mockMvc.perform(get(TECH_SKILL_NAME_API_URL + "/" + techSkillNameId)
                .with(jwt()));

        // then
        verify(techSkillNameService, times(1)).getById(techSkillNameId);
        verify(techSkillNameMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(techSkillName.id()))
                .andExpect(jsonPath("$.name").value(techSkillName.name()))
                .andExpect(jsonPath("$.logoUrl").value(techSkillName.logoUrl()));
    }

    @Test
    void shouldReturnErrorWhenTechSkillNameNotFound() throws Exception {
        // given
        var techSkillNameId = 1;
        var techSkillNameNotFoundError = new EntityNotFoundException(techSkillNameId);

        when(techSkillNameService.getById(techSkillNameId))
                .thenThrow(techSkillNameNotFoundError);

        // when
        var result = mockMvc.perform(get(TECH_SKILL_NAME_API_URL + "/" + techSkillNameId)
                .with(jwt()));

        // then
        verify(techSkillNameService, times(1)).getById(techSkillNameId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(techSkillNameNotFoundError.getMessage()));
    }

    @Test
    void shouldReturnModifiedTechSkillName() throws Exception {
        // given
        var techSkillNameId = 1;
        var techSkillNameToModifyName = new MockMultipartFile("name", "Name 2".getBytes());
        var techSkillNameToModifyLogo = new MockMultipartFile("logo", "logo".getBytes());
        var modifiedTechSkillName = createTechSkillNameReadDto(techSkillNameId);

        when(techSkillNameMapper.toReadDto(any()))
                .thenReturn(modifiedTechSkillName);

        // when
        var result = mockMvc.perform(multipart(PUT, TECH_SKILL_NAME_API_URL + "/" + techSkillNameId)
                .file(techSkillNameToModifyName)
                .file(techSkillNameToModifyLogo)
                .with(createAdminMockJwt()));

        // then
        verify(techSkillNameService, times(1)).update(techSkillNameId, "Name 2", "logo".getBytes());
        verify(techSkillNameMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedTechSkillName.id()))
                .andExpect(jsonPath("$.name").value(modifiedTechSkillName.name()))
                .andExpect(jsonPath("$.logoUrl").value(modifiedTechSkillName.logoUrl()));
    }

    @Test
    void shouldReturnErrorWhenEditedUserNotFound() throws Exception {
        // given
        var techSkillNameId = 1;
        var techSkillNameToModifyName = new MockMultipartFile("name", "Name 2".getBytes());
        var techSkillNameNotFoundError = new EntityNotFoundException(techSkillNameId);

        when(techSkillNameService.update(techSkillNameId, "Name 2", null))
                .thenThrow(techSkillNameNotFoundError);

        // when
        var result = mockMvc.perform(multipart(PUT, TECH_SKILL_NAME_API_URL + "/" + techSkillNameId)
                .file(techSkillNameToModifyName)
                .with(createAdminMockJwt()));

        // then
        verify(techSkillNameService, times(1)).update(techSkillNameId, "Name 2", null);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(techSkillNameNotFoundError.getMessage()));
    }

    @Test
    void shouldDeleteTechSkillName() throws Exception {
        // given
        var techSkillNameId = 1;

        // when
        var result = mockMvc.perform(delete(TECH_SKILL_NAME_API_URL + "/" + techSkillNameId)
                .with(createAdminMockJwt()));

        // then
        verify(techSkillNameService, times(1)).delete(techSkillNameId);

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(techSkillNameId))));
    }

    @Test
    void shouldReturnErrorWhenDeletedTechSkillNameNotFound() throws Exception {
        // given
        var techSkillNameId = 1;
        var techSkillNameNotFoundError = new EntityNotFoundException(techSkillNameId);

        doThrow(techSkillNameNotFoundError).when(techSkillNameService).delete(techSkillNameId);

        // when
        var result = mockMvc.perform(delete(TECH_SKILL_NAME_API_URL + "/" + techSkillNameId)
                .with(createAdminMockJwt()));

        // then
        verify(techSkillNameService, times(1)).delete(techSkillNameId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(techSkillNameNotFoundError.getMessage()));
    }

    @Test
    void shouldReturnErrorWhenErrorDeletingTechSkillNameLogo() throws Exception {
        // given
        var techSkillNameId = 1;
        var errorDeletingLogo = new ErrorDeletingFileException("logo");

        doThrow(errorDeletingLogo).when(techSkillNameService).delete(techSkillNameId);

        // when
        var result = mockMvc.perform(delete(TECH_SKILL_NAME_API_URL + "/" + techSkillNameId)
                .with(createAdminMockJwt()));

        // then
        verify(techSkillNameService, times(1)).delete(techSkillNameId);

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(errorDeletingLogo.getMessage()));
    }
}
