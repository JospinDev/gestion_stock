package com.jospin.gestion_stock.article.repository;

import com.jospin.gestion_stock.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByCodeAndEntrepotId(String code, Long entrepotId);

    boolean existsByCodeAndEntrepotId(String code, Long entrepotId);

    List<Article> findByEntrepotId(Long entrepotId);

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.entrepot WHERE a.id = :id")
    Optional<Article> findByIdWithEntrepot(Long id);

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.entrepot")
    List<Article> findAllWithEntrepot();
}