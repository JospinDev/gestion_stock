package com.jospin.gestion_stock.entrepot.service;

import com.jospin.gestion_stock.entrepot.dto.EntrepotRequestDTO;
import com.jospin.gestion_stock.entrepot.dto.EntrepotResponseDTO;
import java.util.List;

public interface EntrepotService {

    EntrepotResponseDTO createEntrepot(EntrepotRequestDTO requestDTO);

    EntrepotResponseDTO getEntrepotById(Long id);

    EntrepotResponseDTO getEntrepotByCode(String code);

    List<EntrepotResponseDTO> getAllEntrepots();

    EntrepotResponseDTO updateEntrepot(Long id, EntrepotRequestDTO requestDTO);

    void deleteEntrepot(Long id);

    boolean existsByCode(String code);
}
