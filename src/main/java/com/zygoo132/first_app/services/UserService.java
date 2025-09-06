package com.zygoo132.first_app.services;

import com.zygoo132.first_app.dtos.requests.UserCreationRequest;
import com.zygoo132.first_app.dtos.requests.UserUpdateRequest;
import com.zygoo132.first_app.dtos.responses.UserResponse;
import com.zygoo132.first_app.entities.User;
import com.zygoo132.first_app.exceptions.AppException;
import com.zygoo132.first_app.exceptions.ErrorCode;
import com.zygoo132.first_app.mapper.UserMapper;
import com.zygoo132.first_app.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    // Create a new user
    public User createUser(UserCreationRequest request){

        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if(userRepository.existsByUsername(user.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        return userRepository.save(user);
    }

    // Get all users
    public List<UserResponse> getAllUsers(){
        return userMapper.toResponse(userRepository.findAll());
    }

    // Get user by id
    public User getUserEntityById(String id){
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    // Get user by id and return as response DTO
    public UserResponse getUserById(String id){
        return userMapper.toResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)) );
    }

    // Update user
    public UserResponse updateUser(String userId, UserUpdateRequest request){
        User user = getUserEntityById(userId);
        userMapper.updateUser(user, request);
        return userMapper.toResponse(userRepository.save(user));
    }

    // Delete user
    public void deleteUser(String userId){
        User user = getUserEntityById(userId);
        userRepository.delete(user);
    }


}

