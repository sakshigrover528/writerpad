package com.xebia.fs101.service;

import com.xebia.fs101.domain.User;
import com.xebia.fs101.exception.UserNotFoundException;
import com.xebia.fs101.repository.UserRepository;
import com.xebia.fs101.representation.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.xebia.fs101.representation.UserProfileResponse.from;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User save(User user) {

        return userRepository.save(user);

    }

    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public UserProfileResponse getProfile(User user, String username) {
        User currentUser = userRepository.findById(user.getId()).get();
        User foundUserProfile = findByUsername(username);
        foundUserProfile.checkIfFollowing(currentUser);
        return from(foundUserProfile);
    }

    public void followUser(User user, String username) {
        User current = userRepository.findById(user.getId()).get();
        User toBeFollowed = findByUsername(username);
        toBeFollowed.addFollower(current);
        userRepository.save(toBeFollowed);
        userRepository.save(current);
    }

    public void unfollowUser(User user, String username) {
        User currentUser = userRepository.findById(user.getId()).get();
        User userToBeUnfollowed = findByUsername(username);
        userToBeUnfollowed.deleteFollower(currentUser);
        userRepository.save(userToBeUnfollowed);
        userRepository.save(currentUser);
    }
}
