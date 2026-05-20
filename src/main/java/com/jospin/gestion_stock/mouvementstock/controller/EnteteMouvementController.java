package com.jospin.gestion_stock.mouvementstock.controller;

import com.jospin.gestion_stock.article.dto.ArticleStockResponseDTO;
import com.jospin.gestion_stock.common.ApiResponse;
import com.jospin.gestion_stock.mouvementstock.dto.EnteteMouvementRequestDTO;
import com.jospin.gestion_stock.mouvementstock.dto.EnteteMouvementResponseDTO;
import com.jospin.gestion_stock.mouvementstock.entity.TypeMouvement;
import com.jospin.gestion_stock.mouvementstock.service.EnteteMouvementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/mouvements")
@RequiredArgsConstructor
public class EnteteMouvementController {

    private final EnteteMouvementService enteteMouvementService;

    @PostMapping
    public ResponseEntity<ApiResponse<EnteteMouvementResponseDTO>> createMouvement(
            @Valid @RequestBody EnteteMouvementRequestDTO requestDTO) {

        EnteteMouvementResponseDTO mouvement = enteteMouvementService.createMouvement(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Mouvement créé avec succès", mouvement));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EnteteMouvementResponseDTO>> getMouvementById(
            @PathVariable Long id) {

        EnteteMouvementResponseDTO mouvement = enteteMouvementService.getMouvementById(id);
        return ResponseEntity.ok(ApiResponse.success(mouvement));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EnteteMouvementResponseDTO>>> getAllMouvements() {
        List<EnteteMouvementResponseDTO> mouvements = enteteMouvementService.getAllMouvements();
        return ResponseEntity.ok(ApiResponse.success(
                "Liste des mouvements récupérée avec succès", mouvements));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<EnteteMouvementResponseDTO>>> getMouvementsByType(
            @PathVariable TypeMouvement type) {

        List<EnteteMouvementResponseDTO> mouvements = enteteMouvementService.getMouvementsByType(type);
        return ResponseEntity.ok(ApiResponse.success(
                "Mouvements de type " + type + " récupérés avec succès", mouvements));
    }

    @GetMapping("/period")
    public ResponseEntity<ApiResponse<List<EnteteMouvementResponseDTO>>> getMouvementsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<EnteteMouvementResponseDTO> mouvements =
                enteteMouvementService.getMouvementsByPeriod(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(
                "Mouvements de la période récupérés avec succès", mouvements));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMouvement(@PathVariable Long id) {
        enteteMouvementService.deleteMouvement(id);
        return ResponseEntity.ok(ApiResponse.success("Mouvement supprimé avec succès"));
    }

    @GetMapping("/stock/article/{articleId}")
    public ResponseEntity<ApiResponse<ArticleStockResponseDTO>> getStockByArticle(
            @PathVariable Long articleId) {

        ArticleStockResponseDTO stock = enteteMouvementService.getStockByArticle(articleId);
        return ResponseEntity.ok(ApiResponse.success(stock));
    }

    @GetMapping("/stock")
    public ResponseEntity<ApiResponse<List<ArticleStockResponseDTO>>> getAllArticlesStock() {
        List<ArticleStockResponseDTO> stocks = enteteMouvementService.getAllArticlesStock();
        return ResponseEntity.ok(ApiResponse.success(
                "État du stock récupéré avec succès", stocks));
    }

    @GetMapping("/stock/entrepot/{entrepotId}")
    public ResponseEntity<ApiResponse<List<ArticleStockResponseDTO>>> getStockByEntrepot(
            @PathVariable Long entrepotId) {

        List<ArticleStockResponseDTO> stocks = enteteMouvementService.getStockByEntrepot(entrepotId);
        return ResponseEntity.ok(ApiResponse.success(
                "État du stock de l'entrepôt récupéré avec succès", stocks));
    }

    @GetMapping("/stock/quantity/{articleId}")
    public ResponseEntity<ApiResponse<Integer>> getQuantiteEnStock(
            @PathVariable Long articleId) {

        Integer quantity = enteteMouvementService.getQuantiteEnStock(articleId);
        return ResponseEntity.ok(ApiResponse.success(
                "Quantité en stock récupérée avec succès", quantity));
    }
}
