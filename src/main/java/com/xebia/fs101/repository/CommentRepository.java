package com.xebia.fs101.repository;

import com.xebia.fs101.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllCommentsByArticleId(UUID uuid);

}
