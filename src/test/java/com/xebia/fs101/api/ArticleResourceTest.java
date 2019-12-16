package com.xebia.fs101.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.domain.Article;
import com.xebia.fs101.domain.Status;
import com.xebia.fs101.domain.User;
import com.xebia.fs101.domain.UserRole;
import com.xebia.fs101.repository.ArticleRepository;
import com.xebia.fs101.repository.UserRepository;
import com.xebia.fs101.representation.ArticleRequest;
import com.xebia.fs101.representation.UserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static com.xebia.fs101.domain.UserRole.ADMIN;
import static com.xebia.fs101.domain.UserRole.EDITOR;
import static com.xebia.fs101.domain.UserRole.WRITER;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class ArticleResourceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ArticleRepository articleRepository;
    private User writer;
    private User admin;

    private UserRequest createUserRequest(String username, String email,
                                          String password, UserRole userRole) {
        return new UserRequest(username, email, password, userRole);
    }

    private Article create(String title, String description, String body, User user) {
        Article article = new Article.Builder()
                .withTitle(title)
                .withDescription(description)
                .withBody(body)
                .build();
        article.setUser(user);
        return article;
    }

    @BeforeEach
    void setUp() {
        writer = createUserRequest("abc",
                "sakshi.grover@xebia.com", "password", WRITER).toUser(passwordEncoder);
        userRepository.save(writer);

        admin = createUserRequest("sakshi", "abc@gmail.com",
                "password", ADMIN).toUser(passwordEncoder);
        userRepository.save(admin);
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void mockmvc_should_be_set() {
        assertThat(mockMvc).isNotNull();
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
                .content(jsonMock).with(httpBasic("abc", "password"))
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
        article.setUser(writer);
        Article articleToBeSaved = this.articleRepository.save(article);
        String id = String.format("%s-%s", articleToBeSaved.getSlug(), articleToBeSaved.getId());
        System.out.println("iddddddd" + id);
        String jsonMock = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(patch("/api/articles/{slug_id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMock)
                .with(httpBasic("abc", "password")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void should_delete_an_article_with_admin_user() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe").build();
        article.setUser(writer);
        Article savedArticle = articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());
        mockMvc.perform(delete("/api/articles/{slug_id}", id)
                .with(httpBasic("sakshi", "password")))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void should_not_delete_an_article() throws Exception {
        String id = "abc" + "-" + UUID.randomUUID().toString();
        mockMvc.perform(delete("/api/articles/{slug_id}", id)
                .with(httpBasic("sakshi", "password"))
        ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_find_article_by_id() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe").build();
        article.setUser(writer);
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
        Article article1 = create("Title1", "Description1", "body1", writer);
        Article article2 = create("Title2", "description2", "body2", writer);
        Article article3 = create("Title3", "description3", "body3", writer);

        articleRepository.saveAll(asList(article1, article2, article3));
        this.mockMvc.perform(get("/api/articles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void should_list_all_articles_with_pagination() throws Exception {
        Article article1 = create("Title1", "Description1", "body1", writer);
        Article article2 = create("Title2", "description2", "body2", writer);
        Article article3 = create("Title3", "description3", "body3", writer);
        articleRepository.saveAll(asList(article1, article2, article3));
        this.mockMvc.perform(get("/api/articles?pageNo=0&pageSize=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
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

   /* @Test
    void should_publish_article() throws Exception {
        UserRequest userRequest = new UserRequest("sakshi", "abc@gmail.com",
                "password", EDITOR);
        User editor = userRequest.toUser(passwordEncoder);
        userRepository.save(editor);

        Article article = create("Title", "Desc", "Body");
        article.setUser(editor);
        Article saved = articleRepository.save(article);

        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(
                post("/api/articles/{slugUuid}/PUBLISH", id)
                        .with(httpBasic("sakshi", "password")))
                .andDo(print())
                .andExpect(status().isNoContent());
    }*/

    @Test
    void should_give_bad_request_when_tried_to_publish_the_already_published_article() throws Exception {
        UserRequest userRequest = new UserRequest("editor", "abc1@gmail.com",
                "password", EDITOR);
        User editor = userRequest.toUser(passwordEncoder);
        userRepository.save(editor);
        Article article = create("Title", "Desc", "Body", editor);
        article.setStatus(Status.PUBLISHED);
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(post(
                "/api/articles/{slugUuid}/PUBLISH", id)
                .with(httpBasic("editor", "password")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_calculate_reading_time_of_the_given_article() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe").build();
        article.setUser(writer);
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
        article.setUser(writer);
        Article saved = articleRepository.save(article);
        String uri = "/api/articles/tags";
        mockMvc.perform(get(uri))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].tag").value("Spring Boot"))
                .andExpect(jsonPath("$[0].occurrence").value(1));
    }


   /* @Test
    void should_mark_article_as_favorite() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe")
                .withTags(new HashSet<>(Arrays.asList("java", "Spring Boot", "tutorial"))).build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(patch("/api/articles/{slug_uuid}", id)
                .param("favorited", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorited").value(true))
                .andExpect(jsonPath("$.favoritesCount").value(saved.getFavoritesCount() + 1));
    }*/

    /*@Test
    void should_mark_article_as_unfavorite() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe")
                .withTags(new HashSet<>(Arrays.asList("java", "Spring Boot", "tutorial"))).build();
        article.setUser(user);
        Article saved = articleRepository.save(article);

        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(patch("/api/articles/{slug_uuid}", id)
                .param("favorited", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorited").value(false))
                .andExpect(jsonPath("$.favoritesCount").value(0));
    }*/

    @Test
    void should_not_update_article_if_user_is_not_the_owner_of_the_article() throws Exception {
        ArticleRequest updateRequest = new ArticleRequest.Builder()
                .withBody("body")
                .withTitle("title")
                .withDescription("desc")
                .withTags(new HashSet<>(Arrays.asList("java", "Spring Boot", "tutorial"))).build();
        Article article = new Article.Builder()
                .withTitle("DEF")
                .withDescription("Desc")
                .withBody("DEF")
                .build();
        article.setUser(writer);
        Article saved = articleRepository.save(article);
        String json = objectMapper.writeValueAsString(updateRequest);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        UserRequest userRequest1 = createUserRequest("test1", "test1@xebia.com",
                "password", WRITER);
        User user1 = userRequest1.toUser(passwordEncoder);
        userRepository.save(user1);
        this.mockMvc.perform(patch("/api/articles/{slug_uuid}", id)
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .with(httpBasic("test1", "password")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void should_not_delete_article_if_user_is_not_owner_of_if() throws Exception {
        UserRequest userRequest1 = createUserRequest("test1", "test1@xebia.com",
                "password", WRITER);
        User user1 = userRequest1.toUser(passwordEncoder);
        userRepository.save(user1);
        Article article = new Article.Builder()
                .withTitle("spring")
                .withBody("appl")
                .withDescription("boot")
                .build();
        article.setUser(writer);
        Article savedArticle = articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());
        this.mockMvc.perform(delete("/api/articles/{slug_id}", id)
                .with(httpBasic("test1", "password")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void should_update_article_if_similar_article_is_not_present_excluding_the_one_which_is_getting_updated() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe").build();
        article.setUser(writer);
        Article savedArticle = articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());
        ArticleRequest articleRequest = new ArticleRequest.Builder().withBody(
                "body").withTitle("title").withDescription("desc").build();
        String json = objectMapper.writeValueAsString(articleRequest);
        this.mockMvc.perform(patch("/api/articles/{slug_id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic("abc", "password")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void should_not_update_article_if_a_article_with_similar_body_is_already_present()
            throws Exception {

        Article article = create("title", "desc", "body", writer);
        articleRepository.save(article);
        String id = String.format("%s-%s", article.getSlug(), article.getId());

        ArticleRequest duplicateBodyRequest = new ArticleRequest.Builder().withBody(
                "body").withTitle("title").withDescription("desc").build();
        String json = objectMapper.writeValueAsString(duplicateBodyRequest);

        this.mockMvc.perform(patch("/api/articles/{slug_id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic("sakshi", "password")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
