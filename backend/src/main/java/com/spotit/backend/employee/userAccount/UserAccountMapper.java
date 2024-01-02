package com.spotit.backend.employee.userAccount;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spotit.backend.employee.abstraction.EntityMapper;

@Mapper
public interface UserAccountMapper extends EntityMapper<UserAccount, UserAccountReadDto, UserAccountWriteDto> {

    @Override
    UserAccountReadDto toReadDto(UserAccount userAccount);

    UserAccountReadDataDto toReadUserDataDto(UserAccount userAccount);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profilePictureUrl", ignore = true)
    UserAccount fromWriteDto(UserAccountWriteDto writeUserAccountDto);

    @Mapping(target = "id", ignore = true)
    UserAccount fromCreateDto(UserAccountCreateDto userAccountCreateDto);
}
