package com.jospin.gestion_stock.mouvementstock.service;

import com.jospin.gestion_stock.mouvementstock.dto.DetailMouvementResponseDTO;
import com.jospin.gestion_stock.mouvementstock.entity.DetailMouvement;
import com.jospin.gestion_stock.mouvementstock.mapper.DetailMouvementMapper;
import com.jospin.gestion_stock.mouvementstock.repository.DetailMouvementRepository;
import com.jospin.gestion_stock.mouvementstock.service.DetailMouvementService;
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
@Transactional(readOnly = true)
public class DetailMouvementServiceImpl implements DetailMouvementService {

    private final DetailMouvementRepository detailMouvementRepository;
    private final DetailMouvementMapper detailMouvementMapper;
    private final ValidationHandle validationHandle;

    @Override
    public DetailMouvementResponseDTO getDetailById(Long id) {
        log.info("Récupération du détail mouvement ID: {}", id);

        DetailMouvement detail = detailMouvementRepository.findById(id)
                .orElseThrow(() -> new ValidationException(
                        ValidationErrorType.MOUVEMENT_NOT_FOUND,
                        "Détail mouvement non trouvé avec l'ID: " + id
                ));

        return detailMouvementMapper.toResponseDTO(detail);
    }

    @Override
    public List<DetailMouvementResponseDTO> getDetailsByMouvement(Long mouvementId) {
        log.info("Récupération des détails pour le mouvement ID: {}", mouvementId);

        validationHandle
                .required("mouvementId", mouvementId)
                .validate();

        return detailMouvementRepository.findByEnteteMouvementId(mouvementId)
                .stream()
                .map(detailMouvementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailMouvementResponseDTO> getDetailsByArticle(Long articleId) {
        log.info("Récupération des détails pour l'article ID: {}", articleId);

        validationHandle
                .required("articleId", articleId)
                .validate();

        return detailMouvementRepository.findByArticleId(articleId)
                .stream()
                .map(detailMouvementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailMouvementResponseDTO> getDetailsByEntrepot(Long entrepotId) {
        log.info("Récupération des détails pour l'entrepôt ID: {}", entrepotId);

        validationHandle
                .required("entrepotId", entrepotId)
                .validate();

        return detailMouvementRepository.findByEntrepot(entrepotId)
                .stream()
                .map(detailMouvementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
