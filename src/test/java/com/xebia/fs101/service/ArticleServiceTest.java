package com.xebia.fs101.service;

import com.xebia.fs101.repository.ArticleRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    ArticleRepository articleRepository;

    @InjectMocks
    ArticleService articleService;

    /*@Test
    void should_save_the_article() {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withBody("spring boot")
                .withDescription("application")
                .withTitle("spring boot application")
                .build();
        articleService.save(articleRequest);
        verify(articleRepository.save(articleRequest.toArticle()));
        verifyNoMoreInteractions(articleRepository);
    }*/
}