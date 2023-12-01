package com.spotit.backend.userAccount.controller;

import static com.spotit.backend.utils.FileUtils.getBytesFromFile;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spotit.backend.userAccount.dto.ReadUserAccountDto;
import com.spotit.backend.userAccount.dto.WriteUserAccountDto;
import com.spotit.backend.userAccount.mapper.UserAccountMapper;
import com.spotit.backend.userAccount.model.UserAccount;
import com.spotit.backend.userAccount.service.UserAccountService;

@RestController
@RequestMapping("/api/userAccount")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final UserAccountMapper userAccountMapper;

    public UserAccountController(UserAccountService userAccountService, UserAccountMapper userAccountMapper) {
        this.userAccountService = userAccountService;
        this.userAccountMapper = userAccountMapper;
    }

    @GetMapping
    public List<ReadUserAccountDto> getAllUserAccounts() {
        return userAccountService.getAll()
                .stream()
                .map(userAccountMapper::toReadDto)
                .toList();
    }

    @PostMapping
    public ReadUserAccountDto createUserAccount(
            @RequestPart WriteUserAccountDto userData,
            @RequestPart String profilePictureUrl) {
        UserAccount userToCreate = userAccountMapper.fromWriteDto(userData);
        userToCreate.setProfilePictureUrl(profilePictureUrl);
        UserAccount createdUserAccount = userAccountService.create(userToCreate);

        return userAccountMapper.toReadDto(createdUserAccount);
    }

    @GetMapping("/{auth0Id}")
    public ReadUserAccountDto getUserAccountByAuth0Id(@PathVariable String auth0Id) {
        UserAccount foundUser = userAccountService.getByAuth0Id(auth0Id);

        return userAccountMapper.toReadDto(foundUser);
    }

    @PutMapping("/{auth0Id}")
    public ReadUserAccountDto editUserAccountByAuth0Id(
            @PathVariable String auth0Id,
            @RequestPart Optional<WriteUserAccountDto> userData,
            @RequestPart Optional<MultipartFile> profilePicture) {
        UserAccount userToEdit = userAccountMapper.fromWriteDto(userData.orElse(null));
        UserAccount editedUserAccount = userAccountService.update(
                auth0Id,
                userToEdit,
                getBytesFromFile(profilePicture.orElse(null)));

        return userAccountMapper.toReadDto(editedUserAccount);
    }

    @DeleteMapping("/{auth0Id}")
    public String deleteUserAccountByAuth0Id(@PathVariable String auth0Id) {
        userAccountService.delete(auth0Id);

        return "User with auth0Id '" + auth0Id + "' deleted.";
    }
}
