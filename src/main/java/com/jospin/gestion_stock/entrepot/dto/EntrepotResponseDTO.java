package com.jospin.gestion_stock.entrepot.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntrepotResponseDTO {
    private Long id;
    private String code;
    private String designation;
    private Integer nombreArticles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}