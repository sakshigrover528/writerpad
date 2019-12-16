package com.xebia.fs101.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.domain.UserRole;
import com.xebia.fs101.repository.UserRepository;
import com.xebia.fs101.representation.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@WithMockUser
class UserResourceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }


    @Test
    void should_create_user() throws Exception {
        UserRequest userRequest = new UserRequest("Sakshi",
                "sakshi.grover@xebia.com", "password", UserRole.ADMIN);
        userRepository.save(userRequest.toUser(passwordEncoder));
        UserRequest userRequest2 = new UserRequest("Sakshi1",
                "sakshi.grover1@xebia.com", "password", UserRole.WRITER);
        String json = objectMapper.writeValueAsString(userRequest2);
        mockMvc.perform(post("/api/users")
                .accept(APPLICATION_JSON)
                .content(json)
                .contentType(APPLICATION_JSON).with(httpBasic("Sakshi",
                        "password")))
                .andExpect(status().isCreated());
    }

    @Test
    void should_give_bad_request_error_when_creating_user_with_same_email() throws Exception {
        UserRequest userRequest = new UserRequest("Sakshi",
                "sakshi.grover@xebia.com", "password", UserRole.ADMIN);
        userRepository.save(userRequest.toUser(passwordEncoder));
        UserRequest anotherUserRequest = new UserRequest("Sakshi",
                "sakshi.grover@xebia.com", "password", UserRole.WRITER);
        String json = objectMapper.writeValueAsString(anotherUserRequest);
        mockMvc.perform(post("/api/users")
                .accept(APPLICATION_JSON)
                .content(json)
                .contentType(APPLICATION_JSON)
                .with(httpBasic("Sakshi", "password")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_give_bad_request_when_user_email_is_invalid() throws Exception {
        UserRequest userRequest = new UserRequest("Sakshi",
                "sakshi.grover", "password", UserRole.WRITER);
        String json = objectMapper.writeValueAsString(userRequest);
        mockMvc.perform(post("/api/users")
                .accept(APPLICATION_JSON)
                .content(json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}