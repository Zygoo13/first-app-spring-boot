package com.zygoo132.first_app.services;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.zygoo132.first_app.dtos.requests.AuthenticationRequest;
import com.zygoo132.first_app.dtos.requests.IntrospectRequest;
import com.zygoo132.first_app.dtos.responses.AuthenticationResponse;
import com.zygoo132.first_app.dtos.responses.IntrospectResponse;
import com.zygoo132.first_app.entities.User;
import com.zygoo132.first_app.exceptions.AppException;
import com.zygoo132.first_app.exceptions.ErrorCode;
import com.zygoo132.first_app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;

    @NonFinal
    protected static final String SIGNING_KEY = "elPRxrlD0jfSXa+kaWpl8iEWA2msqi4hkf/Eu5oVhybSgxAFDhkPr42k0SNTOczM"; // 32 chars

    private String genarateToken(String username){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("Zygoo132")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("custom-claim", "custom-value")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNING_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot sign the token", e);
            throw new RuntimeException(e);
        }
    }

    public AuthenticationResponse authenticated(AuthenticationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTS));

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            var token = genarateToken(user.getUsername());
            return AuthenticationResponse.builder()
                    .isAuthenticated(true)
                    .token(token)
                    .build();
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNING_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .isActive(verified && new Date().before(expirationTime))
                .build();

    }
}
