package com.xebia.fs101.config;

import com.xebia.fs101.domain.User;
import com.xebia.fs101.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.xebia.fs101.domain.UserRole.ADMIN;

@Component
public class ApplicationStartupRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        User admin = userRepository.findByUsernameOrEmail("admin", "admin");
        if (Objects.equals(admin, null)) {
            User createdAdmin = new User.Builder()
                    .withUsername("admin")
                    .withEmail("admin@gmail.com")
                    .withPassword(passwordEncoder.encode("admin"))
                    .withUserRole(ADMIN).build();

            userRepository.save(createdAdmin);
        }
    }
}
