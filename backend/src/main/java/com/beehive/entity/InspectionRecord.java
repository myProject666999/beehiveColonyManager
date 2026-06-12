package com.beehive.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inspection_records")
public class InspectionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hive_id", nullable = false)
    private Hive hive;

    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;

    @Column(name = "queen_present")
    private Boolean queenPresent;

    @Column(name = "has_mites")
    private Boolean hasMites;

    @Column(name = "has_disease")
    private Boolean hasDisease;

    @Column(name = "disease_detail")
    private String diseaseDetail;

    @Column(name = "honey_store")
    private Double honeyStore;

    @Enumerated(EnumType.STRING)
    @Column(name = "brood_condition")
    private BroodCondition broodCondition;

    @Enumerated(EnumType.STRING)
    private Temper temper;

    @Enumerated(EnumType.STRING)
    @Column(name = "overall_condition")
    private OverallCondition overallCondition;

    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum BroodCondition {
        good, fair, poor
    }

    public enum Temper {
        gentle, normal, aggressive
    }

    public enum OverallCondition {
        strong, medium, weak
    }
}
