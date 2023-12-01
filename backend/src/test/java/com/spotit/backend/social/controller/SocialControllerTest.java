package com.spotit.backend.social.controller;

import static com.spotit.backend.testUtils.GeneralUtils.getDeletedEntityMessage;
import static com.spotit.backend.testUtils.SocialUtils.createReadSocialDto;
import static com.spotit.backend.testUtils.SocialUtils.createWriteSocialDto;
import static com.spotit.backend.testUtils.SocialUtils.generateSocialsList;
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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.exception.EntityNotFoundException;
import com.spotit.backend.abstraction.exception.ErrorCreatingEntityException;
import com.spotit.backend.config.SecurityConfig;
import com.spotit.backend.social.mapper.SocialMapper;
import com.spotit.backend.social.service.SocialService;

@WebMvcTest(SocialController.class)
@Import(SecurityConfig.class)
class SocialControllerTest {

    static final String SOCIAL_API_URL = "/api/userAccount/auth0Id1/social";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SocialService socialService;

    @MockBean
    SocialMapper socialMapper;

    @Test
    void shouldReturnListOfUserSocials() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";

        when(socialService.getAllByUserAccountAuth0Id(userAuth0Id))
                .thenReturn(generateSocialsList(3));
        when(socialMapper.toReadDto(any()))
                .thenReturn(createReadSocialDto(1));

        // when
        var result = mockMvc.perform(get(SOCIAL_API_URL).with(jwt()));

        // then
        verify(socialService, times(1)).getAllByUserAccountAuth0Id(userAuth0Id);
        verify(socialMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedSocial() throws Exception {
        // given
        var socialToCreate = createWriteSocialDto(1);
        var createdSocial = createReadSocialDto(1);

        when(socialMapper.toReadDto(any()))
                .thenReturn(createdSocial);

        // when
        var result = mockMvc.perform(post(SOCIAL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(socialToCreate)));

        // then
        verify(socialMapper, times(1)).fromWriteDto(any());
        verify(socialService, times(1)).create(any(), any());
        verify(socialMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdSocial.id()))
                .andExpect(jsonPath("$.name").value(createdSocial.name()))
                .andExpect(jsonPath("$.socialUrl").value(createdSocial.socialUrl()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateSocial() throws Exception {
        // given
        var socialToCreate = createWriteSocialDto(1);
        var creatingSocialException = new ErrorCreatingEntityException();

        when(socialService.create(any(), any()))
                .thenThrow(creatingSocialException);

        // when
        var result = mockMvc.perform(post(SOCIAL_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(socialToCreate)));

        // then
        verify(socialMapper, times(1)).fromWriteDto(socialToCreate);
        verify(socialService, times(1)).create(any(), any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingSocialException.getMessage()));
    }

    @Test
    void shouldReturnSocialById() throws Exception {
        // given
        var socialId = 1;
        var socialToFind = createReadSocialDto(1);

        when(socialMapper.toReadDto(any()))
                .thenReturn(socialToFind);

        // when
        var result = mockMvc.perform(get(SOCIAL_API_URL + "/" + socialId).with(jwt()));

        // then
        verify(socialService, times(1)).getById(socialId);
        verify(socialMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(socialToFind.id()))
                .andExpect(jsonPath("$.name").value(socialToFind.name()))
                .andExpect(jsonPath("$.socialUrl").value(socialToFind.socialUrl()));
    }

    @Test
    void shouldReturnErrorWhenSocialNotFound() throws Exception {
        // given
        var socialId = 1;
        var socialNotFoundException = new EntityNotFoundException(socialId);

        when(socialService.getById(socialId))
                .thenThrow(socialNotFoundException);

        // when
        var result = mockMvc.perform(get(SOCIAL_API_URL + "/" + socialId).with(jwt()));

        // then
        verify(socialService, times(1)).getById(socialId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(socialNotFoundException.getMessage()));
    }

    @Test
    void shouldReturnModifiedSocial() throws Exception {
        // given
        var socialId = 1;
        var socialToModify = createWriteSocialDto(1);
        var modifiedSocial = createReadSocialDto(1);

        when(socialMapper.toReadDto(any())).thenReturn(modifiedSocial);

        // when
        var result = mockMvc.perform(put(SOCIAL_API_URL + "/" + socialId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(socialToModify)));

        // then
        verify(socialMapper, times(1)).fromWriteDto(any());
        verify(socialService, times(1)).update(any(), any());
        verify(socialMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedSocial.id()))
                .andExpect(jsonPath("$.name").value(modifiedSocial.name()))
                .andExpect(jsonPath("$.socialUrl").value(modifiedSocial.socialUrl()));
    }

    @Test
    void shouldReturnErrorWhenEditedSocialNotFound() throws Exception {
        // given
        var socialId = 1;
        var socialToModify = createWriteSocialDto(1);
        var socialNotFoundException = new EntityNotFoundException(socialId);

        when(socialService.update(any(), any()))
                .thenThrow(socialNotFoundException);

        // when
        var result = mockMvc.perform(put(SOCIAL_API_URL + "/" + socialId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(socialToModify)));

        // then
        verify(socialMapper, times(1)).fromWriteDto(any());
        verify(socialService, times(1)).update(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(socialNotFoundException.getMessage()));
    }

    @Test
    void shouldDeleteSocial() throws Exception {
        // given
        var socialId = 1;

        // when
        var result = mockMvc.perform(delete(SOCIAL_API_URL + "/" + socialId).with(jwt()));

        // then
        verify(socialService, times(1)).delete(socialId);

        result.andExpect(status().isOk())
                .andExpect(content().string(getDeletedEntityMessage(socialId)));
    }

    @Test
    void shouldReturnErrorWhenDeletedSocialNotFound() throws Exception {
        // given
        var socialId = 1;
        var socialNotFoundException = new EntityNotFoundException(socialId);

        doThrow(socialNotFoundException).when(socialService).delete(socialId);

        // when
        var result = mockMvc.perform(delete(SOCIAL_API_URL + "/" + socialId).with(jwt()));

        // then
        verify(socialService, times(1)).delete(socialId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(socialNotFoundException.getMessage()));
    }
}
