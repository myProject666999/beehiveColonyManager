package com.beehive.repository;

import com.beehive.entity.SeasonalNectarSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeasonalNectarSourceRepository extends JpaRepository<SeasonalNectarSource, Long> {

    List<SeasonalNectarSource> findByApiary_Id(Long apiaryId);

    List<SeasonalNectarSource> findByApiary_IdAndSeason(Long apiaryId, String season);
}
