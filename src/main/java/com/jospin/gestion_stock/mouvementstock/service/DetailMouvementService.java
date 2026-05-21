package com.jospin.gestion_stock.mouvementstock.service;

import com.jospin.gestion_stock.mouvementstock.dto.DetailMouvementResponseDTO;

import java.util.List;

public interface DetailMouvementService {
    DetailMouvementResponseDTO getDetailById(Long id);

    List<DetailMouvementResponseDTO> getDetailsByMouvement(Long mouvementId);

    List<DetailMouvementResponseDTO> getDetailsByArticle(Long articleId);

    List<DetailMouvementResponseDTO> getDetailsByEntrepot(Long entrepotId);
}
