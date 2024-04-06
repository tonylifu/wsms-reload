package com.lifu.wsms.reload.mapper.user;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.dto.response.user.UserResponse;
import com.lifu.wsms.reload.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import static com.lifu.wsms.reload.api.AppUtil.*;

@Mapper
public interface UserToUserResponseMapper {
    UserToUserResponseMapper INSTANCE = Mappers.getMapper(UserToUserResponseMapper.class);
    @Mapping(target = "dob", source = "dob", qualifiedByName = "longToLocalDateString")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "longToLocalDateTimeString")
    @Mapping(target = "lastUpdatedAt", source = "lastUpdatedAt", qualifiedByName = "longToLocalDateTimeString")
    @Mapping(target = "lastPasswordChangedAt", source = "lastPasswordChangedAt", qualifiedByName = "longToLocalDateTimeString")
    @Mapping(target = "fullName", expression = "java(concatenateNamesToFullName(user))")
    UserResponse toUserResponse(User user);
    @Named("longToLocalDateString")
    default String mapLongToLocalDateString(long dob) {
        return String.valueOf(convertLongToLocalDate(dob));
    }

    @Named("longToLocalDateTimeString")
    default String mapLongToLocalDateTimeString(long timeStamp) {
        return String.valueOf(convertLongToLocalDateTime(timeStamp));
    }

    @Named("concatenateNamesToFullName")
    default String concatenateNamesToFullName(User user) {
        return getFullName(user.getFirstName(), user.getMiddleName(), user.getLastName());
    }
}