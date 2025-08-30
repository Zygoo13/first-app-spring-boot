package com.zygoo132.first_app.dtos.requests;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    @Size(min = 1, message = "First name must not be empty")
    private String firstName;
    @Size(min = 1, message = "Last name must not be empty")
    private String lastName;
    private String dob;
}
