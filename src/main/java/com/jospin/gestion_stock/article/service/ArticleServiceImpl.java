package com.jospin.gestion_stock.article.service;

import com.jospin.gestion_stock.article.dto.ArticleRequestDTO;
import com.jospin.gestion_stock.article.dto.ArticleResponseDTO;
import com.jospin.gestion_stock.article.entity.Article;
import com.jospin.gestion_stock.entrepot.entity.Entrepot;
import com.jospin.gestion_stock.article.mapper.ArticleMapper;
import com.jospin.gestion_stock.article.repository.ArticleRepository;
import com.jospin.gestion_stock.entrepot.repository.EntrepotRepository;
import com.jospin.gestion_stock.validations.ValidationErrorType;
import com.jospin.gestion_stock.validations.ValidationException;
import com.jospin.gestion_stock.validations.ValidationHandle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final EntrepotRepository entrepotRepository;
    private final ArticleMapper articleMapper;
    private final ValidationHandle validationHandle;

    @Override
    public ArticleResponseDTO createArticle(ArticleRequestDTO requestDTO) {
        log.info("Création d'un article avec le code: {} pour l'entrepôt ID: {}",
                requestDTO.getCode(), requestDTO.getIdEntrepot());

        validationHandle
                .required("code", requestDTO.getCode())
                .minLength("code", requestDTO.getCode(), 2)
                .maxLength("code", requestDTO.getCode(), 50)
                .validCode("code", requestDTO.getCode())
                .required("designation", requestDTO.getDesignation())
                .minLength("designation", requestDTO.getDesignation(), 3)
                .maxLength("designation", requestDTO.getDesignation(), 200)
                .required("idEntrepot", requestDTO.getIdEntrepot())
                .validate();

        Entrepot entrepot = entrepotRepository.findById(requestDTO.getIdEntrepot())
                .orElseThrow(() -> new ValidationException(
                        "idEntrepot",
                        ValidationErrorType.ENTREPOT_NOT_FOUND,
                        "Entrepôt non trouvé avec l'ID: " + requestDTO.getIdEntrepot()
                ));

        if (articleRepository.existsByCodeAndEntrepotId(
                requestDTO.getCode().toUpperCase(),
                requestDTO.getIdEntrepot())) {
            throw new ValidationException(
                    "code",
                    ValidationErrorType.ARTICLE_CODE_EXISTS_IN_ENTREPOT,
                    "Un article avec le code '" + requestDTO.getCode() +
                            "' existe déjà dans cet entrepôt"
            );
        }

        Article article = articleMapper.toEntity(requestDTO, entrepot);
        Article savedArticle = articleRepository.save(article);

        log.info("Article créé avec succès avec l'ID: {}", savedArticle.getId());

        return articleMapper.toResponseDTO(savedArticle);
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleResponseDTO getArticleById(Long id) {
        log.info("Récupération de l'article avec l'ID: {}", id);

        Article article = articleRepository.findByIdWithEntrepot(id)
                .orElseThrow(() -> new ValidationException(
                        ValidationErrorType.ARTICLE_NOT_FOUND,
                        "Article non trouvé avec l'ID: " + id
                ));

        return articleMapper.toResponseDTO(article);
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleResponseDTO getArticleByCodeAndEntrepot(String code, Long entrepotId) {
        log.info("Récupération de l'article avec le code: {} et entrepôt ID: {}", code, entrepotId);

        validationHandle
                .required("code", code)
                .required("entrepotId", entrepotId)
                .validate();

        Article article = articleRepository.findByCodeAndEntrepotId(code.toUpperCase(), entrepotId)
                .orElseThrow(() -> new ValidationException(
                        ValidationErrorType.ARTICLE_NOT_FOUND,
                        "Article non trouvé avec le code: " + code + " dans l'entrepôt ID: " + entrepotId
                ));

        return articleMapper.toResponseDTO(article);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleResponseDTO> getAllArticles() {
        log.info("Récupération de tous les articles");

        return articleRepository.findAllWithEntrepot()
                .stream()
                .map(articleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleResponseDTO> getArticlesByEntrepot(Long entrepotId) {
        log.info("Récupération des articles de l'entrepôt ID: {}", entrepotId);

        validationHandle
                .required("entrepotId", entrepotId)
                .validate();

        if (!entrepotRepository.existsById(entrepotId)) {
            throw new ValidationException(
                    ValidationErrorType.ENTREPOT_NOT_FOUND,
                    "Entrepôt non trouvé avec l'ID: " + entrepotId
            );
        }

        return articleRepository.findByEntrepotId(entrepotId)
                .stream()
                .map(articleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ArticleResponseDTO updateArticle(Long id, ArticleRequestDTO requestDTO) {
        log.info("Mise à jour de l'article avec l'ID: {}", id);

        Article article = articleRepository.findByIdWithEntrepot(id)
                .orElseThrow(() -> new ValidationException(
                        ValidationErrorType.ARTICLE_NOT_FOUND,
                        "Article non trouvé avec l'ID: " + id
                ));

        validationHandle
                .required("code", requestDTO.getCode())
                .minLength("code", requestDTO.getCode(), 2)
                .maxLength("code", requestDTO.getCode(), 50)
                .validCode("code", requestDTO.getCode())
                .required("designation", requestDTO.getDesignation())
                .minLength("designation", requestDTO.getDesignation(), 3)
                .required("idEntrepot", requestDTO.getIdEntrepot())
                .validate();

        Entrepot entrepot = entrepotRepository.findById(requestDTO.getIdEntrepot())
                .orElseThrow(() -> new ValidationException(
                        "idEntrepot",
                        ValidationErrorType.ENTREPOT_NOT_FOUND,
                        "Entrepôt non trouvé avec l'ID: " + requestDTO.getIdEntrepot()
                ));

        if ((!article.getCode().equals(requestDTO.getCode().toUpperCase()) ||
                !article.getEntrepot().getId().equals(requestDTO.getIdEntrepot())) &&
                articleRepository.existsByCodeAndEntrepotId(
                        requestDTO.getCode().toUpperCase(),
                        requestDTO.getIdEntrepot())) {
            throw new ValidationException(
                    "code",
                    ValidationErrorType.ARTICLE_CODE_EXISTS_IN_ENTREPOT,
                    "Un article avec le code '" + requestDTO.getCode() +
                            "' existe déjà dans cet entrepôt"
            );
        }

        articleMapper.updateEntityFromDTO(requestDTO, article, entrepot);
        Article updatedArticle = articleRepository.save(article);

        log.info("Article mis à jour avec succès avec l'ID: {}", updatedArticle.getId());

        return articleMapper.toResponseDTO(updatedArticle);
    }

    @Override
    public void deleteArticle(Long id) {
        log.info("Suppression de l'article avec l'ID: {}", id);

        if (!articleRepository.existsById(id)) {
            throw new ValidationException(
                    ValidationErrorType.ARTICLE_NOT_FOUND,
                    "Article non trouvé avec l'ID: " + id
            );
        }

        articleRepository.deleteById(id);
        log.info("Article supprimé avec succès avec l'ID: {}", id);
    }
}
