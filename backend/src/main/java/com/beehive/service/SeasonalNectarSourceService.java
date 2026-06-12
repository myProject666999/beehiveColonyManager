package com.beehive.service;

import com.beehive.entity.SeasonalNectarSource;
import com.beehive.entity.Apiary;
import com.beehive.repository.SeasonalNectarSourceRepository;
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
public class SeasonalNectarSourceService {

    private final SeasonalNectarSourceRepository seasonalNectarSourceRepository;
    private final ApiaryRepository apiaryRepository;

    @Transactional(readOnly = true)
    public List<SeasonalNectarSource> findAll() {
        return seasonalNectarSourceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public SeasonalNectarSource findById(Long id) {
        return seasonalNectarSourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SeasonalNectarSource not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<SeasonalNectarSource> findByApiaryId(Long apiaryId) {
        return seasonalNectarSourceRepository.findByApiary_Id(apiaryId);
    }

    public SeasonalNectarSource create(SeasonalNectarSource source) {
        Long apiaryId = source.getApiaryId();
        if (apiaryId == null && source.getApiary() != null && source.getApiary().getId() != null) {
            apiaryId = source.getApiary().getId();
        }
        if (apiaryId == null) {
            throw new IllegalArgumentException("apiaryId is required");
        }
        final Long finalApiaryId = apiaryId;
        Apiary apiary = apiaryRepository.findById(apiaryId)
                .orElseThrow(() -> new EntityNotFoundException("Apiary not found with id: " + finalApiaryId));
        source.setApiary(apiary);
        source.setCreatedAt(LocalDateTime.now());
        source.setUpdatedAt(LocalDateTime.now());
        return seasonalNectarSourceRepository.save(source);
    }

    public SeasonalNectarSource update(Long id, SeasonalNectarSource source) {
        SeasonalNectarSource existing = findById(id);
        existing.setSeason(source.getSeason());
        existing.setNectarPlant(source.getNectarPlant());
        existing.setBloomStart(source.getBloomStart());
        existing.setBloomEnd(source.getBloomEnd());
        existing.setDescription(source.getDescription());
        existing.setUpdatedAt(LocalDateTime.now());
        return seasonalNectarSourceRepository.save(existing);
    }

    public void delete(Long id) {
        SeasonalNectarSource existing = findById(id);
        seasonalNectarSourceRepository.delete(existing);
    }
}
