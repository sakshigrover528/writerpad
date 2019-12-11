package com.xebia.fs101.service;

import com.xebia.fs101.model.User;
import com.xebia.fs101.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User save(User user) {

        return userRepository.save(user);

    }

}
