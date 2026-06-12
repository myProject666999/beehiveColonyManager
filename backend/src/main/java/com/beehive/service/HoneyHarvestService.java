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
        return honeyHarvestRepository.findByHiveId(hiveId);
    }

    public HoneyHarvest create(HoneyHarvest harvest) {
        Hive hive = hiveRepository.findById(harvest.getHive().getId())
                .orElseThrow(() -> new EntityNotFoundException("Hive not found with id: " + harvest.getHive().getId()));
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
