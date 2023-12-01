package com.spotit.backend.userAccount.api;

import static com.spotit.backend.testUtils.GeneralUtils.getDeletedUserMessage;
import static com.spotit.backend.testUtils.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.testUtils.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.testUtils.UserAccountUtils.createUserAccount;
import static com.spotit.backend.testUtils.UserAccountUtils.createWriteUserAccountDto;
import static com.spotit.backend.testUtils.UserAccountUtils.generateUserAccountList;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.spotit.backend.storage.service.StorageService;
import com.spotit.backend.userAccount.dto.WriteUserAccountDto;
import com.spotit.backend.userAccount.repository.UserAccountRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class UserAccountIntegrationTest {

    static final String USER_ACCOUNT_API_URL = "/api/userAccount";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StorageService storageService;

    @Test
    void shouldReturnAllUsers() throws Exception {
        // given
        var createdUsers = generateUserAccountList(3);
        userAccountRepository.saveAll(createdUsers);

        // when
        var result = mockMvc.perform(get(USER_ACCOUNT_API_URL).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(createdUsers.size()));
    }

    @Test
    void shouldReturnUserByAuth0Id() throws Exception {
        // given
        var createdUsers = generateUserAccountList(3);
        userAccountRepository.saveAll(createdUsers);

        // when
        var result = mockMvc.perform(get(USER_ACCOUNT_API_URL + "/" + createdUsers.get(1).getAuth0Id()).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.auth0Id").value(createdUsers.get(1).getAuth0Id()));
    }

    @Test
    void shouldReturnErrorWhenUserNotFound() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";

        // when
        var result = mockMvc.perform(get(USER_ACCOUNT_API_URL + "/" + userAuth0Id).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(userAuth0Id)));
    }

    @Test
    void shouldReturnCreatedUser() throws Exception {
        // given
        var userToCreate = createWriteUserAccountDto(1);
        var userToCreateData = new MockMultipartFile(
                "userData",
                "userData",
                "application/json",
                objectMapper.writeValueAsBytes(userToCreate));
        var profilePictureUrl = new MockMultipartFile(
                "profilePictureUrl",
                "http://profilePicture.url".getBytes());

        // when
        var result = mockMvc.perform(multipart(USER_ACCOUNT_API_URL)
                .file(userToCreateData)
                .file(profilePictureUrl)
                .with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.auth0Id").value(userToCreate.auth0Id()))
                .andExpect(jsonPath("$.firstName").value(userToCreate.firstName()))
                .andExpect(jsonPath("$.lastName").value(userToCreate.lastName()))
                .andExpect(jsonPath("$.email").value(userToCreate.email()))
                .andExpect(jsonPath("$.phoneNumber").value(userToCreate.phoneNumber()))
                .andExpect(jsonPath("$.profilePictureUrl").value("http://profilePicture.url"))
                .andExpect(jsonPath("$.position").value(userToCreate.position()))
                .andExpect(jsonPath("$.description").value(userToCreate.description()))
                .andExpect(jsonPath("$.cvClause").value(userToCreate.cvClause()));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingUser() throws Exception {
        // given
        var userToCreate = WriteUserAccountDto.builder().build();
        var userToCreateData = new MockMultipartFile(
                "userData",
                "userData",
                "application/json",
                objectMapper.writeValueAsBytes(userToCreate));
        var profilePictureUrl = new MockMultipartFile(
                "profilePictureUrl",
                "http://profilePicture.url".getBytes());

        // when
        var result = mockMvc.perform(multipart(USER_ACCOUNT_API_URL)
                .file(userToCreateData)
                .file(profilePictureUrl)
                .with(jwt()));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedUser() throws Exception {
        // given
        var existingUser = createUserAccount(1);
        var modifiedUser = WriteUserAccountDto.builder()
                .firstName("Name 2")
                .description("Description 2")
                .build();
        var modifiedUserData = new MockMultipartFile(
                "userData",
                "userData",
                "application/json",
                objectMapper.writeValueAsBytes(modifiedUser));
        userAccountRepository.save(existingUser);

        // when
        var result = mockMvc.perform(multipart(PUT, USER_ACCOUNT_API_URL + "/" + existingUser.getAuth0Id())
                .file(modifiedUserData)
                .with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.auth0Id").value(existingUser.getAuth0Id()))
                .andExpect(jsonPath("$.firstName").value(modifiedUser.firstName()))
                .andExpect(jsonPath("$.description").value(modifiedUser.description()));
    }

    @Test
    void shouldReturnErrorWhenEditedUserNotFound() throws Exception {
        // given
        var userToModify = createWriteUserAccountDto(1);
        var userAuth0Id = userToModify.auth0Id();
        var userToModifyData = new MockMultipartFile(
                "userData",
                "userData",
                "application/json",
                objectMapper.writeValueAsBytes(userToModify));

        // when
        var result = mockMvc.perform(multipart(PUT, USER_ACCOUNT_API_URL + "/" + userAuth0Id)
                .file(userToModifyData)
                .with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(userAuth0Id)));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        // given
        var userToDelete = createUserAccount(1);
        var userAuth0Id = userToDelete.getAuth0Id();
        userAccountRepository.save(userToDelete);

        // when
        var result = mockMvc.perform(delete(USER_ACCOUNT_API_URL + "/" + userAuth0Id).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().string(getDeletedUserMessage(userAuth0Id)));

        assertFalse(userAccountRepository.existsById(userToDelete.getId()));
    }

    @Test
    void shouldReturnErrorWhenDeletedUserNotFound() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";

        // when
        var result = mockMvc.perform(delete(USER_ACCOUNT_API_URL + "/" + userAuth0Id).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(userAuth0Id)));
    }
}
