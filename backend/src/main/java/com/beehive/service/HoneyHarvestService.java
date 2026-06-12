package com.beehive.service;

import com.beehive.entity.HoneyHarvest;
import com.beehive.entity.Hive;
import com.beehive.repository.HoneyHarvestRepository;
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
public class HoneyHarvestService {

    private final HoneyHarvestRepository honeyHarvestRepository;
    private final HiveRepository hiveRepository;

    @Transactional(readOnly = true)
    public List<HoneyHarvest> findAll() {
        return honeyHarvestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public HoneyHarvest findById(Long id) {
        return honeyHarvestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("HoneyHarvest not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<HoneyHarvest> findByHiveId(Long hiveId) {
        return honeyHarvestRepository.findByHive_Id(hiveId);
    }

    public HoneyHarvest create(HoneyHarvest harvest) {
        Long hiveId = harvest.getHiveId();
        if (hiveId == null && harvest.getHive() != null && harvest.getHive().getId() != null) {
            hiveId = harvest.getHive().getId();
        }
        if (hiveId == null) {
            throw new IllegalArgumentException("hiveId is required");
        }
        final Long finalHiveId = hiveId;
        Hive hive = hiveRepository.findById(hiveId)
                .orElseThrow(() -> new EntityNotFoundException("Hive not found with id: " + finalHiveId));
        harvest.setHive(hive);
        harvest.setCreatedAt(LocalDateTime.now());
        harvest.setUpdatedAt(LocalDateTime.now());
        return honeyHarvestRepository.save(harvest);
    }

    public HoneyHarvest update(Long id, HoneyHarvest harvest) {
        HoneyHarvest existing = findById(id);
        existing.setHarvestDate(harvest.getHarvestDate());
        existing.setWeight(harvest.getWeight());
        existing.setWaterContent(harvest.getWaterContent());
        existing.setNectarSource(harvest.getNectarSource());
        existing.setQualityGrade(harvest.getQualityGrade());
        existing.setNotes(harvest.getNotes());
        existing.setUpdatedAt(LocalDateTime.now());
        return honeyHarvestRepository.save(existing);
    }

    public void delete(Long id) {
        HoneyHarvest existing = findById(id);
        honeyHarvestRepository.delete(existing);
    }
}
