package com.zygoo132.first_app.services;


import com.zygoo132.first_app.dtos.requests.PermissionRequest;
import com.zygoo132.first_app.dtos.responses.PermissionResponse;
import com.zygoo132.first_app.entities.Permission;
import com.zygoo132.first_app.mapper.PermissionMapper;
import com.zygoo132.first_app.repositories.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request); // map request to entity
        permission = permissionRepository.save(permission); // save entity
        return permissionMapper.toPermissionResponse(permission); // map entity to response
    }

    public List<PermissionResponse> getAll(){
       var permissions = permissionRepository.findAll(); // get all permissions
       return permissions.stream().map(permissionMapper::toPermissionResponse).toList(); // map entities to responses
    }

    public void delete(String permissionName){
        permissionRepository.deleteById(permissionName); // delete permission by name
    }




}
