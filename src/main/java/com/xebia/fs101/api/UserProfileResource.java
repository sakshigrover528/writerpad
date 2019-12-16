package com.xebia.fs101.api;

import com.xebia.fs101.domain.User;
import com.xebia.fs101.representation.UserProfileResponse;
import com.xebia.fs101.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api/profiles"))
public class UserProfileResource {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/{username}")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal User user,
                                       @PathVariable("username") final String username) {
        UserProfileResponse found = userService.getProfile(user, username);
        return new ResponseEntity<>(found, HttpStatus.OK);

    }

    @PostMapping(path = "/{username}/follow")
    public ResponseEntity<Void> followUser(@AuthenticationPrincipal User user,
                                           @PathVariable("username") String username) {
        this.userService.followUser(user, username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{username}/unfollow")
    public ResponseEntity<Void> unfavoriteArticle(@AuthenticationPrincipal User user,
                                                  @PathVariable("username") String username) {
        this.userService.unfollowUser(user, username);
        return ResponseEntity.noContent().build();
    }
}
