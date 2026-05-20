package com.jospin.gestion_stock.mouvementstock.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailMouvementRequestDTO {

    @NotNull(message = "L'ID de l'article est obligatoire")
    private Long idArticle;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au minimum 1")
    private Integer quantite;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix unitaire doit être supérieur à 0")
    private BigDecimal prixUnitaire;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;
}
