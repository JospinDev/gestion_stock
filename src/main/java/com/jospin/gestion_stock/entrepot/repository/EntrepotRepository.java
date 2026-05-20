package com.jospin.gestion_stock.entrepot.repository;

import com.jospin.gestion_stock.entrepot.entity.Entrepot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EntrepotRepository extends JpaRepository<Entrepot, Long> {

    Optional<Entrepot> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT COUNT(a) > 0 FROM Article a WHERE a.entrepot.id = :entrepotId")
    boolean hasArticles(Long entrepotId);
}
