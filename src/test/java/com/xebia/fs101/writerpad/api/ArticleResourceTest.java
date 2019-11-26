package com.xebia.fs101.writerpad.api;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_get_response_201_when_valid_json_is_passed_along_with_optional_fields() throws Exception {
        String json = "{\n"
                + "    \"title\": \"How DNS Works\",\n"
                + "    \"description\": \"Domain Name System\",\n"
                + "    \"body\": \"It is the phonebook of the Internet.\",\n"
                + "    \"tags\": [\n"
                + "        \"dns\",\n"
                + "        \"blog\",\n"
                + "        \"internet\"\n"
                + "    ]\n"
                + "}";
        mockMvc.perform(
                post("/api/articles")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(
                        status().isCreated())
                .andExpect(content().string(CoreMatchers.containsString("How DNS Works")));
    }

    @Test
    void should_get_response_201_when_valid_json_is_passed_along_without_optional_fields() throws Exception {
        String json = "{\n"
                + "    \"title\": \"How DNS Works\",\n"
                + "    \"description\": \"Domain Name System\",\n"
                + "    \"body\": \"It is the phonebook of the Internet\"\n"
                + "}";
        mockMvc.perform(
                post("/api/articles")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(CoreMatchers.containsString("How DNS Works")));
    }

    @Test
    void should_get_response_bad_request_when_mandatory_field_title_is_not_passed() throws Exception {
        String json = "{\n"
                + "    \"description\": \"Domain Name System\",\n"
                + "    \"body\": \"It is the phonebook of the Internet.\",\n"
                + "    \"tags\": [\n"
                + "        \"dns\",\n" +
                "        \"blog\",\n"
                + "        \"internet\"\n"
                + "    ]\n"
                + "}";
        mockMvc.perform(
                post( "/api/articles" )
                        .accept( MediaType.APPLICATION_JSON )
                        .content( json )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andDo( print() )
                .andExpect(
                        status().isBadRequest() );
    }
}