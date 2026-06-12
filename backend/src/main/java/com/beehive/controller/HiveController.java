package com.beehive.controller;

import com.beehive.entity.Hive;
import com.beehive.service.HiveService;
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
public class HiveController {

    private final HiveService hiveService;

    @GetMapping("/api/hives")
    public ResponseEntity<List<Hive>> findAll() {
        return ResponseEntity.ok(hiveService.findAll());
    }

    @GetMapping("/api/hives/{id}")
    public ResponseEntity<Hive> findById(@PathVariable Long id) {
        return ResponseEntity.ok(hiveService.findById(id));
    }

    @GetMapping("/api/apiaries/{apiaryId}/hives")
    public ResponseEntity<List<Hive>> findByApiaryId(@PathVariable Long apiaryId) {
        return ResponseEntity.ok(hiveService.findByApiaryId(apiaryId));
    }

    @PostMapping("/api/hives")
    public ResponseEntity<Hive> create(@RequestBody Hive hive) {
        return ResponseEntity.ok(hiveService.create(hive));
    }

    @PutMapping("/api/hives/{id}")
    public ResponseEntity<Hive> update(@PathVariable Long id, @RequestBody Hive hive) {
        return ResponseEntity.ok(hiveService.update(id, hive));
    }

    @DeleteMapping("/api/hives/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hiveService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
