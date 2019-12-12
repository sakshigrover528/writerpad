package com.xebia.fs101.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.domain.Article;
import com.xebia.fs101.domain.Comment;
import com.xebia.fs101.domain.User;
import com.xebia.fs101.domain.UserRole;
import com.xebia.fs101.repository.ArticleRepository;
import com.xebia.fs101.repository.CommentRepository;
import com.xebia.fs101.repository.UserRepository;
import com.xebia.fs101.representation.CommentRequest;
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

import java.util.Arrays;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class CommentResourceTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        UserRequest userRequest = new UserRequest("test",
                "test@xebia.com", "password", UserRole.WRITER);
        user = userRequest.toUser(passwordEncoder);
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_be_able_to_create_comment_for_given_article_slug_id_when_valid_comment_body_is_provided() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe").build();
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());

        CommentRequest commentRequest = new CommentRequest("Awesome tutorial!");
        String json = objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(
                post("/api/articles/{slug_uuid}/comments", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.body").isNotEmpty())
                .andExpect(jsonPath("$.body").value("Awesome tutorial!"))
                .andExpect(jsonPath("$.ipAddress").isNotEmpty());
    }

    @Test
    void should_be_a_response_as_bad_request_if_comment_body_is_blank() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe").build();
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());

        CommentRequest commentRequest = new CommentRequest("");
        String json = objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(
                post("/api/articles/{slug_uuid}/comments", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_all_comments_corresponding_to_given_article_id() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe").build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());

        Comment firstComment = new Comment("Awesome tutorial!", "10.0.0.1", article);
        Comment secondComment = new Comment("A good tutorial", "10.0.1.1", article);
        Comment thirdComment = new Comment("Great Job", "10.1.1.1", article);
        commentRepository.saveAll(Arrays.asList(firstComment, secondComment, thirdComment));
        mockMvc.perform(get("/api/articles/{slug_id}/comments/", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void should_delete_comment_corresponding_to_valid_comment_id() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe").build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        String id = saved.getSlug() + "-" + saved.getId();
        Comment comment = new Comment("", "10.0.0.1", saved);
        Comment savedComment = commentRepository.save(comment);
        Long commentId = savedComment.getId();
        mockMvc.perform(delete("/api/articles/{slug_id}/comments/{id}", id, commentId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void should_not_delete_comment_when_comment_is_invalid() throws Exception {
        Article article = new Article.Builder()
                .withTitle("How to learn Spring Boot by building an app")
                .withDescription("Ever wonder how?")
                .withBody("You have to believe").build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        String id = "invalid id" + UUID.randomUUID().toString();
        Comment comment = new Comment("Awesome Tutorial!", "10.1.1.1", saved);
        Comment savedComment = commentRepository.save(comment);
        Long commentId = savedComment.getId();
        mockMvc.perform(delete("/api/articles/{slug_id}/comments/{id}", id, commentId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_get_400_and_not_post_a_comment_if_spam_word_exist_in_content() throws Exception {
        Article article = new Article.Builder()
                .withBody(" body")
                .withTitle("title")
                .withDescription("description")
                .build();
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);
        String slugId = String.format("%s_%s", savedArticle.getSlug(), savedArticle.getId());
        CommentRequest commentRequest=new CommentRequest("buttcheeks");
        String json=objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(post("/api/articles/{slug_id}/comments",slugId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}