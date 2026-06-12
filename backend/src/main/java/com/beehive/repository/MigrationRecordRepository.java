package com.beehive.repository;

import com.beehive.entity.MigrationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MigrationRecordRepository extends JpaRepository<MigrationRecord, Long> {

    List<MigrationRecord> findByApiaryId(Long apiaryId);

    List<MigrationRecord> findByApiaryIdAndDepartureDateBetween(Long apiaryId, LocalDate start, LocalDate end);
}
