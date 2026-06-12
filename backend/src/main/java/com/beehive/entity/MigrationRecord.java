package com.beehive.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "migration_records")
public class MigrationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apiary_id", nullable = false)
    private Apiary apiary;

    @Column(name = "departure_location")
    private String departureLocation;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    private String destination;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "transport_vehicle")
    private String transportVehicle;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "driver_phone")
    private String driverPhone;

    @Column(name = "hive_count")
    private Integer hiveCount;

    private String reason;

    @Column(name = "distance_km")
    private Double distanceKm;

    private Double cost;

    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
