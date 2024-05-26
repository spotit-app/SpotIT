package com.spotit.backend.domain.referenceData.workMode;

import java.util.List;
import java.util.stream.IntStream;

public interface WorkModeUtils {

    static WorkMode createWorkMode(int id) {
        return WorkMode.builder()
                .name("WorkMode " + id)
                .build();
    }

    static WorkModeReadDto createWorkModeReadDto(int id) {
        return WorkModeReadDto.builder()
                .name("WorkMode " + id)
                .build();
    }

    static WorkModeWriteDto createWorkModeWriteDto(int id) {
        return WorkModeWriteDto.builder()
                .name("WorkMode " + id)
                .build();
    }

    static List<WorkMode> generateWorkModeList(int length) {
        return IntStream.range(0, length)
                .mapToObj(WorkModeUtils::createWorkMode)
                .toList();
    }
}
