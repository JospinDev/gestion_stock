package com.jospin.gestion_stock.entrepot.mapper;

import com.jospin.gestion_stock.entrepot.dto.EntrepotRequestDTO;
import com.jospin.gestion_stock.entrepot.dto.EntrepotResponseDTO;
import com.jospin.gestion_stock.entrepot.entity.Entrepot;
import org.springframework.stereotype.Component;

@Component
public class EntrepotMapper {

    public Entrepot toEntity(EntrepotRequestDTO dto) {
        return Entrepot.builder()
                .code(dto.getCode().toUpperCase())
                .designation(dto.getDesignation())
                .build();
    }

    public EntrepotResponseDTO toResponseDTO(Entrepot entrepot) {
        return EntrepotResponseDTO.builder()
                .id(entrepot.getId())
                .code(entrepot.getCode())
                .designation(entrepot.getDesignation())
                .nombreArticles(entrepot.getArticles() != null ? entrepot.getArticles().size() : 0)
                .createdAt(entrepot.getCreatedAt())
                .updatedAt(entrepot.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDTO(EntrepotRequestDTO dto, Entrepot entrepot) {
        entrepot.setCode(dto.getCode().toUpperCase());
        entrepot.setDesignation(dto.getDesignation());
    }
}
