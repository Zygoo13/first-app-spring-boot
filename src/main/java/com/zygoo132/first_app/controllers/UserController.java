package com.zygoo132.first_app.controllers;

import com.zygoo132.first_app.dtos.requests.UserCreationRequest;
import com.zygoo132.first_app.dtos.requests.UserUpdateRequest;
import com.zygoo132.first_app.dtos.responses.ApiResponse;
import com.zygoo132.first_app.entities.User;
import com.zygoo132.first_app.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(
            @RequestBody @Valid UserCreationRequest userCreationRequest,
            WebRequest request) {
        User user = userService.createRequest(userCreationRequest);
        return ResponseEntity.ok(ApiResponse.success(user, request.getDescription(false)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(WebRequest request) {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users, request.getDescription(false)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getUserById(
            @PathVariable String userId,
            WebRequest request) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user, request.getDescription(false)));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable String userId,
            @RequestBody @Valid UserUpdateRequest userUpdateRequest,
            WebRequest request) {
        User updatedUser = userService.updateUser(userId, userUpdateRequest);
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
}
