package com.beehive.repository;

import com.beehive.entity.InspectionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InspectionRecordRepository extends JpaRepository<InspectionRecord, Long> {

    List<InspectionRecord> findByHiveId(Long hiveId);

    List<InspectionRecord> findByHiveIdAndInspectionDateBetween(Long hiveId, LocalDate start, LocalDate end);

    List<InspectionRecord> findByHiveApiaryId(Long apiaryId);
}
