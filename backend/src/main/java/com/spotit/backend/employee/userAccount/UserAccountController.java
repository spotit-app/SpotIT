package com.spotit.backend.employee.userAccount;

import static com.spotit.backend.utils.FileUtils.getBytesFromFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public List<UserAccountReadDto> getAllUserAccounts() {
        return userAccountService.getAll()
                .stream()
                .map(userAccountMapper::toReadDto)
                .toList();
    }

    @GetMapping("/{auth0Id}")
    public UserAccountReadDto getUserAccountByAuth0Id(@PathVariable String auth0Id) {
        UserAccount foundUser = userAccountService.getByAuth0Id(auth0Id);

        return userAccountMapper.toReadDto(foundUser);
    }

    @PostMapping
    public UserAccountReadDto createUserAccount(
            @RequestBody UserAccountCreateDto userData) {
        UserAccount userToCreate = userAccountMapper.fromCreateDto(userData);
        UserAccount createdUserAccount = userAccountService.create(userToCreate);

        return userAccountMapper.toReadDto(createdUserAccount);
    }

    @PutMapping("/{auth0Id}")
    public UserAccountReadDto editUserAccountByAuth0Id(
            @PathVariable String auth0Id,
            @RequestPart Optional<UserAccountWriteDto> userData,
            @RequestPart Optional<MultipartFile> profilePicture) {
        UserAccount userToEdit = userAccountMapper.fromWriteDto(userData.orElse(null));
        UserAccount editedUserAccount = userAccountService.update(
                auth0Id,
                userToEdit,
                getBytesFromFile(profilePicture.orElse(null)));

        return userAccountMapper.toReadDto(editedUserAccount);
    }

    @DeleteMapping("/{auth0Id}")
    public Map<String, String> deleteUserAccountByAuth0Id(@PathVariable String auth0Id) {
        userAccountService.delete(auth0Id);

        return Map.of("id", auth0Id);
    }
}
