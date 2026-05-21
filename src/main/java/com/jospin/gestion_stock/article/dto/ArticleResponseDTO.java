package com.jospin.gestion_stock.article.dto;

import com.jospin.gestion_stock.entrepot.entity.Entrepot;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleResponseDTO {
    private Long id;
    private String code;
    private String designation;
    private Entrepot entrepot;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
