package com.xebia.fs101.api;

import com.xebia.fs101.model.Article;
import com.xebia.fs101.model.ReadingTime;
import com.xebia.fs101.model.Status;
import com.xebia.fs101.representation.ArticleRequest;
import com.xebia.fs101.representation.ReadingTimeResponse;
import com.xebia.fs101.representation.TagResponse;
import com.xebia.fs101.service.ArticleService;
import com.xebia.fs101.service.ReadingTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static com.xebia.fs101.utils.StringUtils.toUuid;

@RestController
@RequestMapping("/api/articles")
public class ArticleResource {
    @Autowired
    ArticleService articleService;
    @Autowired
    ReadingTimeService readingTimeService;
    @PostMapping
    public ResponseEntity<Article> create(@Valid @RequestBody ArticleRequest articleRequest) {
        try {
            Article article = articleService.save(articleRequest);
            return new ResponseEntity<>(article, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PatchMapping(path = "/{slug_uuid}")
    public ResponseEntity<Article> update(@RequestBody ArticleRequest copyFrom,
                                          @PathVariable("slug_uuid") String slugUuid) {
        Article updateArticle = copyFrom.toArticle();
        Optional<Article> updatedArticle = articleService.update(updateArticle, slugUuid);
        return updatedArticle.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping(path = "/{slug_uuid}")
    public ResponseEntity<Void> delete(@PathVariable("slug_uuid") String slugUuid) {
        boolean deleted = articleService.delete(slugUuid);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
    @GetMapping(path = "/{slug_uuid}")
    public ResponseEntity<Optional> findById(@PathVariable("slug_uuid") final String slugUuid) {
        Optional<Article> optionalArticle = articleService.findById(toUuid(slugUuid));
        return new ResponseEntity<>(optionalArticle, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Article>> findAll(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Article> pageResult = articleService.findAll(pageable);
        if (!pageResult.hasContent()) {
            ResponseEntity.noContent().build();
        }
        List<Article> found = pageResult.getContent();
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @GetMapping(params = "status")
    public ResponseEntity<List<Article>> findByStatus(
            @RequestParam final String status, Pageable pageable) {
        List<Article> articles = articleService
                .findByStatus(Status.fromValue(status), pageable);
        return new ResponseEntity<>(articles, HttpStatus.OK);


    }

    @PostMapping(path = "/{slugUuid}/PUBLISH")
    public ResponseEntity<List<Article>> publishArticle(
            @PathVariable("slugUuid") final String slugUuid) {
        boolean published = articleService.publishArticle(slugUuid);
        if (!published) {
            return ResponseEntity.badRequest().build();

        } else
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/{slugUuid}/timetoread")
    public ResponseEntity<ReadingTimeResponse> calculateReadingTime(
            @PathVariable("slugUuid") final String slugUuid) {
        Optional<Article> foundArticle = articleService.findById(toUuid(slugUuid));
        if (!foundArticle.isPresent())
            return ResponseEntity.notFound().build();
        else {
            Duration duration = readingTimeService.calculateReadingTimeInSeconds(
                    foundArticle.get().getBody());
            ReadingTimeResponse readingTimeResponse = new ReadingTimeResponse(
                    slugUuid, new ReadingTime((int) duration.toMinutes(),
                    (int) duration.getSeconds() % 60));
            return new ResponseEntity<>(readingTimeResponse, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/tags")
    public ResponseEntity<List<TagResponse>> getTagsAndTheirReferences() {
        List<TagResponse> tagsWithOccurrence = articleService.findAllTagOccurrences();
        return new ResponseEntity<>(tagsWithOccurrence, HttpStatus.OK);

    }

    @PutMapping(path = "/{slug_uuid}", params = "favorited")
    public ResponseEntity<Article> markArticleAsFavorite(
            @RequestParam final boolean favorited,
            @PathVariable("slug_uuid") String slugUuid) {
        Optional<Article> article = articleService.favoriteTheArticle(toUuid(slugUuid), favorited);
        return article.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{slug_uuid}", params = "unfavorited")
    public ResponseEntity<Article> markArticleUnfavorite(
            @RequestParam final boolean favorited,
            @PathVariable("slug_uuid") String slugUuid) {
        Optional<Article> article = articleService.unfavoriteTheArticle(
                toUuid(slugUuid), favorited);
        return article.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}


