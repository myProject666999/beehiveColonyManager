package com.beehive.service;

import com.beehive.entity.InspectionRecord;
import com.beehive.entity.Hive;
import com.beehive.repository.InspectionRecordRepository;
import com.beehive.repository.HiveRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InspectionRecordService {

    private final InspectionRecordRepository inspectionRecordRepository;
    private final HiveRepository hiveRepository;

    @Transactional(readOnly = true)
    public List<InspectionRecord> findAll() {
        return inspectionRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public InspectionRecord findById(Long id) {
        return inspectionRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("InspectionRecord not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<InspectionRecord> findByHiveId(Long hiveId) {
        return inspectionRecordRepository.findByHive_Id(hiveId);
    }

    public InspectionRecord create(InspectionRecord record) {
        Long hiveId = record.getHiveId();
        if (hiveId == null && record.getHive() != null && record.getHive().getId() != null) {
            hiveId = record.getHive().getId();
        }
        if (hiveId == null) {
            throw new IllegalArgumentException("hiveId is required");
        }
        final Long finalHiveId = hiveId;
        Hive hive = hiveRepository.findById(hiveId)
                .orElseThrow(() -> new EntityNotFoundException("Hive not found with id: " + finalHiveId));
        record.setHive(hive);
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        return inspectionRecordRepository.save(record);
    }

    public InspectionRecord update(Long id, InspectionRecord record) {
        InspectionRecord existing = findById(id);
        existing.setInspectionDate(record.getInspectionDate());
        existing.setQueenPresent(record.getQueenPresent());
        existing.setHasMites(record.getHasMites());
        existing.setHasDisease(record.getHasDisease());
        existing.setDiseaseDetail(record.getDiseaseDetail());
        existing.setHoneyStore(record.getHoneyStore());
        existing.setBroodCondition(record.getBroodCondition());
        existing.setTemper(record.getTemper());
        existing.setOverallCondition(record.getOverallCondition());
        existing.setNotes(record.getNotes());
        existing.setUpdatedAt(LocalDateTime.now());
        return inspectionRecordRepository.save(existing);
    }

    public void delete(Long id) {
        InspectionRecord existing = findById(id);
        inspectionRecordRepository.delete(existing);
    }
}
