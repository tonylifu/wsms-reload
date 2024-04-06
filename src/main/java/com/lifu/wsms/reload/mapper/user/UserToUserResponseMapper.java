package com.lifu.wsms.reload.mapper.user;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.dto.response.user.UserResponse;
import com.lifu.wsms.reload.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import static com.lifu.wsms.reload.api.AppUtil.convertLongToLocalDate;
import static com.lifu.wsms.reload.api.AppUtil.getFullName;

@Mapper
public interface UserToUserResponseMapper {
    UserToUserResponseMapper INSTANCE = Mappers.getMapper(UserToUserResponseMapper.class);
    @Mapping(target = "dob", source = "dob", qualifiedByName = "longToLocalDateString")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "longToLocalDateString")
    @Mapping(target = "lastUpdatedAt", source = "lastUpdatedAt", qualifiedByName = "longToLocalDateString")
    @Mapping(target = "lastPasswordChangedAt", source = "lastPasswordChangedAt", qualifiedByName = "longToLocalDateString")
    @Mapping(target = "fullName", expression = "java(concatenateNamesToFullName(user))")
    UserResponse toUserResponse(User user);
    @Named("longToLocalDateString")
    default String mapLongToLocalDateString(long date) {
        return String.valueOf(convertLongToLocalDate(date));
    }

    @Named("concatenateNamesToFullName")
    default String concatenateNamesToFullName(User user) {
        return getFullName(user.getFirstName(), user.getMiddleName(), user.getLastName());
    }
}