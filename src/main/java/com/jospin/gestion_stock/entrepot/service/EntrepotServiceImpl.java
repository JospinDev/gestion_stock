package com.jospin.gestion_stock.entrepot.service;

import com.jospin.gestion_stock.entrepot.dto.EntrepotRequestDTO;
import com.jospin.gestion_stock.entrepot.dto.EntrepotResponseDTO;
import com.jospin.gestion_stock.entrepot.entity.Entrepot;
import com.jospin.gestion_stock.entrepot.mapper.EntrepotMapper;
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
public class EntrepotServiceImpl implements EntrepotService {

    private final EntrepotRepository entrepotRepository;
    private final EntrepotMapper entrepotMapper;
    private final ValidationHandle validationHandle;

    @Override
    public EntrepotResponseDTO createEntrepot(EntrepotRequestDTO requestDTO) {
        log.info("Création d'un entrepôt avec le code: {}", requestDTO.getCode());

        validationHandle
                .required("code", requestDTO.getCode())
                .minLength("code", requestDTO.getCode(), 2)
                .maxLength("code", requestDTO.getCode(), 50)
                .validCode("code", requestDTO.getCode())
                .required("designation", requestDTO.getDesignation())
                .minLength("designation", requestDTO.getDesignation(), 3)
                .maxLength("designation", requestDTO.getDesignation(), 200)
                .validate();

        if (entrepotRepository.existsByCode(requestDTO.getCode().toUpperCase())) {
            throw new ValidationException(
                    "code",
                    ValidationErrorType.CODE_ALREADY_EXISTS,
                    "Un entrepôt avec le code '" + requestDTO.getCode() + "' existe déjà"
            );
        }

        Entrepot entrepot = entrepotMapper.toEntity(requestDTO);
        Entrepot savedEntrepot = entrepotRepository.save(entrepot);

        log.info("Entrepôt créé avec succès avec l'ID: {}", savedEntrepot.getId());

        return entrepotMapper.toResponseDTO(savedEntrepot);
    }

    @Override
    @Transactional(readOnly = true)
    public EntrepotResponseDTO getEntrepotById(Long id) {
        log.info("Récupération de l'entrepôt avec l'ID: {}", id);

        Entrepot entrepot = entrepotRepository.findById(id)
                .orElseThrow(() -> new ValidationException(
                        ValidationErrorType.ENTREPOT_NOT_FOUND,
                        "Entrepôt non trouvé avec l'ID: " + id
                ));

        return entrepotMapper.toResponseDTO(entrepot);
    }

    @Override
    @Transactional(readOnly = true)
    public EntrepotResponseDTO getEntrepotByCode(String code) {
        log.info("Récupération de l'entrepôt avec le code: {}", code);

        validationHandle
                .required("code", code)
                .validate();

        Entrepot entrepot = entrepotRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new ValidationException(
                        ValidationErrorType.ENTREPOT_NOT_FOUND,
                        "Entrepôt non trouvé avec le code: " + code
                ));

        return entrepotMapper.toResponseDTO(entrepot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntrepotResponseDTO> getAllEntrepots() {
        log.info("Récupération de tous les entrepôts");

        return entrepotRepository.findAll()
                .stream()
                .map(entrepotMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EntrepotResponseDTO updateEntrepot(Long id, EntrepotRequestDTO requestDTO) {
        log.info("Mise à jour de l'entrepôt avec l'ID: {}", id);

        Entrepot entrepot = entrepotRepository.findById(id)
                .orElseThrow(() -> new ValidationException(
                        ValidationErrorType.ENTREPOT_NOT_FOUND,
                        "Entrepôt non trouvé avec l'ID: " + id
                ));

        validationHandle
                .required("code", requestDTO.getCode())
                .minLength("code", requestDTO.getCode(), 2)
                .maxLength("code", requestDTO.getCode(), 50)
                .validCode("code", requestDTO.getCode())
                .required("designation", requestDTO.getDesignation())
                .minLength("designation", requestDTO.getDesignation(), 3)
                .validate();

        if (!entrepot.getCode().equals(requestDTO.getCode().toUpperCase()) &&
                entrepotRepository.existsByCode(requestDTO.getCode().toUpperCase())) {
            throw new ValidationException(
                    "code",
                    ValidationErrorType.CODE_ALREADY_EXISTS,
                    "Un entrepôt avec le code '" + requestDTO.getCode() + "' existe déjà"
            );
        }

        entrepotMapper.updateEntityFromDTO(requestDTO, entrepot);
        Entrepot updatedEntrepot = entrepotRepository.save(entrepot);

        log.info("Entrepôt mis à jour avec succès avec l'ID: {}", updatedEntrepot.getId());

        return entrepotMapper.toResponseDTO(updatedEntrepot);
    }

    @Override
    public void deleteEntrepot(Long id) {
        log.info("Suppression de l'entrepôt avec l'ID: {}", id);

        if (!entrepotRepository.existsById(id)) {
            throw new ValidationException(
                    ValidationErrorType.ENTREPOT_NOT_FOUND,
                    "Entrepôt non trouvé avec l'ID: " + id
            );
        }

        if (entrepotRepository.hasArticles(id)) {
            throw new ValidationException(
                    ValidationErrorType.ENTREPOT_HAS_ARTICLES,
                    "Impossible de supprimer l'entrepôt car il contient des articles"
            );
        }

        entrepotRepository.deleteById(id);
        log.info("Entrepôt supprimé avec succès avec l'ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return entrepotRepository.existsByCode(code.toUpperCase());
    }
}
