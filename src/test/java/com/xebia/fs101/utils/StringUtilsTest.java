package com.xebia.fs101.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @Test
    void should_be_able_to_slug_the_input_string() {
        String input = "This is test string";
        String slugify = StringUtils.slugify(input);
        assertThat(slugify).isEqualTo("this-is-test-string");
    }

    @Test
    void should_be_able_to_trim_spaces_in_the_string() {
        String input = "This     is     test   string";
        String slugify = StringUtils.slugify(input);
        assertThat(slugify).isEqualTo("this-is-test-string");
    }


}