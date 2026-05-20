package com.jospin.gestion_stock.article.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleRequestDTO {

    @NotBlank(message = "Le code est obligatoire")
    @Size(min = 2, max = 50, message = "Le code doit contenir entre 2 et 50 caractères")
    private String code;

    @NotBlank(message = "La désignation est obligatoire")
    @Size(min = 3, max = 200, message = "La désignation doit contenir entre 3 et 200 caractères")
    private String designation;

    @NotNull(message = "L'ID de l'entrepôt est obligatoire")
    private Long idEntrepot;
}
