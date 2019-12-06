package com.xebia.fs101.service;

import com.xebia.fs101.model.Article;
import com.xebia.fs101.model.Status;
import com.xebia.fs101.repository.ArticleRepository;
import com.xebia.fs101.representation.ArticleRequest;
import com.xebia.fs101.representation.TagResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.xebia.fs101.model.Status.PUBLISHED;
import static com.xebia.fs101.utils.StringUtils.toUuid;

@Service
public class ArticleService {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    MailService mailService;

    public Article save(ArticleRequest articleRequest) {
        return articleRepository.save(articleRequest.toArticle());
    }

    public Optional<Article> update(Article updateArticle, String slugUuid) {
        UUID id = toUuid(slugUuid);
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return Optional.empty();
        }
        Article article = optionalArticle.get();
        article.update(updateArticle);
        return Optional.of(articleRepository.save(article));
    }

    public Optional<Article> findById(UUID toUuid) {
        Optional<Article> optionalArticle = articleRepository.findById(toUuid);
        if (!optionalArticle.isPresent())
            return Optional.empty();
        return optionalArticle;
    }

    public boolean delete(String slugUuid) {
        UUID uuid = toUuid(slugUuid);
        Optional<Article> optionalArticle = articleRepository.findById(uuid);
        if (!optionalArticle.isPresent()) {
            return false;
        }
        optionalArticle.ifPresent(a -> articleRepository.deleteById(a.getId()));
        return true;
    }

    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    public List<Article> findByStatus(Status status, Pageable pageable) {
        return this.articleRepository.findByStatus(status, pageable);
    }

    public boolean publishArticle(String slugUuid) {
        String to, subject, text;
        Optional<Article> foundArticle = findById(toUuid(slugUuid));
        if (foundArticle.isPresent() && foundArticle.get().getStatus() != PUBLISHED) {
            Article article = foundArticle.get();
            article.setStatus(PUBLISHED);
            articleRepository.save(article);
            to = "sakshi.grover@xebia.com";
            subject = article.getTitle() + " Published";
            text = "Article Published Successfully \n\n" + article.getBody();
            mailService.sendEmail(to, subject, text);
            return true;
        } else
            return false;
    }


    public List<TagResponse> findAllTagOccurrences() {
        List<TagResponse> tagResponses = new ArrayList<>();
        List<String> allOccurrencesGroupedByTags = articleRepository.
                findAllOccurrencesGroupedByTags();
        for (String tag : allOccurrencesGroupedByTags) {
            String[] allTags = tag.split(",");
            TagResponse response = new TagResponse(
                    allTags[0], Integer.parseInt(allTags[1]));
            tagResponses.add(response);

        }
        return tagResponses;
    }

    public Optional<Article> favoriteTheArticle(UUID uuid, boolean favorited) {
        Optional<Article> optionalArticle = articleRepository.findById(uuid);
        if (!optionalArticle.isPresent()) {
            return Optional.empty();
        }
        Article article = optionalArticle.get();
        checkifArticleIsAlreadyFavoritedOrUnfavorited(favorited, article);
        if (favorited) {
            article.setFavorite(favorited);
            article.setFavoritesCount(article.getFavoritesCount() + 1);
        }
        return Optional.of(articleRepository.save(article));
    }

    public Optional<Article> unfavoriteTheArticle(UUID uuid, boolean unfavorited) {
        Optional<Article> optionalArticle = articleRepository.findById(uuid);
        if (!optionalArticle.isPresent()) {
            return Optional.empty();
        }
        Article article = optionalArticle.get();
        checkifArticleIsAlreadyFavoritedOrUnfavorited(unfavorited, article);
        if (unfavorited) {
            article.setFavorite(unfavorited);
            article.setFavoritesCount(0);
        }
        return Optional.of(articleRepository.save(article));
    }

    public void checkifArticleIsAlreadyFavoritedOrUnfavorited(boolean favorited, Article article) {
        if (favorited && article.getFavorite()) {
            throw new IllegalArgumentException("This article is already favorited");
        } else if (!favorited && !article.getFavorite()) {
            throw new IllegalArgumentException("This article is already unfavorited.");
        }

    }


}
