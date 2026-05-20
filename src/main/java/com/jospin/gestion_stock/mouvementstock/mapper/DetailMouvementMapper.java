package com.jospin.gestion_stock.mouvementstock.mapper;

import com.jospin.gestion_stock.article.dto.ArticleResponseDTO;
import com.jospin.gestion_stock.mouvementstock.dto.DetailMouvementRequestDTO;
import com.jospin.gestion_stock.mouvementstock.dto.DetailMouvementResponseDTO;
import com.jospin.gestion_stock.article.entity.Article;
import com.jospin.gestion_stock.mouvementstock.entity.DetailMouvement;
import org.springframework.stereotype.Component;

@Component
public class DetailMouvementMapper {

    public DetailMouvement toEntity(DetailMouvementRequestDTO dto, Article article) {
        return DetailMouvement.builder()
                .article(article)
                .quantite(dto.getQuantite())
                .prixUnitaire(dto.getPrixUnitaire())
                .description(dto.getDescription())
                .build();
    }

    public DetailMouvementResponseDTO toResponseDTO(DetailMouvement detail) {
        Article a = detail.getArticle();
        return DetailMouvementResponseDTO.builder()
                .id(detail.getId())
                .idEntete(detail.getEnteteMouvement() != null ?
                        detail.getEnteteMouvement().getId() : null)
                .article(
                        ArticleResponseDTO.builder()
                                .id(a.getId())
                                .code(a.getCode())
                                .designation(a.getDesignation())
                                .build()
                )
                .quantite(detail.getQuantite())
                .prixUnitaire(detail.getPrixUnitaire())
                .prixTotal(detail.getPrixTotal())
                .description(detail.getDescription())
                .build();
    }
}
