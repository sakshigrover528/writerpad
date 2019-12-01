package com.xebia.fs101.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpamCheckerTest {
    @Mock
    private ResourceLoader resourceLoader;
    @InjectMocks
    private SpamChecker spamChecker;

    @BeforeEach
    void setUp() throws IOException {
        when(resourceLoader.getResource(anyString())).thenReturn(new ClassPathResource("spamwords.txt"));
        spamChecker.init();
    }

    @Test
    void should_return_false_if_the_string_doesnt_contain_spam_words() throws IOException {
        String input = "Sakshi";
        boolean spam = spamChecker.isSpam(input);
        assertThat(spam).isFalse();
    }

    @Test
    void should_return_true_if_the_given_string_doesnt_contain_spam_words() throws IOException {
        String input = "buttcheeks";
        boolean spam = spamChecker.isSpam(input);
        assertThat(spam).isTrue();
    }

}