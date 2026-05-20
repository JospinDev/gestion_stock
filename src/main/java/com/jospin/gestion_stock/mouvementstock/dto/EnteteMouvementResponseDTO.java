package com.jospin.gestion_stock.mouvementstock.dto;

import com.jospin.gestion_stock.mouvementstock.entity.TypeMouvement;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnteteMouvementResponseDTO {

    private Long id;
    private LocalDateTime date;
    private String numero;
    private TypeMouvement typeMouvement;
    private BigDecimal montantTotal;
    private String description;
    private List<DetailMouvementResponseDTO> details;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
