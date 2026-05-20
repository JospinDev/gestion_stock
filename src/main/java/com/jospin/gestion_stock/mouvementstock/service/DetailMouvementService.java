package com.jospin.gestion_stock.mouvementstock.service;

import com.jospin.gestion_stock.mouvementstock.dto.DetailMouvementResponseDTO;
import com.jospin.gestion_stock.mouvementstock.entity.DetailMouvement;
import com.jospin.gestion_stock.mouvementstock.entity.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface DetailMouvementService {
    DetailMouvementResponseDTO getDetailById(Long id);

    List<DetailMouvementResponseDTO> getDetailsByMouvement(Long mouvementId);

    List<DetailMouvementResponseDTO> getDetailsByArticle(Long articleId);

    List<DetailMouvementResponseDTO> getDetailsByEntrepot(Long entrepotId);
}
