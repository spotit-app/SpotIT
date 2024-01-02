package com.spotit.backend.employee.userDetails.social;

import java.util.List;
import java.util.stream.IntStream;

public interface SocialUtils {

    static Social createSocial(Integer id) {
        return Social.builder()
                .name("Name " + id)
                .socialUrl("socialUrl" + id)
                .build();
    }

    static SocialReadDto createSocialReadDto(Integer id) {
        return SocialReadDto.builder()
                .name("Name " + id)
                .socialUrl("socialUrl" + id)
                .build();
    }

    static SocialWriteDto createSocialWriteDto(Integer id) {
        return SocialWriteDto.builder()
                .name("Name " + id)
                .socialUrl("socialUrl" + id)
                .build();
    }

    static List<Social> generateSocialsList(Integer length) {
        return IntStream.range(0, length)
                .mapToObj(SocialUtils::createSocial)
                .toList();
    }
}
