package com.xebia.fs101.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    @JsonBackReference
    @OneToMany(mappedBy = "user")
    private List<Article> articles;
    @Enumerated
    private UserRole userRole;

    private long countOfFollowers;
    private long countOfFollowing;
    @Transient
    private boolean isFollowing;

    @ManyToMany
    @JoinTable(name = "relation",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    private List<User> following = new ArrayList<>();

    @ManyToMany(mappedBy = "following")
    private List<User> followers = new ArrayList<>();

    public User() {

    }

    private User(Builder builder) {
        id = builder.id;
        username = builder.username;
        email = builder.email;
        password = builder.password;
        articles = builder.articles;
        userRole = builder.userRole;
        countOfFollowers = builder.countOfFollowers;
        countOfFollowing = builder.countOfFollowing;
        isFollowing = builder.isFollowing;
        following = builder.following;
        followers = builder.followers;
    }

    public User(User other) {
        this.id = other.id;
        this.username = other.username;
        this.email = other.email;
        this.password = other.password;
        this.articles = other.articles;
        this.userRole = other.userRole;
        this.countOfFollowers = other.countOfFollowers;
        this.countOfFollowing = other.countOfFollowing;
        this.isFollowing = other.isFollowing;
        this.following = other.following;
        this.followers = other.followers;
    }

    public Long getId() {
        return id;
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

    public List<Article> getArticles() {
        return articles;
    }

    public long getCountOfFollowers() {
        return countOfFollowers;
    }

    public void setCountOfFollowers(long countOfFollowers) {
        this.countOfFollowers = countOfFollowers;
    }

    public long getCountOfFollowing() {
        return countOfFollowing;
    }

    public void setCountOfFollowing(long countOfFollowing) {
        this.countOfFollowing = countOfFollowing;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.isFollowing = following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public void addFollower(User user) {
        if (this.checkIfFollowing(user))
            throw new IllegalArgumentException("Already Following");
        else {
            this.getFollowers().add(user);
            this.setCountOfFollowers(this.getFollowers().size());
            user.getFollowing().add(this);
            user.setCountOfFollowing(user.getFollowing().size());

        }

    }

    public void deleteFollower(User user) {
        if (!this.checkIfFollowing(user))
            throw new IllegalArgumentException("Already Unfollowed");
        else {
            this.getFollowers().remove(user);
            this.setCountOfFollowers(this.getFollowers().size());
            user.getFollowing().remove(this);
            user.setCountOfFollowing(user.getFollowing().size());
        }
    }

    public boolean checkIfFollowing(User user) {
        isFollowing = user.getFollowing().stream()
                .anyMatch(u -> this.getUsername().equals(u.getUsername()));
        return isFollowing;
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", email='" + email + '\''
                + ", password='" + password + '\''
                + '}';
    }

    public static final class Builder {
        private Long id;
        private String username;
        private String email;
        private String password;
        private List<Article> articles;
        private UserRole userRole;
        private long countOfFollowers;
        private long countOfFollowing;
        private boolean isFollowing;
        private List<User> following;
        private List<User> followers;

        public Builder() {
        }

        public Builder withId(Long val) {
            id = val;
            return this;
        }

        public Builder withUsername(String val) {
            username = val;
            return this;
        }

        public Builder withEmail(String val) {
            email = val;
            return this;
        }

        public Builder withPassword(String val) {
            password = val;
            return this;
        }

        public Builder withArticles(List<Article> val) {
            articles = val;
            return this;
        }

        public Builder withUserRole(UserRole val) {
            userRole = val;
            return this;
        }

        public Builder withFollowerCount(long val) {
            countOfFollowers = val;
            return this;
        }

        public Builder withFollowingCount(long val) {
            countOfFollowing = val;
            return this;
        }

        public Builder withFollowing(boolean val) {
            isFollowing = val;
            return this;
        }

        public Builder withFollowingUsers(List<User> val) {
            following = val;
            return this;
        }

        public Builder withFollowers(List<User> val) {
            followers = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
