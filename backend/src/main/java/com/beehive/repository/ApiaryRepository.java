package com.beehive.repository;

import com.beehive.entity.Apiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiaryRepository extends JpaRepository<Apiary, Long> {

    List<Apiary> findByLocationContaining(String location);

    List<Apiary> findByMainNectarPlantContaining(String mainNectarPlant);
}
