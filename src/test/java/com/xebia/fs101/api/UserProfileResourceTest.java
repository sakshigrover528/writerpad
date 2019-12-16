package com.xebia.fs101.api;

import com.xebia.fs101.domain.Article;
import com.xebia.fs101.domain.User;
import com.xebia.fs101.domain.UserRole;
import com.xebia.fs101.repository.ArticleRepository;
import com.xebia.fs101.repository.UserRepository;
import com.xebia.fs101.representation.UserRequest;
import com.xebia.fs101.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static com.xebia.fs101.domain.UserRole.ADMIN;
import static com.xebia.fs101.domain.UserRole.WRITER;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserProfileResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    private User admin, firstWriter, secondWriter;
    private Article article;

    private User create(String username, String email, String password, UserRole role) {
        UserRequest userRequest = new UserRequest(username, email, password, role);
        User user = userRequest.toUser(passwordEncoder);
        return userRepository.save(user);
    }

    @BeforeEach
    void setUp() {
        this.admin = create("admin1", "admin@email.com",
                "password", ADMIN);
        this.firstWriter = create("firstWriter", "firstWriter@email.com",
                "password", WRITER);
        this.secondWriter = create("secondWriter", "secondWriter@email.com",
                "password", WRITER);
        this.article = createArticle("How to learn Spring Boot",
                "Ever wonder how?", "You have to believe", secondWriter);


    }

    private Article createArticle(String title, String description, String body, User user) {
        Article article = new Article.Builder()
                .withTitle(title)
                .withDescription(description)
                .withBody(body)
                .build();
        article.setUser(user);
        return articleRepository.save(article);
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void one_user_should_be_able_to_follow_the_other_user() throws Exception {
        String secondWriterUsername = this.secondWriter.getUsername();
        this.mockMvc.perform(post("/api/profiles/{username}/follow", secondWriterUsername)
                .with(httpBasic("firstWriter", "password")))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void one_user_should_be_able_to_unfollow_the_other_user() throws Exception {

        String secondWriterUsername = this.secondWriter.getUsername();
        this.mockMvc.perform(post("/api/profiles/{username}/follow", secondWriterUsername)
                .with(httpBasic("firstWriter", "password")))
                .andDo(print());
        this.mockMvc.perform(delete("/api/profiles/{username}/unfollow", secondWriterUsername)
                .with(httpBasic("firstWriter", "password")))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void should_be_able_to_see_profile_of_first_writer() throws Exception {

        String secondWriterUsername = this.secondWriter.getUsername();

        this.mockMvc.perform(post("/api/profiles/{username}/follow", secondWriterUsername)
                .with(httpBasic("firstWriter", "password")))
                .andDo(print());
        this.mockMvc.perform(get("/api/profiles/{username}", secondWriterUsername)
                .with(httpBasic("firstWriter", "password")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("secondWriter"))
                //.andExpect(jsonPath("$.isFollowing").value(secondWriter.isFollowing()))
                .andExpect(jsonPath("$.countOfFollowers").value(1))
                .andExpect(jsonPath("$.countOfFollowing").value(0))
                .andExpect(jsonPath("$.articles[0].id").value(
                        article.getSlug() + "-" + article.getId()))
                .andExpect(jsonPath("$.articles[0].title").value(article.getTitle()));
    }
}