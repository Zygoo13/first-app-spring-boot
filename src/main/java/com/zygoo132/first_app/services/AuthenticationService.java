package com.zygoo132.first_app.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signing-key}")
    String signingKey;

    /** Generate JWT token */
    private String generateToken(User user) {
        try {
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("Zygoo132")
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                    .claim("scope", buildScope(user))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new com.nimbusds.jose.JWSHeader(JWSAlgorithm.HS512),
                    claims
            );

            signedJWT.sign(new MACSigner(signingKey.getBytes()));
            return signedJWT.serialize();

        } catch (JOSEException e) {
            log.error("Cannot sign the token", e);
            throw new RuntimeException("Error while generating token", e);
        }
    }

    /** Authenticate user and return token */
    public AuthenticationResponse authenticated(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTS));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = generateToken(user);
            return AuthenticationResponse.builder()
                    .isAuthenticated(true)
                    .token(token)
                    .build();
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    /** Validate token (introspection endpoint) */
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(request.getToken());
        var verifier = new MACVerifier(signingKey.getBytes());

        boolean verified = signedJWT.verify(verifier);
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();

        return IntrospectResponse.builder()
                .isActive(verified && new Date().before(expiration))
                .build();
    }

    /** Build scope string from roles */
    private String buildScope(User user) {
        if (CollectionUtils.isEmpty(user.getRoles())) return "";
        StringJoiner scopes = new StringJoiner(" ");
        user.getRoles().forEach(scopes::add);
        return scopes.toString();
    }
}
