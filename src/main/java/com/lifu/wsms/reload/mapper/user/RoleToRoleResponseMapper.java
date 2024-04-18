package com.lifu.wsms.reload.mapper.user;

import com.lifu.wsms.reload.dto.response.user.RoleResponse;
import com.lifu.wsms.reload.entity.user.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleToRoleResponseMapper {
    RoleToRoleResponseMapper INSTANCE = Mappers.getMapper(RoleToRoleResponseMapper.class);

    RoleResponse toRoleResponse(Role role);
}
