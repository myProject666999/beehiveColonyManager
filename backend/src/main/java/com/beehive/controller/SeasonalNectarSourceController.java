package com.beehive.controller;

import com.beehive.entity.SeasonalNectarSource;
import com.beehive.service.SeasonalNectarSourceService;
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
public class SeasonalNectarSourceController {

    private final SeasonalNectarSourceService seasonalNectarSourceService;

    @GetMapping("/api/nectar-sources")
    public ResponseEntity<List<SeasonalNectarSource>> findAll() {
        return ResponseEntity.ok(seasonalNectarSourceService.findAll());
    }

    @GetMapping("/api/nectar-sources/{id}")
    public ResponseEntity<SeasonalNectarSource> findById(@PathVariable Long id) {
        return ResponseEntity.ok(seasonalNectarSourceService.findById(id));
    }

    @GetMapping("/api/apiaries/{apiaryId}/nectar-sources")
    public ResponseEntity<List<SeasonalNectarSource>> findByApiaryId(@PathVariable Long apiaryId) {
        return ResponseEntity.ok(seasonalNectarSourceService.findByApiaryId(apiaryId));
    }

    @PostMapping("/api/nectar-sources")
    public ResponseEntity<SeasonalNectarSource> create(@RequestBody SeasonalNectarSource source) {
        return ResponseEntity.ok(seasonalNectarSourceService.create(source));
    }

    @PutMapping("/api/nectar-sources/{id}")
    public ResponseEntity<SeasonalNectarSource> update(@PathVariable Long id, @RequestBody SeasonalNectarSource source) {
        return ResponseEntity.ok(seasonalNectarSourceService.update(id, source));
    }

    @DeleteMapping("/api/nectar-sources/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seasonalNectarSourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
