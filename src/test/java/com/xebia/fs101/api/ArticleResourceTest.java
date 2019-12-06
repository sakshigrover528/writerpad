package com.xebia.fs101.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.model.Article;
import com.xebia.fs101.model.Status;
import com.xebia.fs101.repository.ArticleRepository;
import com.xebia.fs101.representation.ArticleRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

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
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe").build();
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
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe").build();
        Article savedArticle = articleRepository.save(article);
        String id = savedArticle.getSlug() + "-" + savedArticle.getId();
        mockMvc.perform(get("/api/articles/{slug_uuid}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("How to learn Spring Boot by building an app"))
                .andExpect(jsonPath("$.body").value("You have to believe"))
                .andExpect(jsonPath("$.description").value("Ever wonder how?"));
    }

    @Test
    void should_list_all_articles() throws Exception {
        Article article1 = create("Title1", "Description1", "body1");
        Article article2 = create("Title2", "description2", "body2");
        Article article3 = create("Title3", "description3", "body3");
        articleRepository.saveAll(asList(article1, article2, article3));
        this.mockMvc.perform(get("/api/articles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void should_list_all_articles_with_pagination() throws Exception {
        Article article1 = create("Title1", "Description1", "body1");
        Article article2 = create("Title2", "description2", "body2");
        Article article3 = create("Title3", "description3", "body3");
        articleRepository.saveAll(asList(article1, article2, article3));
        this.mockMvc.perform(get("/api/articles?pageNo=0&pageSize=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    private Article create(String title, String description, String body) {
        return new Article.Builder()
                .withTitle(title)
                .withDescription(description)
                .withDescription(body)
                .build();
    }
    /*@Test
    void should_return_articles_on_the_basis_of_status() throws Exception {
        Article article1=createArticle("Title1","Desc1","Body3");
        Article article2=createArticle("Title2","Desc2","Body3");
        Article article3=createArticle("Title3","Desc3","Body3");
        articleRepository.saveAll(Arrays.asList(article1,article2,article3));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/articles")
                .param("status","draft"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

    }*/

    //@Test
    /*void should_publish_article() throws Exception {
        Article article=createArticle("Title","Desc","Body");
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/articles/{slugUuid}/PUBLISH",id))
                .andDo(print())
                .andExpect(status().isNoContent());
    }*/

    @Test
    void should_give_bad_request_when_tried_to_publish_the_already_published_article() throws Exception {
        Article article = create("Title", "Desc", "Body");
        article.setStatus(Status.PUBLISHED);
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/articles/{slugUuid}/PUBLISH",id))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void should_calculate_reading_time_of_the_given_article() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe").build();
        Article saved = articleRepository.save(article);
        String id = saved.getSlug() + "-" + saved.getId();
        String uri = "/api/articles/" + id + "/timetoread";
        mockMvc.perform(get(uri))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleID").value(id))
                .andExpect(jsonPath("$.readTime.minutes").isNotEmpty())
                .andExpect(jsonPath("$.readTime.seconds").isNotEmpty());
    }

    @Test
    void should_return_tags_with_their_occurrences() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe")
                .withTags(new HashSet<>(Arrays.asList("java", "Spring Boot", "tutorial"))).build();
        Article saved = articleRepository.save(article);
        String uri = "/api/articles/tags";
        mockMvc.perform(get(uri))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].tag").value("Spring Boot"))
                .andExpect(jsonPath("$[0].occurrence").value(1));
    }

/*
    @Test
    void should_mark_article_as_favorite() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe")
                .withTags(new HashSet<>(Arrays.asList("java", "Spring Boot", "tutorial"))).build();
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(patch("/api/articles/{slug_uuid}", id)
                .param("favorited", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorited").value(true))
                .andExpect(jsonPath("$.favoritesCount").value(saved.getFavoritesCount() + 1));
    }

    @Test
    void should_mark_article_as_unfavorite() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe")
                .withTags(new HashSet<>(Arrays.asList("java", "Spring Boot", "tutorial"))).build();
        Article saved = articleRepository.save(article);

        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(patch("/api/articles/{slug_uuid}", id)
                .param("favorited", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorited").value(false))
                .andExpect(jsonPath("$.favoritesCount").value(0));
    }
*/



}
