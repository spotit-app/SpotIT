package com.spotit.backend.testUtils;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.userAccount.dto.ReadUserAccountDto;
import com.spotit.backend.userAccount.dto.WriteUserAccountDto;
import com.spotit.backend.userAccount.model.UserAccount;

public interface UserAccountUtils {

    public static UserAccount createUserAccount(int id) {
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
                .build();
    }

    public static ReadUserAccountDto createReadUserAccountDto(int id) {
        return ReadUserAccountDto.builder()
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
                .build();
    }

    public static WriteUserAccountDto createWriteUserAccountDto(int id) {
        return WriteUserAccountDto.builder()
                .auth0Id("auth0Id" + id)
                .firstName("Name " + id)
                .lastName("Lastname " + id)
                .email("email@email" + id + ".com")
                .phoneNumber("phone" + id)
                .position("Position " + id)
                .description("Description " + id)
                .cvClause("CV clause " + id)
                .build();
    }

    public static List<UserAccount> generateUserAccountList(int length) {
        return IntStream.range(0, length)
                .mapToObj(UserAccountUtils::createUserAccount)
                .toList();
    }
}
