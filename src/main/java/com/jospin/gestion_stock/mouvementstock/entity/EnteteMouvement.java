package com.jospin.gestion_stock.mouvementstock.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entete_mouvements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnteteMouvement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, unique = true, length = 20)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_mouvement", nullable = false, length = 20)
    private TypeMouvement typeMouvement;

    @Column(name = "montant_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal montantTotal;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "enteteMouvement", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetailMouvement> details = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (date == null) {
            date = LocalDateTime.now();
        }
        calculateMontantTotal();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateMontantTotal();
    }
    public void addDetail(DetailMouvement detail) {
        details.add(detail);
        detail.setEnteteMouvement(this);
        calculateMontantTotal();
    }

    public void removeDetail(DetailMouvement detail) {
        details.remove(detail);
        detail.setEnteteMouvement(null);
        calculateMontantTotal();
    }
    public void calculateMontantTotal() {
        this.montantTotal = details.stream()
                .map(detail ->
                        detail.getPrixUnitaire().multiply(BigDecimal.valueOf(detail.getQuantite()))
                ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
