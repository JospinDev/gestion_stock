package com.jospin.gestion_stock.article.service;

import com.jospin.gestion_stock.article.dto.ArticleRequestDTO;
import com.jospin.gestion_stock.article.dto.ArticleResponseDTO;
import java.util.List;

public interface ArticleService {

    ArticleResponseDTO createArticle(ArticleRequestDTO requestDTO);

    ArticleResponseDTO getArticleById(Long id);

    ArticleResponseDTO getArticleByCodeAndEntrepot(String code, Long entrepotId);

    List<ArticleResponseDTO> getAllArticles();

    List<ArticleResponseDTO> getArticlesByEntrepot(Long entrepotId);

    ArticleResponseDTO updateArticle(Long id, ArticleRequestDTO requestDTO);

    void deleteArticle(Long id);
}
