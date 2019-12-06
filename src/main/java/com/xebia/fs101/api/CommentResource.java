package com.xebia.fs101.api;

import com.xebia.fs101.model.Article;
import com.xebia.fs101.model.Comment;
import com.xebia.fs101.representation.CommentRequest;
import com.xebia.fs101.service.ArticleService;
import com.xebia.fs101.service.CommentService;
import com.xebia.fs101.service.SpamCheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.xebia.fs101.utils.StringUtils.toUuid;

@RestController
@RequestMapping("/api/articles/{slug_uuid}/comments")
public class CommentResource {
    @Autowired
    CommentService commentService;
    @Autowired
    ArticleService articleService;
    @Autowired
    SpamCheckerService spamCheckerService;

    @PostMapping
    public ResponseEntity<Comment> create(@Valid @RequestBody CommentRequest commentRequest,
                                          @PathVariable("slug_uuid") String slugUuid,
                                          HttpServletRequest httpServletRequest) {

        Optional<Article> optionArticle = articleService.findById(toUuid(slugUuid));
        if (optionArticle.isPresent()) {
            if (!this.spamCheckerService.isSpam(commentRequest.getBody())) {
                Comment savedComments = commentService.save(
                        commentRequest.toComment(httpServletRequest.getRemoteAddr()
                                , optionArticle.get()));

                return new ResponseEntity<>(savedComments, HttpStatus.CREATED);
            }
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping()
    ResponseEntity<List<Comment>> getAll(@PathVariable("slug_uuid") String slugUuid) {
        Optional<Article> optionalArticle = articleService.findById(toUuid(slugUuid));
        if (optionalArticle.isPresent()) {
            List<Comment> allComments = commentService.findAllByArticleId(
                    optionalArticle.get().getId());
            return new ResponseEntity<>(allComments, HttpStatus.OK);

        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Void> delete(@PathVariable("slug_uuid") String slugUuid,
                                @PathVariable("id") Long id) {
        Optional<Article> optionalArticle = articleService.findById((toUuid((slugUuid))));
        if (optionalArticle.isPresent()) {
            if (commentService.delete(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}

