package com.beehive.repository;

import com.beehive.entity.MigrationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MigrationRecordRepository extends JpaRepository<MigrationRecord, Long> {

    List<MigrationRecord> findByApiary_Id(Long apiaryId);

    List<MigrationRecord> findByApiary_IdAndDepartureDateBetween(Long apiaryId, LocalDate start, LocalDate end);
}
