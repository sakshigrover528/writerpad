package com.xebia.fs101.repository;

import com.xebia.fs101.domain.Article;
import com.xebia.fs101.domain.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {
    List<Article> findByStatus(Status status, Pageable pageable);

    @Query(value = "select TAGS,count(TAGS) from ARTICLE_TAGS group by TAGS", nativeQuery = true)
    List<String> findAllOccurrencesGroupedByTags();
}
