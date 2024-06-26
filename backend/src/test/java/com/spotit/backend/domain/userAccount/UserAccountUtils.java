package com.spotit.backend.domain.userAccount;

import java.util.List;
import java.util.stream.IntStream;

public interface UserAccountUtils {

    static UserAccount createUserAccount(int id) {
        return UserAccount.builder()
                .auth0Id("auth0Id" + id)
                .firstName("Name " + id)
                .lastName("Lastname " + id)
                .email("email@email" + id + ".com")
                .phoneNumber("phone" + id)
                .profilePictureUrl("https://example.com/profile" + id + ".jpg")
                .position("Position " + id)
                .description("Description " + id)
                .cvClause("CV clause " + id)
                .isOpen(false)
                .build();
    }

    static UserAccountReadDto createUserAccountReadDto(int id) {
        return UserAccountReadDto.builder()
                .id(id)
                .auth0Id("auth0Id" + id)
                .firstName("Name " + id)
                .lastName("Lastname " + id)
                .email("email@email" + id + ".com")
                .phoneNumber("phone" + id)
                .profilePictureUrl("https://example.com/profile" + id + ".jpg")
                .position("Position " + id)
                .description("Description " + id)
                .cvClause("CV clause " + id)
                .isOpen(false)
                .build();
    }

    static UserAccountReadDataDto createUserAccountReadDataDto(int id) {
        return UserAccountReadDataDto.builder()
                .firstName("Name " + id)
                .lastName("Lastname " + id)
                .email("email@email" + id + ".com")
                .phoneNumber("phone" + id)
                .profilePictureUrl("https://example.com/profile" + id + ".jpg")
                .position("Position " + id)
                .description("Description " + id)
                .cvClause("CV clause " + id)
                .isOpen(false)
                .build();
    }

    static UserAccountWriteDto createUserAccountWriteDto(int id) {
        return UserAccountWriteDto.builder()
                .auth0Id("auth0Id" + id)
                .firstName("Name " + id)
                .lastName("Lastname " + id)
                .email("email@email" + id + ".com")
                .phoneNumber("phone" + id)
                .position("Position " + id)
                .description("Description " + id)
                .cvClause("CV clause " + id)
                .isOpen(false)
                .build();
    }

    static UserAccountCreateDto createUserAccountCreateDto(int id) {
        return UserAccountCreateDto.builder()
                .auth0Id("auth0Id" + id)
                .firstName("Name " + id)
                .lastName("Lastname " + id)
                .email("email@email" + id + ".com")
                .phoneNumber("phone" + id)
                .position("Position " + id)
                .description("Description " + id)
                .cvClause("CV clause " + id)
                .profilePictureUrl("http://profilePicture.url")
                .isOpen(false)
                .build();
    }

    static List<UserAccount> generateUserAccountList(int length) {
        return IntStream.range(0, length)
                .mapToObj(UserAccountUtils::createUserAccount)
                .toList();
    }
}
