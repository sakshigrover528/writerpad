package com.xebia.fs101.representation;

import com.xebia.fs101.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserRequest {

    @NotBlank(message = "username can't be null")
    private String username;
    @NotBlank(message = "email can't be null")
    @Email
    private String email;
    @NotBlank(message = "password can't be empty")
    private String password;

    public UserRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
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

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(this.username, this.email, passwordEncoder.encode(this.password));
    }

}

