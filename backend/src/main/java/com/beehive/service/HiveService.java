package com.beehive.service;

import com.beehive.entity.Hive;
import com.beehive.entity.Apiary;
import com.beehive.repository.HiveRepository;
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
public class HiveService {

    private final HiveRepository hiveRepository;
    private final ApiaryRepository apiaryRepository;

    @Transactional(readOnly = true)
    public List<Hive> findAll() {
        return hiveRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Hive findById(Long id) {
        return hiveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hive not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Hive> findByApiaryId(Long apiaryId) {
        return hiveRepository.findByApiary_Id(apiaryId);
    }

    public Hive create(Hive hive) {
        Long apiaryId = hive.getApiaryId();
        if (apiaryId == null && hive.getApiary() != null && hive.getApiary().getId() != null) {
            apiaryId = hive.getApiary().getId();
        }
        if (apiaryId == null) {
            throw new IllegalArgumentException("apiaryId is required");
        }
        final Long finalApiaryId = apiaryId;
        Apiary apiary = apiaryRepository.findById(apiaryId)
                .orElseThrow(() -> new EntityNotFoundException("Apiary not found with id: " + finalApiaryId));
        hive.setApiary(apiary);
        hive.setCreatedAt(LocalDateTime.now());
        hive.setUpdatedAt(LocalDateTime.now());
        return hiveRepository.save(hive);
    }

    public Hive update(Long id, Hive hive) {
        Hive existing = findById(id);
        existing.setHiveNumber(hive.getHiveNumber());
        existing.setQueenSource(hive.getQueenSource());
        existing.setQueenYear(hive.getQueenYear());
        existing.setQueenBreed(hive.getQueenBreed());
        existing.setWorkerBeeCount(hive.getWorkerBeeCount());
        existing.setFrameCount(hive.getFrameCount());
        existing.setStatus(hive.getStatus());
        existing.setDescription(hive.getDescription());
        existing.setUpdatedAt(LocalDateTime.now());
        return hiveRepository.save(existing);
    }

    public void delete(Long id) {
        Hive existing = findById(id);
        hiveRepository.delete(existing);
    }
}
