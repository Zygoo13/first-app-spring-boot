package com.zygoo132.first_app.configs;

import com.zygoo132.first_app.emums.Role; // enum
import com.zygoo132.first_app.entities.User;
import com.zygoo132.first_app.repositories.RoleRepository;
import com.zygoo132.first_app.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                // Tìm role ADMIN trong DB, nếu chưa có thì tạo mới
                var roleAdmin = roleRepository.findById(Role.ADMIN.name())
                        .orElseGet(() -> roleRepository.save(
                                com.zygoo132.first_app.entities.Role.builder()
                                        .name(Role.ADMIN.name())
                                        .description("Administrator role")
                                        .build()
                        ));

                var roles = new HashSet<com.zygoo132.first_app.entities.Role>();
                roles.add(roleAdmin);

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user created with username 'admin' and password 'admin'");
            }
        };
    }
}
