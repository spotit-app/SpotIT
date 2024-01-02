package com.spotit.backend.employee.userAccount;

import static com.spotit.backend.employee.userAccount.UserAccountUtils.createUserAccountReadDto;
import static com.spotit.backend.employee.userAccount.UserAccountUtils.createUserAccountWriteDto;
import static com.spotit.backend.employee.userAccount.UserAccountUtils.generateUserAccountList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.config.SecurityConfig;
import com.spotit.backend.employee.abstraction.EntityNotFoundException;
import com.spotit.backend.employee.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.storage.ErrorDeletingFileException;
import com.spotit.backend.storage.ErrorUploadingFileException;

@WebMvcTest(UserAccountController.class)
@Import(SecurityConfig.class)
class UserAccountControllerTest {

    static final String USER_ACCOUNT_API_URL = "/api/userAccount";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserAccountService userAccountService;

    @MockBean
    UserAccountMapper userAccountMapper;

    @Test
    void shouldReturnListOfUsers() throws Exception {
        // given
        when(userAccountService.getAll())
                .thenReturn(generateUserAccountList(3));
        when(userAccountMapper.toReadDto(any()))
                .thenReturn(createUserAccountReadDto(1));

        // when
        var result = mockMvc.perform(get(USER_ACCOUNT_API_URL).with(jwt()));

        // then
        verify(userAccountService, times(1)).getAll();
        verify(userAccountMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedUser() throws Exception {
        // given
        var userToCreate = UserAccountCreateDto.builder().build();
        var createdUser = createUserAccountReadDto(1);

        when(userAccountMapper.fromCreateDto(any()))
                .thenReturn(new UserAccount());
        when(userAccountMapper.toReadDto(any()))
                .thenReturn(createdUser);

        // when
        var result = mockMvc.perform(post(USER_ACCOUNT_API_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(userToCreate))
                .with(jwt()));

        // then
        verify(userAccountMapper, times(1)).fromCreateDto(any());
        verify(userAccountMapper, times(1)).toReadDto(any());
        verify(userAccountService, times(1)).create(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUser.id()))
                .andExpect(jsonPath("$.auth0Id").value(createdUser.auth0Id()))
                .andExpect(jsonPath("$.firstName").value(createdUser.firstName()))
                .andExpect(jsonPath("$.lastName").value(createdUser.lastName()))
                .andExpect(jsonPath("$.email").value(createdUser.email()))
                .andExpect(jsonPath("$.phoneNumber").value(createdUser.phoneNumber()))
                .andExpect(jsonPath("$.profilePictureUrl").value(createdUser.profilePictureUrl()))
                .andExpect(jsonPath("$.position").value(createdUser.position()))
                .andExpect(jsonPath("$.description").value(createdUser.description()))
                .andExpect(jsonPath("$.cvClause").value(createdUser.cvClause()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateUser() throws Exception {
        // given
        var userToCreate = UserAccountCreateDto.builder().build();
        var creatingUserError = new ErrorCreatingEntityException();

        when(userAccountMapper.fromCreateDto(any()))
                .thenReturn(new UserAccount());
        when(userAccountService.create(any()))
                .thenThrow(creatingUserError);

        // when
        var result = mockMvc.perform(post(USER_ACCOUNT_API_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(userToCreate))
                .with(jwt()));

        // then
        verify(userAccountMapper, times(1)).fromCreateDto(any());
        verify(userAccountService, times(1)).create(any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingUserError.getMessage()));
    }

    @Test
    void shouldReturnErrorWhenCantUploadProfilePicture() throws Exception {
        // given
        var userToCreate = new MockMultipartFile(
                "userData",
                "userData",
                "application/json",
                objectMapper.writeValueAsBytes(createUserAccountWriteDto(1)));
        var userProfilePicture = new MockMultipartFile(
                "profilePictureUrl",
                "http://profilePicture.url".getBytes());
        var errorUploadingPicture = new ErrorUploadingFileException("profilePicture");

        when(userAccountMapper.fromWriteDto(any()))
                .thenReturn(new UserAccount());
        when(userAccountService.update(any(), any(), any()))
                .thenThrow(errorUploadingPicture);

        // when
        var result = mockMvc.perform(multipart(PUT, USER_ACCOUNT_API_URL + "/" + "auth0Id1")
                .file(userToCreate)
                .file(userProfilePicture)
                .with(jwt()));

        // then
        verify(userAccountMapper, times(1)).fromWriteDto(any());
        verify(userAccountService, times(1)).update(any(), any(), any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(errorUploadingPicture.getMessage()));
    }

    @Test
    void shouldReturnUserAccountByAuth0Id() throws Exception {
        // given
        var userToFind = createUserAccountReadDto(1);
        var userAuth0Id = userToFind.auth0Id();

        when(userAccountMapper.toReadDto(any()))
                .thenReturn(userToFind);

        // when
        var result = mockMvc.perform(get(USER_ACCOUNT_API_URL + "/" + userAuth0Id).with(jwt()));

        // then
        verify(userAccountService, times(1)).getByAuth0Id(userAuth0Id);
        verify(userAccountMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userToFind.id()))
                .andExpect(jsonPath("$.auth0Id").value(userToFind.auth0Id()));
    }

    @Test
    void shouldReturnErrorWhenUserNotFound() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";
        var userNotFoundException = new EntityNotFoundException(userAuth0Id);
        when(userAccountService.getByAuth0Id(any()))
                .thenThrow(userNotFoundException);

        // when
        var result = mockMvc.perform(get(USER_ACCOUNT_API_URL + "/" + userAuth0Id).with(jwt()));

        // then
        verify(userAccountService, times(1)).getByAuth0Id(userAuth0Id);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(userNotFoundException.getMessage()));
    }

