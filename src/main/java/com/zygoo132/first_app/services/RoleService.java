package com.zygoo132.first_app.services;


import com.zygoo132.first_app.dtos.requests.PermissionRequest;
import com.zygoo132.first_app.dtos.requests.RoleRequest;
import com.zygoo132.first_app.dtos.responses.PermissionResponse;
import com.zygoo132.first_app.dtos.responses.RoleResponse;
import com.zygoo132.first_app.entities.Permission;
import com.zygoo132.first_app.mapper.PermissionMapper;
import com.zygoo132.first_app.mapper.RoleMapper;
import com.zygoo132.first_app.repositories.PermissionRepository;
import com.zygoo132.first_app.repositories.RoleRepository;
import com.zygoo132.first_app.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return roleMapper.toResponse(role);
    }

    public List<RoleResponse> getAll(){
       var roles = roleRepository.findAll();
       return roles.stream().map(roleMapper::toResponse).toList();
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }






}
