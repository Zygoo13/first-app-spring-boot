package com.zygoo132.first_app.controllers;


import com.zygoo132.first_app.dtos.requests.PermissionRequest;
import com.zygoo132.first_app.dtos.responses.ApiResponse;
import com.zygoo132.first_app.dtos.responses.PermissionResponse;
import com.zygoo132.first_app.services.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/permissions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest, WebRequest request){
        PermissionResponse permissionResponse = permissionService.create(permissionRequest);
        return ApiResponse.success(permissionResponse, request.getDescription(true));
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermissions(WebRequest request){
        List<PermissionResponse> permissionResponses = permissionService.getAll();
        return ApiResponse.<List<PermissionResponse>>builder()
                .timestamp(LocalDateTime.now())
                .result(permissionResponses)
                .path(request.getDescription(true))
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<PermissionResponse> deletePermission(@PathVariable String permission, WebRequest request){
        permissionService.delete(permission);
        return ApiResponse.<PermissionResponse>builder()
                .timestamp(LocalDateTime.now())
                .message("Permission deleted successfully")
                .path(request.getDescription(true))
                .build();
    }
    
}
