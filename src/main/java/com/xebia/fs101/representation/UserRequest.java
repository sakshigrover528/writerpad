package com.xebia.fs101.representation;

import com.xebia.fs101.domain.User;
import com.xebia.fs101.domain.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserRequest {

    @NotBlank(message = "username can't be null")
    private String username;
    @NotBlank(message = "email can't be null")
    @Email
    private String email;
    @NotBlank(message = "password can't be empty")
    private String password;
    @NotNull
    private UserRole userRole;

    public UserRequest(String username, String email, String password, UserRole userRole) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User.Builder()
                .withUsername(this.username)
                .withEmail(this.email)
                .withPassword(passwordEncoder.encode(this.password))
                .withUserRole(this.userRole).build();
    }
}