    @Test
    void shouldReturnModifiedUser() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";
        var userToModify = new MockMultipartFile(
                "userData",
                "userData",
                "application/json",
                objectMapper.writeValueAsBytes(createUserAccountWriteDto(1)));
        var userProfilePictureToModify = new MockMultipartFile(
                "profilePicture",
                "profilePicture".getBytes());
        var modifiedUser = createUserAccountReadDto(1);

        when(userAccountMapper.toReadDto(any())).thenReturn(modifiedUser);

        // when
        var result = mockMvc.perform(multipart(PUT, USER_ACCOUNT_API_URL + "/" + userAuth0Id)
                .file(userToModify)
                .file(userProfilePictureToModify)
                .with(jwt()));

        // then
        verify(userAccountMapper, times(1)).fromWriteDto(any());
        verify(userAccountService, times(1)).update(any(), any(), any());
        verify(userAccountMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedUser.id()))
                .andExpect(jsonPath("$.auth0Id").value(modifiedUser.auth0Id()));
    }

    @Test
    void shouldReturnErrorWhenEditedUserNotFound() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";
        var userToModify = new MockMultipartFile(
                "userData",
                "userData",
                "application/json",
                objectMapper.writeValueAsBytes(createUserAccountWriteDto(1)));
        var userProfilePictureToModify = new MockMultipartFile(
                "profilePicture",
                "profilePicture".getBytes());
        var userNotFoundException = new EntityNotFoundException(userAuth0Id);

        when(userAccountService.update(any(), any(), any()))
                .thenThrow(userNotFoundException);

        // when
        var result = mockMvc.perform(multipart(PUT, USER_ACCOUNT_API_URL + "/" + userAuth0Id)
                .file(userToModify)
                .file(userProfilePictureToModify)
                .with(jwt()));

        // then
        verify(userAccountService, times(1)).update(any(), any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(userNotFoundException.getMessage()));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";

        // when
        var result = mockMvc.perform(delete(USER_ACCOUNT_API_URL + "/" + userAuth0Id).with(jwt()));

        // then
        verify(userAccountService, times(1)).delete(userAuth0Id);

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(Map.of("id", userAuth0Id))));
    }

    @Test
    void shouldReturnErrorWhenDeletedUserNotFound() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";
        var userNotFoundException = new EntityNotFoundException(userAuth0Id);

        doThrow(userNotFoundException).when(userAccountService).delete(anyString());

        // when
        var result = mockMvc.perform(delete(USER_ACCOUNT_API_URL + "/" + userAuth0Id).with(jwt()));

        // then
        verify(userAccountService, times(1)).delete(userAuth0Id);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(userNotFoundException.getMessage()));
    }

    @Test
    void shouldReturnErrorWhenCantDeleteProfilePicture() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";
        var errorDeletingProfilePicture = new ErrorDeletingFileException("profilePicture");

        doThrow(errorDeletingProfilePicture).when(userAccountService).delete(anyString());

        // when
        var result = mockMvc.perform(delete(USER_ACCOUNT_API_URL + "/" + userAuth0Id).with(jwt()));

        // then
        verify(userAccountService, times(1)).delete(userAuth0Id);

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(errorDeletingProfilePicture.getMessage()));
    }
}
