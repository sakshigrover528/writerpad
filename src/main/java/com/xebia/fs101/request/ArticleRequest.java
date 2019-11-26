package com.xebia.fs101.request;

import com.xebia.fs101.model.Article;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ArticleRequest {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @NotBlank(message = "Body cannot be blank")
    private String body;
    private Set<String> tags;
    private String featuredImage;

    public ArticleRequest() {
    }

    private ArticleRequest(Builder builder) {
        title = builder.title;
        description = builder.description;
        body = builder.body;
        tags = builder.tags;
        featuredImage = builder.featuredImage;
    }


    public static final class Builder {
        private @NotBlank(message = "Title cannot be blank") String title;
        private @NotBlank(message = "Description cannot be blank") String description;
        private @NotBlank(message = "Body cannot be blank") String body;
        private Set<String> tags;
        private String featuredImage;

        public Builder() {
        }

        public Builder withTitle(@NotBlank(message = "Title cannot be blank") String val) {
            title = val;
            return this;
        }

        public Builder withDescription(
                @NotBlank(message = "Description cannot be blank") String val) {
            description = val;
            return this;
        }

        public Builder withBody(@NotBlank(message = "Body cannot be blank") String val) {
            body = val;
            return this;
        }

        public Builder withTags(Set<String> val) {
            tags = val;
            return this;
        }

        public Builder withFeaturedImage(String val) {
            featuredImage = val;
            return this;
        }

        public ArticleRequest build() {
            return new ArticleRequest(this);
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public Article toArticle() {
        return new Article.Builder()
                .withTitle(this.title)
                .withDescription(this.description)
                .withBody(this.body)
                .withTags(Objects.nonNull(this.tags)
                        ? this.tags.stream().map(String::toLowerCase).
                        collect(Collectors.toSet()) : tags)
                .withCreatedAt(new Date())
                .withSlug(String.join("-", this.title.toLowerCase().split(" "))).build();
    }
}
