package com.beehive.repository;

import com.beehive.entity.HoneyHarvest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HoneyHarvestRepository extends JpaRepository<HoneyHarvest, Long> {

    List<HoneyHarvest> findByHive_Id(Long hiveId);

    List<HoneyHarvest> findByHive_IdAndHarvestDateBetween(Long hiveId, LocalDate start, LocalDate end);

    List<HoneyHarvest> findByHive_Apiary_Id(Long apiaryId);
}
