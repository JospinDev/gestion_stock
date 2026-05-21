package com.jospin.gestion_stock.mouvementstock.dto;

import com.jospin.gestion_stock.mouvementstock.entity.TypeMouvement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnteteMouvementRequestDTO {

    @NotNull(message = "Le type de mouvement est obligatoire")
    private TypeMouvement typeMouvement;

    @NotNull(message = "Les détails du mouvement sont obligatoires")
    @NotEmpty(message = "Le mouvement doit contenir au moins un article")
    @Valid
    private List<DetailMouvementRequestDTO> details;
}
