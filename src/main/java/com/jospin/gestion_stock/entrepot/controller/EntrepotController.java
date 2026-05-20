package com.jospin.gestion_stock.entrepot.controller;

import com.jospin.gestion_stock.common.ApiResponse;
import com.jospin.gestion_stock.entrepot.dto.EntrepotRequestDTO;
import com.jospin.gestion_stock.entrepot.dto.EntrepotResponseDTO;
import com.jospin.gestion_stock.entrepot.service.EntrepotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entrepots")
@RequiredArgsConstructor
public class EntrepotController {

    private final EntrepotService entrepotService;

    @PostMapping
    public ResponseEntity<ApiResponse<EntrepotResponseDTO>> createEntrepot(
            @Valid @RequestBody EntrepotRequestDTO requestDTO) {

        EntrepotResponseDTO entrepot = entrepotService.createEntrepot(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Entrepôt créé avec succès", entrepot));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntrepotResponseDTO>> getEntrepotById(
            @PathVariable Long id) {

        EntrepotResponseDTO entrepot = entrepotService.getEntrepotById(id);
        return ResponseEntity.ok(ApiResponse.success(entrepot));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<EntrepotResponseDTO>> getEntrepotByCode(
            @PathVariable String code) {

        EntrepotResponseDTO entrepot = entrepotService.getEntrepotByCode(code);
        return ResponseEntity.ok(ApiResponse.success(entrepot));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EntrepotResponseDTO>>> getAllEntrepots() {
        List<EntrepotResponseDTO> entrepots = entrepotService.getAllEntrepots();
        return ResponseEntity.ok(ApiResponse.success(
                "Liste des entrepôts récupérée avec succès", entrepots));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EntrepotResponseDTO>> updateEntrepot(
            @PathVariable Long id,
            @Valid @RequestBody EntrepotRequestDTO requestDTO) {

        EntrepotResponseDTO entrepot = entrepotService.updateEntrepot(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Entrepôt mis à jour avec succès", entrepot));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEntrepot(@PathVariable Long id) {
        entrepotService.deleteEntrepot(id);
        return ResponseEntity.ok(ApiResponse.success("Entrepôt supprimé avec succès"));
    }
}