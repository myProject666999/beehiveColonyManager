package com.beehive.repository;

import com.beehive.entity.HoneyHarvest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HoneyHarvestRepository extends JpaRepository<HoneyHarvest, Long> {

    List<HoneyHarvest> findByHiveId(Long hiveId);

    List<HoneyHarvest> findByHiveIdAndHarvestDateBetween(Long hiveId, LocalDate start, LocalDate end);

    List<HoneyHarvest> findByHiveApiaryId(Long apiaryId);
}
