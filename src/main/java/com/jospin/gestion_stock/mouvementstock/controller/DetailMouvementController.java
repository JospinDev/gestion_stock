package com.jospin.gestion_stock.mouvementstock.controller;

import com.jospin.gestion_stock.common.ApiResponse;
import com.jospin.gestion_stock.mouvementstock.dto.DetailMouvementResponseDTO;
import com.jospin.gestion_stock.mouvementstock.service.DetailMouvementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/details")
@RequiredArgsConstructor
public class DetailMouvementController {

    private final DetailMouvementService detailMouvementService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DetailMouvementResponseDTO>> getDetailById(
            @PathVariable Long id) {

        DetailMouvementResponseDTO detail = detailMouvementService.getDetailById(id);
        return ResponseEntity.ok(ApiResponse.success(detail));
    }

    @GetMapping("/mouvement/{mouvementId}")
    public ResponseEntity<ApiResponse<List<DetailMouvementResponseDTO>>> getDetailsByMouvement(
            @PathVariable Long mouvementId) {

        List<DetailMouvementResponseDTO> details =
                detailMouvementService.getDetailsByMouvement(mouvementId);
        return ResponseEntity.ok(ApiResponse.success(
                "Détails du mouvement récupérés avec succès", details));
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<ApiResponse<List<DetailMouvementResponseDTO>>> getDetailsByArticle(
            @PathVariable Long articleId) {

        List<DetailMouvementResponseDTO> details =
                detailMouvementService.getDetailsByArticle(articleId);
        return ResponseEntity.ok(ApiResponse.success(
                "Détails de l'article récupérés avec succès", details));
    }

    @GetMapping("/entrepot/{entrepotId}")
    public ResponseEntity<ApiResponse<List<DetailMouvementResponseDTO>>> getDetailsByEntrepot(
            @PathVariable Long entrepotId) {

        List<DetailMouvementResponseDTO> details =
                detailMouvementService.getDetailsByEntrepot(entrepotId);
        return ResponseEntity.ok(ApiResponse.success(
                "Détails de l'entrepôt récupérés avec succès", details));
    }
}