package com.spotit.backend.employee.userDetails.foreignLanguage;

import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.employee.userDetails.foreignLanguage.ForeignLanguageUtils.createForeignLanguageReadDto;
import static com.spotit.backend.employee.userDetails.foreignLanguage.ForeignLanguageUtils.createForeignLanguageWriteDto;
import static com.spotit.backend.employee.userDetails.foreignLanguage.ForeignLanguageUtils.generateForeignLanguageList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.employee.abstraction.EntityNotFoundException;
import com.spotit.backend.employee.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.employee.referenceData.foreignLanguageName.ForeignLanguageNameService;

@WebMvcTest(ForeignLanguageController.class)
@Import(SecurityConfig.class)
class ForeignLanguageControllerTest {

    static final String FOREIGN_LANGUAGE_API_URL = "/api/userAccount/auth0Id1/foreignLanguage";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ForeignLanguageService foreignLanguageService;

    @MockBean
    ForeignLanguageNameService foreignLanguageNameService;

    @MockBean
    ForeignLanguageMapper foreignLanguageMapper;

    @Test
    void shouldReturnListOfForeignLanguages() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";

        when(foreignLanguageService.getAllByUserAccountAuth0Id(userAuth0Id))
                .thenReturn(generateForeignLanguageList(3));
        when(foreignLanguageMapper.toReadDto(any()))
                .thenReturn(createForeignLanguageReadDto(1));

        // when
        var result = mockMvc.perform(get(FOREIGN_LANGUAGE_API_URL).with(jwt()));

        // then
        verify(foreignLanguageService, times(1)).getAllByUserAccountAuth0Id(userAuth0Id);
        verify(foreignLanguageMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedForeignLanguage() throws Exception {
        // given
        var foreignLanguageToCreate = createForeignLanguageWriteDto(1);
        var createdForeignLanguage = createForeignLanguageReadDto(1);

        when(foreignLanguageMapper.toReadDto(any()))
                .thenReturn(createdForeignLanguage);

        // when
        var result = mockMvc.perform(post(FOREIGN_LANGUAGE_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(foreignLanguageToCreate)));

        // then
        verify(foreignLanguageMapper, times(1)).toReadDto(any());
        verify(foreignLanguageService, times(1)).create(any(), any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdForeignLanguage.id()))
                .andExpect(jsonPath("$.foreignLanguageName").value(createdForeignLanguage.foreignLanguageName()))
                .andExpect(jsonPath("$.languageLevel").value(createdForeignLanguage.languageLevel()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateForeignLanguage() throws Exception {
        // given
        var foreignLanguageToCreate = createForeignLanguageWriteDto(1);
        var creatingForeignLanguageError = new ErrorCreatingEntityException();

        when(foreignLanguageService.create(any(), any()))
                .thenThrow(creatingForeignLanguageError);

        // when
        var result = mockMvc.perform(post(FOREIGN_LANGUAGE_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(foreignLanguageToCreate)));

        // then
        verify(foreignLanguageService, times(1)).create(any(), any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingForeignLanguageError.getMessage()));
    }

    @Test
    void shouldReturnForeignLanguageById() throws Exception {
        // given
        var foreignLanguageId = 1;
        var foreignLanguage = createForeignLanguageReadDto(foreignLanguageId);

        when(foreignLanguageMapper.toReadDto(any()))
                .thenReturn(foreignLanguage);

        // when
        var result = mockMvc.perform(get(FOREIGN_LANGUAGE_API_URL + "/" + foreignLanguageId)
                .with(jwt()));

        // then
        verify(foreignLanguageService, times(1)).getById(foreignLanguageId);
        verify(foreignLanguageMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(foreignLanguage.id()))
                .andExpect(jsonPath("$.foreignLanguageName").value(foreignLanguage.foreignLanguageName()))
                .andExpect(jsonPath("$.languageLevel").value(foreignLanguage.languageLevel()));
    }

    @Test
    void shouldReturnErrorWhenForeignLanguageNotFound() throws Exception {
        // given
        var foreignLanguageId = 1;
        var foreignLanguageNotFoundError = new EntityNotFoundException(foreignLanguageId);

        when(foreignLanguageService.getById(foreignLanguageId))
                .thenThrow(foreignLanguageNotFoundError);

        // when
        var result = mockMvc.perform(get(FOREIGN_LANGUAGE_API_URL + "/" + foreignLanguageId)
                .with(jwt()));

        // then
        verify(foreignLanguageService, times(1)).getById(foreignLanguageId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(foreignLanguageNotFoundError.getMessage()));
    }

    @Test
    void shouldReturnModifiedForeignLanguage() throws Exception {
        // given
        var foreignLanguageId = 1;
        var modifiedForeignLanguage = createForeignLanguageReadDto(foreignLanguageId);
        var foreignLanguageToModify = ForeignLanguageWriteDto.builder()
                .languageLevel("5")
                .build();

        when(foreignLanguageMapper.toReadDto(any()))
                .thenReturn(modifiedForeignLanguage);

        // when
        var result = mockMvc.perform(put(FOREIGN_LANGUAGE_API_URL + "/" + foreignLanguageId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(foreignLanguageToModify)));

        // then
        verify(foreignLanguageService, times(1)).update(any(), any());
        verify(foreignLanguageMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedForeignLanguage.id()))
                .andExpect(jsonPath("$.foreignLanguageName").value(modifiedForeignLanguage.foreignLanguageName()))
                .andExpect(jsonPath("$.languageLevel").value(modifiedForeignLanguage.languageLevel()));
    }

    @Test
    void shouldReturnErrorWhenModifiedForeignLanguageNotFound() throws Exception {
        // given
        var foreignLanguageId = 1;
        var foreignLanguageNotFoundError = new EntityNotFoundException(foreignLanguageId);
        var foreignLanguageToModify = ForeignLanguageWriteDto.builder().languageLevel("5").build();

        when(foreignLanguageService.update(any(), any()))
                .thenThrow(foreignLanguageNotFoundError);

        // when
        var result = mockMvc.perform(put(FOREIGN_LANGUAGE_API_URL + "/" + foreignLanguageId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(foreignLanguageToModify)));

        // then
        verify(foreignLanguageService, times(1)).update(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(foreignLanguageNotFoundError.getMessage()));
    }

    @Test
    void shouldDeleteForeignLanguage() throws Exception {
        // given
        var foreignLanguageId = 1;

        // when
        var result = mockMvc.perform(delete(FOREIGN_LANGUAGE_API_URL + "/" + foreignLanguageId)
                .with(jwt()));

        // then
        verify(foreignLanguageService, times(1)).delete(foreignLanguageId);

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(foreignLanguageId))));
    }

    @Test
    void shouldReturnErrorWhenDeletedForeignLanguageNotFound() throws Exception {
        // given
        var foreignLanguageId = 1;
        var foreignLanguageNotFoundError = new EntityNotFoundException(foreignLanguageId);
        doThrow(foreignLanguageNotFoundError).when(foreignLanguageService).delete(foreignLanguageId);

        // when
        var result = mockMvc.perform(delete(FOREIGN_LANGUAGE_API_URL + "/" + foreignLanguageId)
                .with(jwt()));

        // then
        verify(foreignLanguageService, times(1)).delete(foreignLanguageId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(foreignLanguageNotFoundError.getMessage()));
    }
}
