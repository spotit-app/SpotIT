package com.spotit.backend.foreignLanguage.controller;

import static com.spotit.backend.testUtils.ForeignLanguageNameUtils.createReadForeignLanguageNameDto;
import static com.spotit.backend.testUtils.ForeignLanguageNameUtils.generateForeignLanguageNameList;
import static com.spotit.backend.testUtils.GeneralUtils.getDeletedEntityMessage;
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

import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.exception.EntityNotFoundException;
import com.spotit.backend.abstraction.exception.ErrorCreatingEntityException;
import com.spotit.backend.foreignLanguage.mapper.ForeignLanguageNameMapper;
import com.spotit.backend.foreignLanguage.service.ForeignLanguageNameService;
import com.spotit.backend.storage.exception.ErrorDeletingFileException;
import com.spotit.backend.storage.exception.ErrorUploadingFileException;

@WebMvcTest(ForeignLanguageNameController.class)
@Import(SecurityConfig.class)
class ForeignLanguageNameControllerTest {

    static final String FOREIGN_LANGUAGE_NAME_API_URL = "/api/foreignLanguageName";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ForeignLanguageNameService foreignLanguageNameService;

    @MockBean
    ForeignLanguageNameMapper foreignLanguageNameMapper;

    @Test
    void shouldReturnListOfForeignLanguageNames() throws Exception {
        //given
        when(foreignLanguageNameService.getAll())
            .thenReturn(generateForeignLanguageNameList(3));
        when(foreignLanguageNameMapper.toReadDto(any()))
            .thenReturn(createReadForeignLanguageNameDto(1));

        //when
        var result = mockMvc.perform(get(FOREIGN_LANGUAGE_NAME_API_URL).with(jwt()));

        //then
        verify(foreignLanguageNameService, times(1)).getAll();
        verify(foreignLanguageNameMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedForeignLanguageName() throws Exception {
        // given
        var foreignLanguageNameName = new MockMultipartFile("name", "Name 1".getBytes());
        var foreignLanguageNameFlag = new MockMultipartFile("flag", "flag".getBytes());
        var createdForeignLanguageName = createReadForeignLanguageNameDto(1);

        when(foreignLanguageNameMapper.toReadDto(any()))
                .thenReturn(createdForeignLanguageName);

        // when
        var result = mockMvc.perform(
                multipart(FOREIGN_LANGUAGE_NAME_API_URL)
                        .file(foreignLanguageNameName)
                        .file(foreignLanguageNameFlag)
                        .with(jwt()));
        // then
        verify(foreignLanguageNameService, times(1)).create("Name 1", "flag".getBytes());
        verify(foreignLanguageNameMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdForeignLanguageName.id()))
                .andExpect(jsonPath("$.name").value(createdForeignLanguageName.name()))
                .andExpect(jsonPath("$.flagUrl").value(createdForeignLanguageName.flagUrl()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateForeignLanguageName() throws Exception {
        // given
        var foreignLanguageNameName = new MockMultipartFile("name", "Name 1".getBytes());
        var foreignLanguageNameFlag = new MockMultipartFile("flag", "flag".getBytes());
        var creatingForeignLanguageNameError = new ErrorCreatingEntityException();

        when(foreignLanguageNameService.create("Name 1", "flag".getBytes()))
                .thenThrow(creatingForeignLanguageNameError);

        // when
        var result = mockMvc.perform(multipart(FOREIGN_LANGUAGE_NAME_API_URL)
                .file(foreignLanguageNameName)
                .file(foreignLanguageNameFlag)
                .with(jwt()));

        // then
        verify(foreignLanguageNameService, times(1)).create("Name 1", "flag".getBytes());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingForeignLanguageNameError.getMessage()));
    }

    @Test
    void shouldReturnErrorWhenCantSaveForeignLanguageNameFlag() throws Exception {
        // given
        var foreignLanguageNameName = new MockMultipartFile("name", "Name 1".getBytes());
        var foreignLanguageNameFlag = new MockMultipartFile("flag", "flag".getBytes());
        var errorCreatingFile = new ErrorUploadingFileException("flag");

        when(foreignLanguageNameService.create("Name 1", "flag".getBytes()))
                .thenThrow(errorCreatingFile);

        // when
        var result = mockMvc.perform(multipart(FOREIGN_LANGUAGE_NAME_API_URL)
                .file(foreignLanguageNameName)
                .file(foreignLanguageNameFlag)
                .with(jwt()));

        // then
        verify(foreignLanguageNameService, times(1)).create("Name 1", "flag".getBytes());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(errorCreatingFile.getMessage()));
    }

    @Test
    void shouldReturnForeignLanguageNameById() throws Exception {
        // given
        var foreignLanguageNameId = 1;
        var foreignLanguageName = createReadForeignLanguageNameDto(foreignLanguageNameId);

        when(foreignLanguageNameMapper.toReadDto(any()))
                .thenReturn(foreignLanguageName);

        // when
        var result = mockMvc.perform(get(FOREIGN_LANGUAGE_NAME_API_URL + "/" + foreignLanguageNameId)
                .with(jwt()));

        // then
        verify(foreignLanguageNameService, times(1)).getById(foreignLanguageNameId);
        verify(foreignLanguageNameMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(foreignLanguageName.id()))
                .andExpect(jsonPath("$.name").value(foreignLanguageName.name()))
                .andExpect(jsonPath("$.flagUrl").value(foreignLanguageName.flagUrl()));
    }

    @Test
    void shouldReturnErrorWhenForeignLanguageNameNotFound() throws Exception {
        // given
        var foreignLanguageNameId = 1;
        var foreignLanguageNameNotFoundError = new EntityNotFoundException(foreignLanguageNameId);

        when(foreignLanguageNameService.getById(foreignLanguageNameId))
                .thenThrow(foreignLanguageNameNotFoundError);

        // when
        var result = mockMvc.perform(get(FOREIGN_LANGUAGE_NAME_API_URL + "/" + foreignLanguageNameId)
                .with(jwt()));

        // then
        verify(foreignLanguageNameService, times(1)).getById(foreignLanguageNameId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(foreignLanguageNameNotFoundError.getMessage()));
    }

    @Test
    void shouldReturnModifiedForeignLanguageName() throws Exception {
        // given
        var foreignLanguageNameId = 1;
        var foreignLanguageNameToModifyName = new MockMultipartFile("name", "Name 2".getBytes());
        var foreignLanguageNameToModifyFlag = new MockMultipartFile("flag", "flag2".getBytes());
        var modifiedForeignLanguageName = createReadForeignLanguageNameDto(foreignLanguageNameId);

        when(foreignLanguageNameMapper.toReadDto(any()))
                .thenReturn(modifiedForeignLanguageName);

        // when
        var result = mockMvc.perform(
                multipart(PUT, FOREIGN_LANGUAGE_NAME_API_URL + "/" + foreignLanguageNameId)
                        .file(foreignLanguageNameToModifyName)
                        .file(foreignLanguageNameToModifyFlag)
                        .with(jwt()));
        // then
        verify(foreignLanguageNameService, times(1)).update(1, "Name 2", "flag2".getBytes());
        verify(foreignLanguageNameMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedForeignLanguageName.id()))
                .andExpect(jsonPath("$.name").value(modifiedForeignLanguageName.name()))
                .andExpect(jsonPath("$.flagUrl").value(modifiedForeignLanguageName.flagUrl()));
    }

