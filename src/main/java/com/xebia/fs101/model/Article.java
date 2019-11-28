package com.xebia.fs101.model;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.xebia.fs101.utils.StringUtils.slugify;

@Entity(name = "Article")
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Transient
    private String slug;
    private String title;
    private String description;
    @Column(length = 4000)
    private String body;
    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "id"))
    private Set<String> tagList;
    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    @Column(name = "updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "favorites")
    private Boolean favorite;
    @Column(name = "favorites_count")
    private int favoritesCount;
    @OneToMany
    @JoinColumn(name = "article")
    private List<Comment> comments;
    public Article() {
    }
    private Article(Builder builder) {
        id = builder.id;
        slug = builder.slug;
        title = builder.title;
        description = builder.description;
        body = builder.body;
        tagList = builder.tagList;
        updatedAt = builder.updatedAt;
        favorite = builder.favorite;
        favoritesCount = builder.favoritesCount;
    }
    public UUID getId() {
        return id;
    }
    public String getSlug() {
        return this.title == null ? null : slugify(title);
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
    public Set<String> getTagList() {
        return tagList;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public Boolean getFavorite() {
        return favorite;
    }
    public int getFavoritesCount() {
        return favoritesCount;
    }
    public Article update(Article article) {
        if (Objects.nonNull(article.getTitle())) {
            this.title = article.getTitle();
        }
        if (Objects.nonNull(article.getBody())) {
            this.body = article.getBody();
        }
        if (Objects.nonNull(article.getDescription())) {
            this.description = article.getDescription();
        }
        if (Objects.nonNull(article.getTagList()) && article.getTagList().size() > 0) {
            this.tagList = article.getTagList();
        }
        this.updatedAt = new Date();
        return this;
    }
    public static final class Builder {
        private UUID id;
        private String slug;
        private String title;
        private String description;
        private String body;
        private Set<String> tagList;
        private Date updatedAt;
        private Boolean favorite;
        private int favoritesCount;
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
        public Builder withTagList(Set<String> val) {
            tagList = val;
            return this;
        }
        public Builder withUpdatedAt(Date val) {
            updatedAt = new Date();
            return this;
        }
        public Builder withFavorite(Boolean val) {
            favorite = val;
            return this;
        }
        public Builder withFavoritesCount(int val) {
            favoritesCount = val;
            return this;
        }
        public Article build() {
            return new Article(this);
        }
    }
}
