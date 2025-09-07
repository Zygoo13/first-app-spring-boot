package com.zygoo132.first_app.services;

import com.zygoo132.first_app.dtos.requests.UserCreationRequest;
import com.zygoo132.first_app.dtos.requests.UserUpdateRequest;
import com.zygoo132.first_app.dtos.responses.UserResponse;
import com.zygoo132.first_app.emums.Role;
import com.zygoo132.first_app.entities.User;
import com.zygoo132.first_app.exceptions.AppException;
import com.zygoo132.first_app.exceptions.ErrorCode;
import com.zygoo132.first_app.mapper.UserMapper;
import com.zygoo132.first_app.repositories.RoleRepository;
import com.zygoo132.first_app.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public User createUser(UserCreationRequest request){
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Kiểm tra username trùng
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        // Lấy Role entity từ DB
        var roleUser = roleRepository.findById(Role.USER.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        var roles = new HashSet<com.zygoo132.first_app.entities.Role>();
        roles.add(roleUser);
        user.setRoles(roles);

        return userRepository.save(user);
    }


    // Get all users
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers(){
        log.info("In method getAllUsers of UserService");
        return userMapper.toResponse(userRepository.findAll());
    }

    // Get user by id
    public User getUserEntityById(String id){
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    // Get user by id and return as response DTO
    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    public UserResponse getUserById(String id){
        log.info("In method getUserById of UserService");
        return userMapper.toResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)) );
    }

    // Update user
    public UserResponse updateUser(String userId, UserUpdateRequest request){
        User user = getUserEntityById(userId);
        userMapper.updateUser(user, request);
        if(request.getPassword() != null && !request.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        var role = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(role));

        return userMapper.toResponse(userRepository.save(user));
    }

    // Delete user
    public void deleteUser(String userId){
        User user = getUserEntityById(userId);
        userRepository.delete(user);
    }

    //get my profile
    public UserResponse getMyProfile(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTS));
        return userMapper.toResponse(user);
    }


}

