package com.jospin.gestion_stock.mouvementstock.service;

import com.jospin.gestion_stock.article.dto.ArticleResponseDTO;
import com.jospin.gestion_stock.article.dto.ArticleStockResponseDTO;
import com.jospin.gestion_stock.entrepot.dto.EntrepotResponseDTO;
import com.jospin.gestion_stock.entrepot.entity.Entrepot;
import com.jospin.gestion_stock.mouvementstock.dto.DetailMouvementRequestDTO;
import com.jospin.gestion_stock.mouvementstock.dto.EnteteMouvementRequestDTO;
import com.jospin.gestion_stock.mouvementstock.dto.EnteteMouvementResponseDTO;
import com.jospin.gestion_stock.article.entity.Article;
import com.jospin.gestion_stock.mouvementstock.entity.DetailMouvement;
import com.jospin.gestion_stock.mouvementstock.entity.EnteteMouvement;
import com.jospin.gestion_stock.mouvementstock.entity.TypeMouvement;
import com.jospin.gestion_stock.mouvementstock.mapper.EnteteMouvementMapper;
import com.jospin.gestion_stock.article.repository.ArticleRepository;
import com.jospin.gestion_stock.mouvementstock.repository.DetailMouvementRepository;
import com.jospin.gestion_stock.mouvementstock.repository.EnteteMouvementRepository;
import com.jospin.gestion_stock.validations.ValidationErrorType;
import com.jospin.gestion_stock.validations.ValidationException;
import com.jospin.gestion_stock.validations.ValidationHandle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EnteteMouvementServiceImpl implements EnteteMouvementService {

    private final EnteteMouvementRepository enteteMouvementRepository;
    private final DetailMouvementRepository detailMouvementRepository;
    private final ArticleRepository articleRepository;
    private final EnteteMouvementMapper enteteMouvementMapper;
    private final ValidationHandle validationHandle;

    private String generateNumero() {
        long nextId = enteteMouvementRepository.count() + 1001;

        return "EN-" + nextId;
    }

    @Override
    public EnteteMouvementResponseDTO createMouvement(EnteteMouvementRequestDTO requestDTO) {
        log.info("Création d'un mouvement de type: {}", requestDTO.getTypeMouvement());

        validationHandle
                .required("typeMouvement", requestDTO.getTypeMouvement())
                .required("details", requestDTO.getDetails())
                .custom("details",
                        requestDTO.getDetails() != null && !requestDTO.getDetails().isEmpty(),
                        ValidationErrorType.FIELD_INVALID,
                        "Le mouvement doit contenir au moins un article")
                .validate();

        EnteteMouvement entete = enteteMouvementMapper.toEntity(requestDTO);

        entete.setNumero(generateNumero());

        for (DetailMouvementRequestDTO detailDTO : requestDTO.getDetails()) {

            validationHandle
                    .required("idArticle", detailDTO.getIdArticle())
                    .required("quantite", detailDTO.getQuantite())
                    .positive("quantite", detailDTO.getQuantite())
                    .required("prixUnitaire", detailDTO.getPrixUnitaire())
                    .minDecimal("prixUnitaire", detailDTO.getPrixUnitaire(),
                            BigDecimal.valueOf(0.01))
                    .validate();

            Article article = articleRepository.findByIdWithEntrepot(detailDTO.getIdArticle())
                    .orElseThrow(() -> new ValidationException(
                            ValidationErrorType.ARTICLE_NOT_FOUND,
                            "Article non trouvé avec l'ID: " + detailDTO.getIdArticle()
                    ));

            if (requestDTO.getTypeMouvement() == TypeMouvement.SORTIE) {
                Integer stockActuel = getQuantiteEnStock(article.getId());
                if (stockActuel < detailDTO.getQuantite()) {
                    throw new ValidationException(
                            ValidationErrorType.INSUFFICIENT_STOCK,
                            String.format("Stock insuffisant pour l'article '%s' (%s). " +
                                            "Stock actuel: %d, Quantité demandée: %d",
                                    article.getDesignation(),
                                    article.getCode(),
                                    stockActuel,
                                    detailDTO.getQuantite())
                    );
                }
            }

            DetailMouvement detail = DetailMouvement.builder()
                    .article(article)
                    .quantite(detailDTO.getQuantite())
                    .prixUnitaire(detailDTO.getPrixUnitaire())
                    .description(detailDTO.getDescription())
                    .build();
            detail.calculatePrixTotal();
            entete.addDetail(detail);
        }

        EnteteMouvement savedEntete = enteteMouvementRepository.save(entete);

        log.info("Mouvement créé avec succès - ID: {}, Type: {}, Montant total: {}, Nombre d'articles: {}",
                savedEntete.getId(),
                savedEntete.getTypeMouvement(),
                savedEntete.getMontantTotal(),
                savedEntete.getDetails().size());

        return enteteMouvementMapper.toResponseDTO(savedEntete);
    }

    @Override
    @Transactional(readOnly = true)
    public EnteteMouvementResponseDTO getMouvementById(Long id) {
        log.info("Récupération du mouvement ID: {}", id);

        EnteteMouvement entete = enteteMouvementRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ValidationException(
                        ValidationErrorType.MOUVEMENT_NOT_FOUND,
                        "Mouvement non trouvé avec l'ID: " + id
                ));

        return enteteMouvementMapper.toResponseDTO(entete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnteteMouvementResponseDTO> getAllMouvements() {
        log.info("Récupération de tous les mouvements");

        return enteteMouvementRepository.findAllWithDetails()
                .stream()
                .map(enteteMouvementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnteteMouvementResponseDTO> getMouvementsByType(TypeMouvement type) {
        log.info("Récupération des mouvements de type: {}", type);

        validationHandle
                .required("type", type)
                .validate();

        return enteteMouvementRepository.findByTypeMouvementOrderByDateDesc(type)
                .stream()
                .map(enteteMouvementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnteteMouvementResponseDTO> getMouvementsByPeriod(
            LocalDateTime startDate,
            LocalDateTime endDate) {

        log.info("Récupération des mouvements entre {} et {}", startDate, endDate);

        validationHandle
                .required("startDate", startDate)
                .required("endDate", endDate)
                .custom("dates",
                        startDate.isBefore(endDate),
                        ValidationErrorType.FIELD_INVALID,
                        "La date de début doit être avant la date de fin")
                .validate();

        return enteteMouvementRepository.findByDateBetween(startDate, endDate)
                .stream()
                .map(enteteMouvementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMouvement(Long id) {
        log.info("Suppression du mouvement ID: {}", id);

        if (!enteteMouvementRepository.existsById(id)) {
            throw new ValidationException(
                    ValidationErrorType.MOUVEMENT_NOT_FOUND,
                    "Mouvement non trouvé avec l'ID: " + id
            );
        }

        enteteMouvementRepository.deleteById(id);
        log.info("Mouvement supprimé avec succès - ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getQuantiteEnStock(Long articleId) {
        log.debug("Calcul de la quantité en stock pour l'article ID: {}", articleId);

        Integer quantity = detailMouvementRepository.calculateStockByArticle(articleId);
        return quantity != null ? quantity : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleStockResponseDTO getStockByArticle(Long articleId) {
        log.info("Récupération du stock pour l'article ID: {}", articleId);

        validationHandle
                .required("articleId", articleId)
                .validate();

        Article article = articleRepository.findByIdWithEntrepot(articleId)
                .orElseThrow(() -> new ValidationException(
                        ValidationErrorType.ARTICLE_NOT_FOUND,
                        "Article non trouvé avec l'ID: " + articleId
                ));

        return buildArticleStockResponse(article);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleStockResponseDTO> getAllArticlesStock() {
        log.info("Récupération du stock de tous les articles");

        return articleRepository.findAllWithEntrepot()
                .stream()
                .map(this::buildArticleStockResponse)
                .sorted((a, b) -> Integer.compare(b.getQuantiteEnStock(), a.getQuantiteEnStock()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleStockResponseDTO> getStockByEntrepot(Long entrepotId) {
        log.info("Récupération du stock pour l'entrepôt ID: {}", entrepotId);

        validationHandle
                .required("entrepotId", entrepotId)
                .validate();

        return articleRepository.findByEntrepotId(entrepotId)
                .stream()
                .map(this::buildArticleStockResponse)
                .sorted((a, b) -> Integer.compare(b.getQuantiteEnStock(), a.getQuantiteEnStock()))
                .collect(Collectors.toList());
    }

    private ArticleStockResponseDTO buildArticleStockResponse(Article article) {
        Entrepot e = article.getEntrepot();
        Integer quantiteEnStock = getQuantiteEnStock(article.getId());
        Integer nombreMouvements = detailMouvementRepository.findByArticleId(article.getId()).size();
        BigDecimal valeurStock = calculateStockValue(article.getId(), quantiteEnStock);

        return ArticleStockResponseDTO.builder()
                .id(article.getId())
                .article(ArticleResponseDTO.builder()
                        .code(article.getCode())
                        .designation(article.getDesignation())
                        .build()
                )

                .entrepot(EntrepotResponseDTO.builder()
                        .code(e.getCode())
                        .designation(e.getDesignation())
                        .build()
                )
                .quantiteEnStock(quantiteEnStock)
                .valeurStock(valeurStock)
                .nombreMouvements(nombreMouvements)
                .build();
    }

    private BigDecimal calculateStockValue(Long articleId, Integer quantiteEnStock) {
        if (quantiteEnStock == null || quantiteEnStock == 0) {
            return BigDecimal.ZERO;
        }

        List<DetailMouvement> entrees = detailMouvementRepository
                .findByArticleAndType(articleId, TypeMouvement.ENTREE);

        if (entrees.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalValue = BigDecimal.ZERO;
        int totalQuantity = 0;

        for (DetailMouvement entree : entrees) {
            BigDecimal value = entree.getPrixUnitaire()
                    .multiply(BigDecimal.valueOf(entree.getQuantite()));
            totalValue = totalValue.add(value);
            totalQuantity += entree.getQuantite();
        }

        if (totalQuantity == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal pmp = totalValue.divide(
                BigDecimal.valueOf(totalQuantity),
                2,
                RoundingMode.HALF_UP
        );
        return pmp.multiply(BigDecimal.valueOf(quantiteEnStock))
                .setScale(2, RoundingMode.HALF_UP);
    }
}