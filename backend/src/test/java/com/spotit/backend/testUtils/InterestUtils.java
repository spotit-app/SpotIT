package com.spotit.backend.testUtils;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.interest.dto.ReadInterestDto;
import com.spotit.backend.interest.model.Interest;

public interface InterestUtils {

    static Interest createInterest(Integer id) {
        return Interest.builder()
                .name("Name " + id)
                .build();
    }

    static ReadInterestDto createReadInterestDto(Integer id) {
        return ReadInterestDto.builder()
                .id(id)
                .name("Name " + id)
                .build();
    }

    static List<Interest> generateInterestList(int length) {
        return IntStream.range(0, length)
                .mapToObj(InterestUtils::createInterest)
                .toList();
    }
}
