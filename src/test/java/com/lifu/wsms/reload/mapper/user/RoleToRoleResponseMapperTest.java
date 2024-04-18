package com.lifu.wsms.reload.mapper.user;

import com.lifu.wsms.reload.dto.Item;
import com.lifu.wsms.reload.dto.response.user.RoleResponse;
import com.lifu.wsms.reload.util.UserTestUtil;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoleToRoleResponseMapperTest {

    @Test
    void toRoleResponse() {
        var roleEntity = UserTestUtil.getRoleEntity();
        RoleResponse roleResponse = RoleToRoleResponseMapper.INSTANCE.toRoleResponse(roleEntity);

        Set<Item> entityPermissions = roleEntity.getPermissions();
        Set<Item> responsePermissions = roleResponse.getPermissions();

        assertEquals(roleEntity.getName(), roleResponse.getName());
        assertEquals(entityPermissions.size(), responsePermissions.size());
    }
}