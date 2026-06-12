package com.beehive.service;

import com.beehive.entity.MigrationRecord;
import com.beehive.entity.Apiary;
import com.beehive.repository.MigrationRecordRepository;
import com.beehive.repository.ApiaryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MigrationRecordService {

    private final MigrationRecordRepository migrationRecordRepository;
    private final ApiaryRepository apiaryRepository;

    @Transactional(readOnly = true)
    public List<MigrationRecord> findAll() {
        return migrationRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MigrationRecord findById(Long id) {
        return migrationRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MigrationRecord not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<MigrationRecord> findByApiaryId(Long apiaryId) {
        return migrationRecordRepository.findByApiaryId(apiaryId);
    }

    public MigrationRecord create(MigrationRecord record) {
        Apiary apiary = apiaryRepository.findById(record.getApiary().getId())
                .orElseThrow(() -> new EntityNotFoundException("Apiary not found with id: " + record.getApiary().getId()));
        record.setApiary(apiary);
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        return migrationRecordRepository.save(record);
    }

    public MigrationRecord update(Long id, MigrationRecord record) {
        MigrationRecord existing = findById(id);
        existing.setDepartureLocation(record.getDepartureLocation());
        existing.setDepartureDate(record.getDepartureDate());
        existing.setDestination(record.getDestination());
        existing.setArrivalDate(record.getArrivalDate());
        existing.setTransportVehicle(record.getTransportVehicle());
        existing.setDriverName(record.getDriverName());
        existing.setDriverPhone(record.getDriverPhone());
        existing.setHiveCount(record.getHiveCount());
        existing.setReason(record.getReason());
        existing.setDistanceKm(record.getDistanceKm());
        existing.setCost(record.getCost());
        existing.setNotes(record.getNotes());
        existing.setUpdatedAt(LocalDateTime.now());
        return migrationRecordRepository.save(existing);
    }

    public void delete(Long id) {
        MigrationRecord existing = findById(id);
        migrationRecordRepository.delete(existing);
    }
}
