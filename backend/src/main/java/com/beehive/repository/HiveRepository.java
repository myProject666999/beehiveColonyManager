package com.beehive.repository;

import com.beehive.entity.Hive;
import com.beehive.entity.Hive.HiveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HiveRepository extends JpaRepository<Hive, Long> {

    List<Hive> findByApiary_Id(Long apiaryId);

    List<Hive> findByApiary_IdAndStatus(Long apiaryId, HiveStatus status);

    List<Hive> findByStatus(HiveStatus status);
}
