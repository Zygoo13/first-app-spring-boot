package com.zygoo132.first_app.mapper;


import com.zygoo132.first_app.dtos.requests.RoleRequest;
import com.zygoo132.first_app.dtos.responses.RoleResponse;
import com.zygoo132.first_app.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);

    RoleResponse toResponse(Role role);
}
