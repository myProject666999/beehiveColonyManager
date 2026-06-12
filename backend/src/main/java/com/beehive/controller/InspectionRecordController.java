package com.beehive.controller;

import com.beehive.entity.InspectionRecord;
import com.beehive.service.InspectionRecordService;
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
public class InspectionRecordController {

    private final InspectionRecordService inspectionRecordService;

    @GetMapping("/api/inspections")
    public ResponseEntity<List<InspectionRecord>> findAll() {
        return ResponseEntity.ok(inspectionRecordService.findAll());
    }

    @GetMapping("/api/inspections/{id}")
    public ResponseEntity<InspectionRecord> findById(@PathVariable Long id) {
        return ResponseEntity.ok(inspectionRecordService.findById(id));
    }

    @GetMapping("/api/hives/{hiveId}/inspections")
    public ResponseEntity<List<InspectionRecord>> findByHiveId(@PathVariable Long hiveId) {
        return ResponseEntity.ok(inspectionRecordService.findByHiveId(hiveId));
    }

    @PostMapping("/api/inspections")
    public ResponseEntity<InspectionRecord> create(@RequestBody InspectionRecord record) {
        return ResponseEntity.ok(inspectionRecordService.create(record));
    }

    @PutMapping("/api/inspections/{id}")
    public ResponseEntity<InspectionRecord> update(@PathVariable Long id, @RequestBody InspectionRecord record) {
        return ResponseEntity.ok(inspectionRecordService.update(id, record));
    }

    @DeleteMapping("/api/inspections/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inspectionRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
