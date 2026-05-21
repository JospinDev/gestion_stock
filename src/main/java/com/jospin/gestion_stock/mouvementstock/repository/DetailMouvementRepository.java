package com.jospin.gestion_stock.mouvementstock.repository;

import com.jospin.gestion_stock.mouvementstock.entity.DetailMouvement;
import com.jospin.gestion_stock.mouvementstock.entity.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailMouvementRepository extends JpaRepository<DetailMouvement, Long> {
    List<DetailMouvement> findByArticleId(Long articleId);

    List<DetailMouvement> findByEnteteMouvementId(Long enteteMouvementId);

    @Query("SELECT d FROM DetailMouvement d " +
            "WHERE d.article.id = :articleId " +
            "AND d.enteteMouvement.typeMouvement = :type")
    List<DetailMouvement> findByArticleAndType(
            @Param("articleId") Long articleId,
            @Param("type") TypeMouvement type
    );

    @Query("SELECT COALESCE(SUM(CASE WHEN d.enteteMouvement.typeMouvement = 'ENTREE' " +
            "THEN d.quantite ELSE 0 END), 0) - " +
            "COALESCE(SUM(CASE WHEN d.enteteMouvement.typeMouvement = 'SORTIE' " +
            "THEN d.quantite ELSE 0 END), 0) " +
            "FROM DetailMouvement d WHERE d.article.id = :articleId")
    Integer calculateStockByArticle(@Param("articleId") Long articleId);

    @Query("SELECT d FROM DetailMouvement d " +
            "LEFT JOIN FETCH d.enteteMouvement e " +
            "LEFT JOIN FETCH d.article a " +
            "WHERE a.entrepot.id = :entrepotId " +
            "ORDER BY e.date DESC")
    List<DetailMouvement> findByEntrepot(@Param("entrepotId") Long entrepotId);
}