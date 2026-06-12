package com.beehive.controller;

import com.beehive.entity.HoneyHarvest;
import com.beehive.service.HoneyHarvestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HoneyHarvestController {

    private final HoneyHarvestService honeyHarvestService;

    @GetMapping("/api/harvests")
    public ResponseEntity<List<HoneyHarvest>> findAll() {
        return ResponseEntity.ok(honeyHarvestService.findAll());
    }

    @GetMapping("/api/harvests/{id}")
    public ResponseEntity<HoneyHarvest> findById(@PathVariable Long id) {
        return ResponseEntity.ok(honeyHarvestService.findById(id));
    }

    @GetMapping("/api/hives/{hiveId}/harvests")
    public ResponseEntity<List<HoneyHarvest>> findByHiveId(@PathVariable Long hiveId) {
        return ResponseEntity.ok(honeyHarvestService.findByHiveId(hiveId));
    }

    @PostMapping("/api/harvests")
    public ResponseEntity<HoneyHarvest> create(@RequestBody HoneyHarvest harvest) {
        return ResponseEntity.ok(honeyHarvestService.create(harvest));
    }

    @PutMapping("/api/harvests/{id}")
    public ResponseEntity<HoneyHarvest> update(@PathVariable Long id, @RequestBody HoneyHarvest harvest) {
        return ResponseEntity.ok(honeyHarvestService.update(id, harvest));
    }

    @DeleteMapping("/api/harvests/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        honeyHarvestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
