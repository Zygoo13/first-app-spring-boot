package com.zygoo132.first_app.mapper;

import com.zygoo132.first_app.dtos.requests.PermissionRequest;
import com.zygoo132.first_app.dtos.requests.UserCreationRequest;
import com.zygoo132.first_app.dtos.requests.UserUpdateRequest;
import com.zygoo132.first_app.dtos.responses.PermissionResponse;
import com.zygoo132.first_app.dtos.responses.UserResponse;
import com.zygoo132.first_app.entities.Permission;
import com.zygoo132.first_app.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {


    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

}
