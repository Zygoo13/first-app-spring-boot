package com.zygoo132.first_app.controllers;


import com.nimbusds.jose.JOSEException;
import com.zygoo132.first_app.dtos.requests.AuthenticationRequest;
import com.zygoo132.first_app.dtos.requests.IntrospectRequest;
import com.zygoo132.first_app.dtos.responses.ApiResponse;
import com.zygoo132.first_app.dtos.responses.AuthenticationResponse;
import com.zygoo132.first_app.dtos.responses.IntrospectResponse;
import com.zygoo132.first_app.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticated(request);
        return ApiResponse.success(result, "User logged in successfully");
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.success(result, "Token introspection successful");
    }


}
