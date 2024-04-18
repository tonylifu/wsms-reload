package com.lifu.wsms.reload.dto.response.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifu.wsms.reload.dto.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleResponse {
    private String name;
    private Set<Item> permissions;
}
