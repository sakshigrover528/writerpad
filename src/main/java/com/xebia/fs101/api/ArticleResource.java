package com.xebia.fs101.api;

import com.xebia.fs101.model.Article;
import com.xebia.fs101.request.ArticleRequest;
import com.xebia.fs101.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/articles")
public class ArticleResource {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<Article> save(@Valid @RequestBody ArticleRequest articleRequest) {
        try {
            Article save = articleService.save(articleRequest);
            return new ResponseEntity<>(save, CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }
}
