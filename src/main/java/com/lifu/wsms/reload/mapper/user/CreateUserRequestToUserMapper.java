package com.lifu.wsms.reload.mapper.user;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.entity.student.Student;
import com.lifu.wsms.reload.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreateUserRequestToUserMapper {
    CreateUserRequestToUserMapper INSTANCE = Mappers.getMapper(CreateUserRequestToUserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastUpdateAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dob", source = "dob", dateFormat = "yyyy-MM-dd", qualifiedByName = "localDateStringToLong")
    @Mapping(target = "actionBy", expression = "java(mapUserFromSecurityContext())")
    @Mapping(target = "lastActionBy", expression = "java(mapUserFromSecurityContext())")
    User toUser(CreateUserRequest createUserRequest);

    @Named("localDateStringToLong")
    default long mapLocalDateStringToLong(String dob) {
        return AppUtil.convertLocalDateToLong(AppUtil.parseToLocalDate(dob));
    }

    default String mapUserFromSecurityContext() {
        return AppUtil.getUserFromSecurityContext();
    }
}