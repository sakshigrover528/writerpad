package com.xebia.fs101.service;

import com.xebia.fs101.model.Article;
import com.xebia.fs101.repository.ArticleRepository;
import com.xebia.fs101.request.ArticleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Article save(ArticleRequest articleRequest) {
        return articleRepository.save(articleRequest.toArticle());
    }
}
