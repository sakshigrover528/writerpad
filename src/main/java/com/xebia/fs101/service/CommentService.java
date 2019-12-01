package com.xebia.fs101.service;

import com.xebia.fs101.model.Comment;
import com.xebia.fs101.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> findAllByArticleId(UUID uuid) {
        return commentRepository.findAllCommentsByArticleId(uuid);
    }

    public boolean delete(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
