package com.xebia.fs101.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.model.Article;
import com.xebia.fs101.repository.ArticleRepository;
import com.xebia.fs101.request.ArticleRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleResourceTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ArticleRepository articleRepository;
    @Test
    public void mockmvc_should_be_set() {
        assertThat(mockMvc).isNotNull();
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
    }

    @Test
    void should_return_response_code_201_when_valid_data_is_passed() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withTitle("How to learn Spring Boot")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe")
                .withTags(new HashSet<>(Arrays.asList("java", "Spring Boot", "tutorial"))).build();


        String jsonMock = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(post("/api/articles")
                .content(jsonMock)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    @Test
    void should_get_response_code_400_when_valid_data_is_not_passed() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withDescription("Ever wonder how?")
                .withBody("You have to believe")
                .withTags(new HashSet<>(Arrays.asList("java", "Spring Boot", "tutorial"))).build();

        String jsonMock = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(post("/api/articles")
                .content(jsonMock)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_update_the_article() throws Exception {
            ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withBody("spring boot")
                .withDescription("application")
                .withTitle("spring boot application")
                .build();
        Article article = new Article.Builder()
                .withTitle("spring")
                .withBody("appl")
                .withDescription("boot")
                .build();
        Article articleToBeSaved = articleRepository.save(article);
        String id = String.format("%s-%s", articleToBeSaved.getSlug(), articleToBeSaved.getId());
        String jsonMock = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(patch("/api/articles/{slug_id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMock))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void should_delete_an_article() throws Exception {
        Article article = new Article.Builder()
                .withTitle("spring")
                .withBody("appl")
                .withDescription("boot")
                .build();
        Article savedArticle = articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());
        mockMvc.perform(delete("/api/articles/{slug_id}", id)
        ).andDo(print())
                .andExpect(status().isNoContent());
    }
    @Test
    void should_not_delete_an_article() throws Exception {
        String id = "abc" + "-" + UUID.randomUUID().toString();
        mockMvc.perform(delete("/api/articles/{slug_id}", id)
        ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_find_article_by_id() throws Exception {
        Article article = new Article.Builder()
                .withTitle("spring")
                .withBody("appl")
                .withDescription("boot")
                .build();
        Article savedArticle = articleRepository.save(article);
        String id = savedArticle.getSlug() + "-" + savedArticle.getId();
        mockMvc.perform(get("/api/articles/{slug_uuid}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("spring"))
                .andExpect(jsonPath("$.body").value("appl"))
                .andExpect(jsonPath("$.description").value("boot"));
    }

    @Test
    void should_list_all_articles() throws Exception {
        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3");
        articleRepository.saveAll(asList(article1, article2, article3));
        this.mockMvc.perform(get("/api/articles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void should_list_all_articles_with_pagination() throws Exception {
        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3");
        articleRepository.saveAll(asList(article1, article2, article3));
        this.mockMvc.perform(get("/api/articles?pageNo=0&pageSize=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    private Article createArticle(String title, String description, String body) {
        return new Article.Builder()
                .withTitle(title)
                .withDescription(description)
                .withDescription(body)
                .build();
    }
}
