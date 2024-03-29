package com.xebia.fs101.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    private Set<String> tags;
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
    private Status status;
    @JsonManagedReference
    @ManyToOne(optional = false)
    private User user;
    private String image;

    public Article() {
    }

    private Article(Builder builder) {
        id = builder.id;
        slug = builder.slug;
        title = builder.title;
        description = builder.description;
        body = builder.body;
        tags = builder.tagList;

        favorite = builder.favorite;
        favoritesCount = builder.favoritesCount;
        comments = builder.comments;
        status = builder.status;
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

    public Set<String> getTags() {
        return tags;
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

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(int favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Article update(Article changedArticle) {

        if (Objects.nonNull(changedArticle.getTitle())) {
            this.title = changedArticle.getTitle();
        }
        if (Objects.nonNull(changedArticle.getBody())) {
            this.body = changedArticle.getBody();
        }
        if (Objects.nonNull(changedArticle.getDescription())) {
            this.description = changedArticle.getDescription();
        }
        if (Objects.nonNull(changedArticle.getTags()) && changedArticle.getTags().size() > 0) {
            this.tags = changedArticle.getTags();
        }


        this.updatedAt = new Date();
        return this;
    }

    @Override
    public String toString() {
        return "Article{"
                + "id=" + id
                + ", slug='" + slug + '\''
                + ", title='" + title + '\''
                + ", description='" + description + '\''
                + ", body='" + body + '\''
                + ", tags=" + tags
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + ", favorite=" + favorite
                + ", favoritesCount=" + favoritesCount
                + ", comments=" + comments
                + ", status=" + status
                + '}';
    }

    public static final class Builder {
        private UUID id;
        private String slug;
        private String title;
        private String description;
        private String body;
        private Set<String> tagList;
        private Date createdAt;
        private Date updatedAt;
        private Boolean favorite;
        private int favoritesCount;
        private List<Comment> comments;
        private Status status;

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
            tagList = val;
            return this;
        }


        public Builder withUpdatedAt(Date val) {
            updatedAt = val;
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

        public Builder withComments(List<Comment> val) {
            comments = val;
            return this;
        }

        public Builder withStatus(Status val) {
            status = val;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }
}

