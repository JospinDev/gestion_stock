package com.jospin.gestion_stock.article.dto;

import com.jospin.gestion_stock.article.entity.Article;
import com.jospin.gestion_stock.entrepot.dto.EntrepotResponseDTO;
import com.jospin.gestion_stock.entrepot.entity.Entrepot;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleStockResponseDTO {
    private Long id;
//    private String code;
//    private String designation;
//    private String entrepotCode;
//    private String entrepotDesignation;
    private ArticleResponseDTO article;
    private EntrepotResponseDTO entrepot;
    private Integer quantiteEnStock;
    private Integer nombreMouvements;
    private BigDecimal valeurStock; // Prix moyen * quantité
    private Integer nombreEntrees;
    private Integer nombreSorties;
}