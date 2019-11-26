package com.xebia.fs101.model;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String slug;
    private String title;
    private String description;
    private String body;
    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "id"))
    private Set<String> tags;
    private Date createdAt;
    private Date updatedAt;
    private boolean favorited;
    private int favoritesCount;
    private String featuredImage;

    private Article(Builder builder) {
        id = builder.id;
        slug = builder.slug;
        title = builder.title;
        description = builder.description;
        body = builder.body;
        tags = builder.tags;
        createdAt = builder.createdAt;
        updatedAt = builder.updatedAt;
        favorited = builder.favorited;
        favoritesCount = builder.favoritesCount;
        featuredImage = builder.featuredImage;
    }


    public static final class Builder {
        private UUID id;
        private String slug;
        private String title;
        private String description;
        private String body;
        private Set<String> tags;
        private Date createdAt;
        private Date updatedAt;
        private boolean favorited;
        private int favoritesCount;
        private String featuredImage;

        public Builder() {
        }

        public Builder withId(UUID val) {
            id = val;
            return this;
        }

        public Builder withSlug(String val) {
            slug = val;
            return this;
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

        public Builder withCreatedAt(Date val) {
            createdAt = val;
            return this;
        }

        public Builder withUpdatedAt(Date val) {
            updatedAt = val;
            return this;
        }

        public Builder withFavorited(boolean val) {
            favorited = val;
            return this;
        }

        public Builder withFavoritesCount(int val) {
            favoritesCount = val;
            return this;
        }

        public Builder withFeaturedImage(String val) {
            featuredImage = val;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    @Override
    public String toString() {
        return "Article{"
                + "id=" + id
                + ", slug='" + slug + '\''
                + ", title='" + title + '\''
                + ", description='" + description + '\''
                + ", body='" + body + '\''
                + ", tags='" + tags + '\''
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + ", favorited=" + favorited
                + ", favoritesCount=" + favoritesCount
                + ", featuredImage='" + featuredImage + '\''
                + '}';
    }

}
