package com.beehive.repository;

import com.beehive.entity.InspectionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InspectionRecordRepository extends JpaRepository<InspectionRecord, Long> {

    List<InspectionRecord> findByHive_Id(Long hiveId);

    List<InspectionRecord> findByHive_IdAndInspectionDateBetween(Long hiveId, LocalDate start, LocalDate end);

    List<InspectionRecord> findByHive_Apiary_Id(Long apiaryId);
}
