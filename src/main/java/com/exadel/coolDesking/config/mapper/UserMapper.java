package com.exadel.coolDesking.config.mapper;

import com.exadel.coolDesking.user.User;
import com.exadel.coolDesking.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(target = "preferredWorkplace", ignore = true)
    @Mapping(target = "employmentStart", defaultExpression = "java(java.time.LocalDate.now())")
    @Mapping(target = "employmentEnd", defaultExpression = "java(java.time.LocalDate.now().plusDays(1))")
    @Mapping(target = "userState", defaultValue = "MAIN_MENU")
    User userDtoToUser(UserDto userDto);
}
