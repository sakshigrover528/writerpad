package com.xebia.fs101.service;

import com.xebia.fs101.model.Article;
import com.xebia.fs101.repository.ArticleRepository;
import com.xebia.fs101.request.ArticleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.xebia.fs101.utils.StringUtils.toUuid;

@Service
public class ArticleService {
    @Autowired
    ArticleRepository articleRepository;
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
    public Article findById(UUID toUuid) {
        return articleRepository.findById(toUuid).orElseThrow(RuntimeException::new);
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
}
