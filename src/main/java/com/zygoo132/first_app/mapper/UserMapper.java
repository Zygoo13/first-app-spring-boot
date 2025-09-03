package com.zygoo132.first_app.mapper;

import com.zygoo132.first_app.dtos.requests.UserCreationRequest;
import com.zygoo132.first_app.dtos.requests.UserUpdateRequest;
import com.zygoo132.first_app.dtos.responses.UserResponse;
import com.zygoo132.first_app.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toResponse(User user);

    List<UserResponse> toResponse(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
