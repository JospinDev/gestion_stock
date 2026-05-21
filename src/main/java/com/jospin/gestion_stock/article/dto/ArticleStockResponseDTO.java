package com.jospin.gestion_stock.article.dto;

import com.jospin.gestion_stock.entrepot.dto.EntrepotResponseDTO;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleStockResponseDTO {
    private Long id;
    private ArticleResponseDTO article;
    private EntrepotResponseDTO entrepot;
    private Integer quantiteEnStock;
    private Integer nombreMouvements;
    private BigDecimal valeurStock; // Prix moyen * quantité
    private Integer nombreEntrees;
    private Integer nombreSorties;
}