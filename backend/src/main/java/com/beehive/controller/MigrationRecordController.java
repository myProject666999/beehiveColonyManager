package com.beehive.controller;

import com.beehive.entity.MigrationRecord;
import com.beehive.service.MigrationRecordService;
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
public class MigrationRecordController {

    private final MigrationRecordService migrationRecordService;

    @GetMapping("/api/migrations")
    public ResponseEntity<List<MigrationRecord>> findAll() {
        return ResponseEntity.ok(migrationRecordService.findAll());
    }

    @GetMapping("/api/migrations/{id}")
    public ResponseEntity<MigrationRecord> findById(@PathVariable Long id) {
        return ResponseEntity.ok(migrationRecordService.findById(id));
    }

    @GetMapping("/api/apiaries/{apiaryId}/migrations")
    public ResponseEntity<List<MigrationRecord>> findByApiaryId(@PathVariable Long apiaryId) {
        return ResponseEntity.ok(migrationRecordService.findByApiaryId(apiaryId));
    }

    @PostMapping("/api/migrations")
    public ResponseEntity<MigrationRecord> create(@RequestBody MigrationRecord record) {
        return ResponseEntity.ok(migrationRecordService.create(record));
    }

    @PutMapping("/api/migrations/{id}")
    public ResponseEntity<MigrationRecord> update(@PathVariable Long id, @RequestBody MigrationRecord record) {
        return ResponseEntity.ok(migrationRecordService.update(id, record));
    }

    @DeleteMapping("/api/migrations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        migrationRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
