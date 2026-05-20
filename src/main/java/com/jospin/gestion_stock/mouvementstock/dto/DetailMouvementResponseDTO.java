package com.jospin.gestion_stock.mouvementstock.dto;

import com.jospin.gestion_stock.article.dto.ArticleResponseDTO;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailMouvementResponseDTO {

    private Long id;
    private Long idEntete;
    private ArticleResponseDTO article;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal prixTotal;
    private String description;
}
