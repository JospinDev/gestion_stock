package com.jospin.gestion_stock.article.controller;

import com.jospin.gestion_stock.common.ApiResponse;
import com.jospin.gestion_stock.article.dto.ArticleRequestDTO;
import com.jospin.gestion_stock.article.dto.ArticleResponseDTO;
import com.jospin.gestion_stock.article.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ApiResponse<ArticleResponseDTO>> createArticle(
            @Valid @RequestBody ArticleRequestDTO requestDTO) {

        ArticleResponseDTO article = articleService.createArticle(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Article créé avec succès", article));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ArticleResponseDTO>> getArticleById(
            @PathVariable Long id) {

        ArticleResponseDTO article = articleService.getArticleById(id);
        return ResponseEntity.ok(ApiResponse.success(article));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ArticleResponseDTO>> getArticleByCodeAndEntrepot(
            @RequestParam String code,
            @RequestParam Long entrepotId) {

        ArticleResponseDTO article = articleService.getArticleByCodeAndEntrepot(code, entrepotId);
        return ResponseEntity.ok(ApiResponse.success(article));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ArticleResponseDTO>>> getAllArticles() {
        List<ArticleResponseDTO> articles = articleService.getAllArticles();
        return ResponseEntity.ok(ApiResponse.success(
                "Liste des articles récupérée avec succès", articles));
    }

    @GetMapping("/entrepot/{entrepotId}")
    public ResponseEntity<ApiResponse<List<ArticleResponseDTO>>> getArticlesByEntrepot(
            @PathVariable Long entrepotId) {

        List<ArticleResponseDTO> articles = articleService.getArticlesByEntrepot(entrepotId);
        return ResponseEntity.ok(ApiResponse.success(
                "Articles de l'entrepôt récupérés avec succès", articles));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ArticleResponseDTO>> updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody ArticleRequestDTO requestDTO) {

        ArticleResponseDTO article = articleService.updateArticle(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Article mis à jour avec succès", article));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok(ApiResponse.success("Article supprimé avec succès"));
    }
}
