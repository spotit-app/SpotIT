package com.spotit.backend.employee.userDetails.interest;

import java.util.List;
import java.util.stream.IntStream;

public interface InterestUtils {

    static Interest createInterest(Integer id) {
        return Interest.builder()
                .name("Name " + id)
                .build();
    }

    static InterestReadDto createInterestReadDto(Integer id) {
        return InterestReadDto.builder()
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
