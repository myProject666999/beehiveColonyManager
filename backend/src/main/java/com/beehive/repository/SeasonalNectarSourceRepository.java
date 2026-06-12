package com.beehive.repository;

import com.beehive.entity.SeasonalNectarSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeasonalNectarSourceRepository extends JpaRepository<SeasonalNectarSource, Long> {

    List<SeasonalNectarSource> findByApiaryId(Long apiaryId);

    List<SeasonalNectarSource> findByApiaryIdAndSeason(Long apiaryId, String season);
}
