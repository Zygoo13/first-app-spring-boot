package com.zygoo132.first_app.controllers;

import com.zygoo132.first_app.dtos.requests.UserCreationRequest;
import com.zygoo132.first_app.dtos.requests.UserUpdateRequest;
import com.zygoo132.first_app.dtos.responses.ApiResponse;
import com.zygoo132.first_app.dtos.responses.UserResponse;
import com.zygoo132.first_app.mapper.UserMapper;
import com.zygoo132.first_app.services.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @RequestBody @Valid UserCreationRequest userCreationRequest,
            WebRequest request) {
        UserResponse user = userMapper.toResponse(userService.createUser(userCreationRequest));
        return ResponseEntity.ok(ApiResponse.success(user, request.getDescription(false)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(WebRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());

        authentication.getAuthorities().forEach(authority -> {
            log.info("Authority: {}", authority.getAuthority());
        });

        List<UserResponse> userResponses = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(userResponses, request.getDescription(false)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable String userId,
            WebRequest request) {
        UserResponse userResponse = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(userResponse, request.getDescription(false)));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable String userId,
            @RequestBody @Valid UserUpdateRequest userUpdateRequest,
            WebRequest request) {
        UserResponse updatedUser = userService.updateUser(userId, userUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, request.getDescription(false)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @PathVariable String userId,
            WebRequest request) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success(
                "User with ID " + userId + " has been deleted.",
                request.getDescription(false)
        ));
    }

    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(WebRequest request) {
        var user = userService.getMyProfile();
        return ResponseEntity.ok(ApiResponse.success(user, request.getDescription(false)));
    }
}
