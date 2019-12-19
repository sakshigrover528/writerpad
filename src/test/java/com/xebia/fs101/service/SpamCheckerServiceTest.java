package com.xebia.fs101.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SpamCheckerServiceTest {
    @Mock
    private ResourceLoader resourceLoader;
    @InjectMocks
    private SpamCheckerService spamCheckerService;


    @BeforeEach
    void setUp() {
        spamCheckerService.spamWords = new HashSet<>(Arrays.asList("buttcheeks"));
    }

    @Test
    void should_return_false_if_the_string_doesnt_contain_spam_words() throws IOException {
        String input = "Sakshi";
        boolean spam = spamCheckerService.isSpam(input);
        assertThat(spam).isFalse();
    }

    @Test
    void should_return_true_if_the_given_string_doesnt_contain_spam_words() throws IOException {
        String input = "buttcheeks";
        boolean spam = spamCheckerService.isSpam(input);
        assertThat(spam).isTrue();
    }

}