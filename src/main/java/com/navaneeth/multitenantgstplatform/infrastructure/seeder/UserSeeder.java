package com.navaneeth.multitenantgstplatform.infrastructure.seeder;

import com.navaneeth.multitenantgstplatform.feature.authentication.entity.User;
import com.navaneeth.multitenantgstplatform.feature.authentication.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(2)
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedSuperAdmin();
    }

    private void seedSuperAdmin() {

        if (userRepository.existsByUsername("superadmin")) {
            return;
        }

        User user = User.builder()
                .name("Super Admin")
                .email("admin@example.com")
                .username("superadmin")
                .password(passwordEncoder.encode("ChangeMe123!"))
                .active(true)
                .build();

        userRepository.save(user);
    }
}