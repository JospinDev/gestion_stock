package com.jospin.gestion_stock.article.mapper;

import com.jospin.gestion_stock.article.dto.ArticleRequestDTO;
import com.jospin.gestion_stock.article.dto.ArticleResponseDTO;
import com.jospin.gestion_stock.article.entity.Article;
import com.jospin.gestion_stock.entrepot.entity.Entrepot;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public Article toEntity(ArticleRequestDTO dto, Entrepot entrepot) {
        return Article.builder()
                .code(dto.getCode().toUpperCase())
                .designation(dto.getDesignation())
                .entrepot(entrepot)
                .build();
    }

    public ArticleResponseDTO toResponseDTO(Article article) {
        Entrepot e = article.getEntrepot();
        return ArticleResponseDTO.builder()
                .id(article.getId())
                .code(article.getCode())
                .designation(article.getDesignation())
                .entrepot(Entrepot.builder()
                                .id(e.getId())
                                .code(e.getCode())
                                .designation(e.getDesignation())
                                .build())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDTO(ArticleRequestDTO dto, Article article, Entrepot entrepot) {
        article.setCode(dto.getCode().toUpperCase());
        article.setDesignation(dto.getDesignation());
        article.setEntrepot(entrepot);
    }
}
