package com.exadel.coolDesking.config.mapper;

import com.exadel.coolDesking.user.User;
import com.exadel.coolDesking.user.UserDto;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import javax.annotation.processing.Generated;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(target = "preferredWorkplace", ignore = true)
    User userDtoToUser(UserDto userDto);
}
