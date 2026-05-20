package com.jospin.gestion_stock.mouvementstock.mapper;

import com.jospin.gestion_stock.mouvementstock.dto.EnteteMouvementRequestDTO;
import com.jospin.gestion_stock.mouvementstock.dto.EnteteMouvementResponseDTO;
import com.jospin.gestion_stock.mouvementstock.entity.EnteteMouvement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EnteteMouvementMapper {

    private final DetailMouvementMapper detailMapper;

    public EnteteMouvement toEntity(EnteteMouvementRequestDTO dto) {
        return EnteteMouvement.builder()
                .typeMouvement(dto.getTypeMouvement())
                .build();
    }

    public EnteteMouvementResponseDTO toResponseDTO(EnteteMouvement entete) {
        return EnteteMouvementResponseDTO.builder()
                .id(entete.getId())
                .date(entete.getDate())
                .numero(entete.getNumero())
                .typeMouvement(entete.getTypeMouvement())
                .montantTotal(entete.getMontantTotal())
                .description(entete.getDescription())
                .details(entete.getDetails().stream()
                        .map(detailMapper::toResponseDTO)
                        .collect(Collectors.toList()))
                .createdAt(entete.getCreatedAt())
                .updatedAt(entete.getUpdatedAt())
                .build();
    }
}
