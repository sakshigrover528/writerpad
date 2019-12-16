package com.xebia.fs101.representation;

import com.xebia.fs101.domain.Article;
import com.xebia.fs101.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserProfileResponse {
    private String username;
    private boolean isFollowing;
    private long countOfFollowers;
    private long countOfFollowing;
    private List<ArticleResponse> articles;


    public UserProfileResponse() {
    }

    private UserProfileResponse(Builder builder) {
        username = builder.username;
        isFollowing = builder.isFollowing;
        countOfFollowers = builder.countOfFollowers;
        countOfFollowing = builder.countOfFollowing;
        articles = builder.articleResponses;
    }

    public static UserProfileResponse from(User user) {
        return new UserProfileResponse.Builder()
                .withUsername(user.getUsername())
                .withFollowing(user.isFollowing())
                .withCountOfFollowers(user.getCountOfFollowers())
                .withCountOfFollowing(user.getCountOfFollowing())
                .withArticles(user.getArticles())
                .build();
    }

    public String getUsername() {
        return username;
    }

    public boolean isFollowing() {

        return isFollowing;
    }

    public long getCountOfFollowers() {
        return countOfFollowers;
    }

    public long getCountOfFollowing() {
        return countOfFollowing;
    }

    public List<ArticleResponse> getArticles() {
        return articles;
    }

    public static class ArticleResponse {
        private String id;
        private String title;

        public ArticleResponse(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }

    public static final class Builder {
        private String username;
        private boolean isFollowing;
        private long countOfFollowers;
        private long countOfFollowing;
        private List<Article> articles;
        private String id;
        private String title;
        private List<ArticleResponse> articleResponses = new ArrayList<>();

        public Builder() {
        }

        public Builder withUsername(String val) {
            username = val;
            return this;
        }

        public Builder withFollowing(boolean val) {
            isFollowing = val;
            return this;
        }

        public Builder withCountOfFollowers(long val) {
            countOfFollowers = val;
            return this;
        }

        public Builder withCountOfFollowing(long val) {
            countOfFollowing = val;
            return this;
        }

        public Builder withArticles(List<Article> val) {
            articles = val;
            articles.forEach(article -> {
                        articleResponses.add(new ArticleResponse(
                                article.getSlug() + "-" + article.getId(),
                                article.getTitle()));

                    }
            );
            withArticleResponses(articleResponses);
            return this;
        }

        public Builder withArticleResponses(List<ArticleResponse> val) {
            articleResponses = val;
            return this;
        }

        public UserProfileResponse build() {
            return new UserProfileResponse(this);
        }
    }
}
