package com.zygoo132.first_app.controllers;

import com.zygoo132.first_app.dtos.requests.RoleRequest;
import com.zygoo132.first_app.dtos.responses.ApiResponse;
import com.zygoo132.first_app.dtos.responses.RoleResponse;
import com.zygoo132.first_app.services.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest, WebRequest request) {
        RoleResponse roleResponse = roleService.create(roleRequest);
        return ApiResponse.success(roleResponse, request.getDescription(true));
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAllRoles(WebRequest request) {
        List<RoleResponse> roleResponses = roleService.getAll();
        return ApiResponse.<List<RoleResponse>>builder()
                .timestamp(LocalDateTime.now())
                .result(roleResponses)
                .path(request.getDescription(true))
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<RoleResponse> deleteRole(@PathVariable String role, WebRequest request) {
        roleService.delete(role);
        return ApiResponse.<RoleResponse>builder()
                .timestamp(LocalDateTime.now())
                .message("Role deleted successfully")
                .path(request.getDescription(true))
                .build();
    }
}
