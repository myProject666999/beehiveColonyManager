package com.beehive.service;

import com.beehive.entity.Apiary;
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
public class ApiaryService {

    private final ApiaryRepository apiaryRepository;

    @Transactional(readOnly = true)
    public List<Apiary> findAll() {
        return apiaryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Apiary findById(Long id) {
        return apiaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Apiary not found with id: " + id));
    }

    public Apiary create(Apiary apiary) {
        apiary.setCreatedAt(LocalDateTime.now());
        apiary.setUpdatedAt(LocalDateTime.now());
        return apiaryRepository.save(apiary);
    }

    public Apiary update(Long id, Apiary apiary) {
        Apiary existing = findById(id);
        existing.setName(apiary.getName());
        existing.setLocation(apiary.getLocation());
        existing.setLatitude(apiary.getLatitude());
        existing.setLongitude(apiary.getLongitude());
        existing.setMainNectarPlant(apiary.getMainNectarPlant());
        existing.setAreaSize(apiary.getAreaSize());
        existing.setDescription(apiary.getDescription());
        existing.setUpdatedAt(LocalDateTime.now());
        return apiaryRepository.save(existing);
    }

    public void delete(Long id) {
        Apiary existing = findById(id);
        apiaryRepository.delete(existing);
    }
}
