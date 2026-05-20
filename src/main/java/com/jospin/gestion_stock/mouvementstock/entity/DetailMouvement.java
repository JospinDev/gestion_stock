package com.jospin.gestion_stock.mouvementstock.entity;

import com.jospin.gestion_stock.article.entity.Article;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "detail_mouvements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailMouvement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entete", nullable = false)
    private EnteteMouvement enteteMouvement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_article", nullable = false)
    private Article article;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "prix_unitaire", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(name = "prix_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal prixTotal;

    @Column(length = 500)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        calculatePrixTotal();
    }

    @PreUpdate
    protected void onUpdate() {
        calculatePrixTotal();
    }
    public void calculatePrixTotal() {
        if (quantite != null && prixUnitaire != null) {
            this.prixTotal = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
        }
    }
}
