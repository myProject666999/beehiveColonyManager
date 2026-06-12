package com.beehive.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hives")
public class Hive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apiary_id", nullable = false)
    private Apiary apiary;

    @Transient
    private Long apiaryId;

    @Column(name = "hive_number", nullable = false)
    private String hiveNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "queen_source")
    private QueenSource queenSource;

    @Column(name = "queen_year")
    private Integer queenYear;

    @Column(name = "queen_breed")
    private String queenBreed;

    @Column(name = "worker_bee_count")
    private Integer workerBeeCount;

    @Column(name = "frame_count")
    private Integer frameCount;

    @Enumerated(EnumType.STRING)
    private HiveStatus status;

    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "hive", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InspectionRecord> inspectionRecords = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "hive", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HoneyHarvest> honeyHarvests = new ArrayList<>();

    public enum QueenSource {
        self_bred, purchased
    }

    public enum HiveStatus {
        active, weak, dead
    }
}
