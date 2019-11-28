package com.xebia.fs101.request;

import com.xebia.fs101.model.Article;

import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xebia.fs101.utils.StringUtils.slugify;

public class ArticleRequest {
    @NotBlank(message = "Title should not be null")
    private String title;
    @NotBlank(message = "Description should not be null")
    private String description;
    @NotBlank(message = "Body should not be null")
    private String body;
    private Set<String> tags;

    private ArticleRequest(Builder builder) {
        setTitle(builder.title);
        setDescription(builder.description);
        setBody(builder.body);
        setTags(builder.tags);
    }
    public ArticleRequest() {
    }

    public Article toArticle() {
        return new Article.Builder()
                .withTitle(this.title)
                .withDescription(this.description)
                .withBody(this.body)
                .withSlug(slugify(title))
                .withTagList(this.tags == null ? null
                        : this.tags.stream().
                        map(String::toLowerCase).
                        collect(Collectors.toSet()))
                .build();
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
    public static final class Builder {
        private
        @NotBlank(message = "Title should not be null") String title;
        private
        @NotBlank(message = "Description should not be null") String description;
        private
        @NotBlank(message = "Body should not be null") String body;
        private Set<String> tags;
        public Builder() {
        }

        public Builder withTitle(@NotBlank(message = "Title should not be null") String val) {
            title = val;
            return this;
        }
        public Builder withDescription(
                @NotBlank(message = "Description should not be null") String val) {
            description = val;
            return this;
        }

        public Builder withBody(@NotBlank(message = "Body should not be null") String val) {
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
}
