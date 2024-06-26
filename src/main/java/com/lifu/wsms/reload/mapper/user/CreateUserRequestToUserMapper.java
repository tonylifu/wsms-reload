package com.lifu.wsms.reload.mapper.user;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.dto.Item;
import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.entity.user.User;
import com.lifu.wsms.reload.enums.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface CreateUserRequestToUserMapper {
    CreateUserRequestToUserMapper INSTANCE = Mappers.getMapper(CreateUserRequestToUserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "passwordSet", ignore = true)
//    @Mapping(target = "refreshTokens", ignore = true)
    @Mapping(target = "lastPasswordChangedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastUpdatedAt", ignore = true)
    @Mapping(target = "dob", source = "dob", dateFormat = "yyyy-MM-dd", qualifiedByName = "localDateStringToLong")
    @Mapping(target = "actionBy", expression = "java(mapUserFromSecurityContext())")
    @Mapping(target = "lastActionBy", expression = "java(mapUserFromSecurityContext())")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapUserRolesFromRequest")
    User toUser(CreateUserRequest createUserRequest);

    @Named("localDateStringToLong")
    default long mapLocalDateStringToLong(String date) {
        return AppUtil.convertLocalDateToLong(AppUtil.parseToLocalDate(date));
    }

    default String mapUserFromSecurityContext() {
        return AppUtil.getUserFromSecurityContext();
    }

    @Named("mapUserRolesFromRequest")
    default Set<Item> mapUserRolesFromRequest(Set<UserRole> roles) {
        if (roles == null) {
            return new HashSet<>();
        }
        return roles.stream()
                .map(role -> new Item(role.name()))
                .collect(Collectors.toSet());
    }
}