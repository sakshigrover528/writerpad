package com.xebia.fs101.representation;

import com.xebia.fs101.domain.Article;
import com.xebia.fs101.domain.Status;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

import static com.xebia.fs101.domain.Status.DRAFT;


public class ArticleRequest {

    @NotNull(message = "Title can't be null")
    @NotEmpty(message = "Title can't be empty")
    private String title;
    @NotNull(message = "Description can't be null")
    @NotEmpty(message = "Description can't be empty")
    private String description;
    @NotNull(message = "Body can't be null")
    @NotEmpty(message = "Body can't be empty")
    private String body;
    private Set<String> tags;
    private Status status;


    public ArticleRequest(String title, String description,
                          String body, Set<String> tags, Status status) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.tags = tags;
        this.status = status;

    }

    private ArticleRequest(Builder builder) {
        setTitle(builder.title);
        setDescription(builder.description);
        setBody(builder.body);
        setTags(builder.tags);
        setStatus(builder.status);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Article toArticle() {
        if (!Objects.nonNull(status))
            this.status = DRAFT;
        return new Article.Builder().withTitle(this.title)
                .withDescription(this.description)
                .withBody(this.body)
                .withTags(this.tags)
                .withStatus(this.status)
                .build();

    }

    public static final class Builder {
        public Status status;
        private String title;
        private String description;
        private String body;
        private Set<String> tags;

        public Builder() {
        }

        public Builder withTitle(String val) {
            title = val;
            return this;
        }

        public Builder withDescription(String val) {
            description = val;
            return this;
        }

        public Builder withBody(String val) {
            body = val;
            return this;
        }

        public Builder withTags(Set<String> val) {
            tags = val;
            return this;
        }

        public ArticleRequest build() {
            return new ArticleRequest(this);
        }
    }

    @Override
    public String toString() {
        return "ArticleRequest{"
                + "title='"
                + title
                + '\''
                + ", description='" + description + '\''
                + ", body='" + body + '\''
                + ", tags=" + tags
                + '}';
    }
}
