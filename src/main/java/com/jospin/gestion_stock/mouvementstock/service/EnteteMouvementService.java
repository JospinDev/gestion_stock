package com.jospin.gestion_stock.mouvementstock.service;

import com.jospin.gestion_stock.mouvementstock.dto.EnteteMouvementRequestDTO;
import com.jospin.gestion_stock.article.dto.ArticleStockResponseDTO;
import com.jospin.gestion_stock.mouvementstock.dto.EnteteMouvementResponseDTO;
import com.jospin.gestion_stock.mouvementstock.entity.TypeMouvement;

import java.time.LocalDateTime;
import java.util.List;

public interface EnteteMouvementService {

    EnteteMouvementResponseDTO createMouvement(EnteteMouvementRequestDTO requestDTO);

    EnteteMouvementResponseDTO getMouvementById(Long id);

    List<EnteteMouvementResponseDTO> getAllMouvements();

    List<EnteteMouvementResponseDTO> getMouvementsByType(TypeMouvement type);

    List<EnteteMouvementResponseDTO> getMouvementsByPeriod(LocalDateTime startDate, LocalDateTime endDate);

    void deleteMouvement(Long id);

    Integer getQuantiteEnStock(Long articleId);

    ArticleStockResponseDTO getStockByArticle(Long articleId);

    List<ArticleStockResponseDTO> getAllArticlesStock();

    List<ArticleStockResponseDTO> getStockByEntrepot(Long entrepotId);
}