package com.jospin.gestion_stock.mouvementstock.repository;

import com.jospin.gestion_stock.mouvementstock.entity.EnteteMouvement;
import com.jospin.gestion_stock.mouvementstock.entity.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnteteMouvementRepository extends JpaRepository<EnteteMouvement, Long> {

    List<EnteteMouvement> findByTypeMouvementOrderByDateDesc(TypeMouvement typeMouvement);
    Optional<EnteteMouvement> findTopByOrderByIdDesc();
    List<EnteteMouvement> findAllByOrderByDateDesc();

    @Query("SELECT e FROM EnteteMouvement e " +
            "LEFT JOIN FETCH e.details d " +
            "LEFT JOIN FETCH d.article a " +
            "LEFT JOIN FETCH a.entrepot " +
            "WHERE e.id = :id")
    Optional<EnteteMouvement> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT e FROM EnteteMouvement e " +
            "LEFT JOIN FETCH e.details d " +
            "LEFT JOIN FETCH d.article a " +
            "LEFT JOIN FETCH a.entrepot " +
            "ORDER BY e.date DESC")
    List<EnteteMouvement> findAllWithDetails();

    @Query("SELECT e FROM EnteteMouvement e " +
            "WHERE e.date BETWEEN :startDate AND :endDate " +
            "ORDER BY e.date DESC")
    List<EnteteMouvement> findByDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
