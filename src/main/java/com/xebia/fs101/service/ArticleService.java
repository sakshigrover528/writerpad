package com.xebia.fs101.service;

import com.xebia.fs101.model.Article;
import com.xebia.fs101.model.Status;
import com.xebia.fs101.repository.ArticleRepository;
import com.xebia.fs101.request.ArticleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}
