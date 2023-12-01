package com.spotit.backend.testUtils;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.social.dto.ReadSocialDto;
import com.spotit.backend.social.dto.WriteSocialDto;
import com.spotit.backend.social.model.Social;

public interface SocialUtils {

    static Social createSocial(Integer id) {
        return Social.builder()
                .name("Name " + id)
                .socialUrl("socialUrl" + id)
                .build();
    }

    static ReadSocialDto createReadSocialDto(Integer id) {
        return ReadSocialDto.builder()
                .name("Name " + id)
                .socialUrl("socialUrl" + id)
                .build();
    }

    static WriteSocialDto createWriteSocialDto(Integer id) {
        return WriteSocialDto.builder()
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
