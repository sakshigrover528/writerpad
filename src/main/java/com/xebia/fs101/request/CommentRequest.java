package com.xebia.fs101.request;


import com.xebia.fs101.model.Article;
import com.xebia.fs101.model.Comment;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CommentRequest {

    @NotNull(message = "Body is mandatory field")
    @NotEmpty(message = "Body can't be empty")
    private String body;

    public CommentRequest() {

    }

    public CommentRequest(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }



    public Comment toComment(String ipAddress, Article article) {
        return new Comment(this.body, ipAddress, article);
    }
}
