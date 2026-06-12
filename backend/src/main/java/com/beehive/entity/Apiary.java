package com.beehive.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "apiaries")
public class Apiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;

    private Double latitude;

    private Double longitude;

    @Column(name = "main_nectar_plant")
    private String mainNectarPlant;

    @Column(name = "area_size")
    private Double areaSize;

    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "apiary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SeasonalNectarSource> seasonalNectarSources = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "apiary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Hive> hives = new ArrayList<>();
}
