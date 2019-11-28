package com.xebia.fs101.request;

import com.xebia.fs101.model.Article;
import com.xebia.fs101.model.Comment;

import javax.validation.constraints.NotBlank;

public class CommentRequest {
    @NotBlank(message = "comment body can't be null")
    private String body;
    public CommentRequest() {
    }
    public CommentRequest(
            @NotBlank(message = "comment body can't be null") String body) {
        this.body = body;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public Comment toComment(String ipAddress, Article article) {
        return new Comment(body, ipAddress, article);
    }
}