    @Test
    void shouldReturnErrorWhenEditedUserNotFound() throws Exception {
        // given
        var foreignLanguageNameId = 1;
        var foreignLanguageNameToModifyName = new MockMultipartFile("name", "Name 2".getBytes());
        var foreignLanguageNameNotFoundError = new EntityNotFoundException(foreignLanguageNameId);

        when(foreignLanguageNameService.update(1, "Name 2", null))
                .thenThrow(foreignLanguageNameNotFoundError);

        // when
        var result = mockMvc.perform(
                multipart(PUT, FOREIGN_LANGUAGE_NAME_API_URL + "/" + foreignLanguageNameId)
                        .file(foreignLanguageNameToModifyName)
                        .with(jwt()));

        // then
        verify(foreignLanguageNameService, times(1)).update(1, "Name 2", null);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(foreignLanguageNameNotFoundError.getMessage()));
    }

    @Test
    void shouldDeleteForeignLanguageName() throws Exception {
        // given
        var foreignLanguageNameId = 1;

        // when
        var result = mockMvc.perform(delete(FOREIGN_LANGUAGE_NAME_API_URL + "/" + foreignLanguageNameId)
                .with(jwt()));

        // then
        verify(foreignLanguageNameService, times(1)).delete(foreignLanguageNameId);

        result.andExpect(status().isOk())
                .andExpect(content().string(getDeletedEntityMessage(foreignLanguageNameId)));
    }

    @Test
    void shouldReturnErrorWhenDeletedForeignLanguageNameNotFound() throws Exception {
        // given
        var foreignLanguageNameId = 1;
        var foreignLanguageNameNotFoundError = new EntityNotFoundException(foreignLanguageNameId);

        doThrow(foreignLanguageNameNotFoundError).when(foreignLanguageNameService).delete(foreignLanguageNameId);

        // when
        var result = mockMvc.perform(delete(FOREIGN_LANGUAGE_NAME_API_URL + "/" + foreignLanguageNameId)
                .with(jwt()));

        // then
        verify(foreignLanguageNameService, times(1)).delete(foreignLanguageNameId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(foreignLanguageNameNotFoundError.getMessage()));
    }

    @Test
    void shouldReturnErrorWhenErrorDeletingForeignLanguageNameFlag() throws Exception {
        // given
        var foreignLanguageNameId = 1;
        var errorDeletingFlag = new ErrorDeletingFileException("flag");

        doThrow(errorDeletingFlag).when(foreignLanguageNameService).delete(foreignLanguageNameId);

        // when
        var result = mockMvc.perform(delete(FOREIGN_LANGUAGE_NAME_API_URL + "/" + foreignLanguageNameId)
                .with(jwt()));

        // then
        verify(foreignLanguageNameService, times(1)).delete(foreignLanguageNameId);

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(errorDeletingFlag.getMessage()));
    }
}
